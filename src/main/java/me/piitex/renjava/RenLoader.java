package me.piitex.renjava;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Scanner;

import javafx.application.Platform;
import me.piitex.renjava.api.music.Track;
import me.piitex.renjava.configuration.InfoFile;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.utils.MDUtils;
import me.piitex.renjava.tasks.Tasks;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.software.os.OperatingSystem;

public class RenLoader {
    private final RenJava renJava;
    boolean shutdown = false;

    public RenLoader(RenJava renJava) {
        this.renJava = renJava;
        RenLogger.LOGGER.info("Starting processes...");
        renJava.buildVersion = getVersion();
        RenLogger.LOGGER.info("Ren Version: {}", renJava.buildVersion);
        setupMain();
        setupGame();
        if (shutdown) {
            // Shutdown application if startup fails.
            Platform.exit();
            System.exit(0);
            return;
        }

        this.startPreProcess();

        // This will collect system and RenJava information. Used for me or developers to diagnose issues with the application.
        // RenJava estimated requirements are as follows;
        //    1. 4 Core CPU
        //    2. 2GB Vram
        //    3. 4GB Ram
        // Anything less cam result in crashes.
        // Ran on different thread as to not thread block the gui loader.
        Tasks.runAsync(() -> {
            StringBuilder builder = new StringBuilder();
            builder.append("Compiling system information...");
            // Get system information
            // New dependency to do this
            SystemInfo systemInfo = new SystemInfo();


            // Operating System
            OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
            builder.append("\n\t").append("OS: ").append(operatingSystem.getManufacturer()).append(" ").append(operatingSystem.getFamily());
            builder.append("\n\tArchitecture: ").append(operatingSystem.getBitness());
            builder.append("\n\tVersion: ").append(operatingSystem.getVersionInfo());

            builder.append("\n");
            CentralProcessor centralProcessor = systemInfo.getHardware().getProcessor();
            builder.append("\n\t").append("CPU: ").append(centralProcessor.getProcessorIdentifier().getName());
            builder.append("\n\t").append("Logical Cores: ").append(centralProcessor.getLogicalProcessorCount());
            builder.append("\n\t").append("Physical Cores: ").append(centralProcessor.getPhysicalProcessorCount());

            GlobalMemory memory = systemInfo.getHardware().getMemory();
            builder.append("\n\t").append("Available Memory: ").append(memory.getAvailable() / 1000000000).append("GB");
            builder.append("\n\t").append("System Memory: ").append(memory.getTotal() / 1000000000).append("GB");

            builder.append("\n");
            for (GraphicsCard graphicsCard : systemInfo.getHardware().getGraphicsCards()) {
                builder.append("\n\t").append("Graphics Card: ").append(graphicsCard.getName());
                builder.append("\n\t").append("Device ID: ").append(graphicsCard.getDeviceId());
                builder.append("\n\t").append("Video Memory: ").append(graphicsCard.getVRam() / 1000000000).append("GB");
                builder.append("\n\t").append("Graphics Version: ").append(graphicsCard.getVersionInfo());
                builder.append("\n");
            }

            RenLogger.LOGGER.debug(builder.toString());

            if (centralProcessor.getLogicalProcessorCount() <= 2) {
                // If running 2 logical processes automatically disable multi-threading.
                RenLogger.LOGGER.error("Your machine does not meet system requirements. Multi-threading has been automatically disabled.");
                RenJava.getInstance().getSettings().setMultiThreading(false);
            }
        });
    }

    private void setupMain() {
        RenLogger.LOGGER.info("Checking game environment...");
        File gameDirectory = new File(System.getProperty("user.dir") + "/game/");
        if (gameDirectory.mkdir()) {
            RenLogger.LOGGER.error("Game assets do not exist. Please download default assets and place them inside the 'game/images/gui' folder.");
            shutdown = true;
        }

        // Verify GUI folder
        File guiDirectory = new File(gameDirectory, "/images/gui/");
        if (!guiDirectory.exists() || guiDirectory.listFiles().length == 0) {
            RenLogger.LOGGER.error("GUI directory does not exist. The game will not work properly, please move all assets into the newly created gui directory.");
            guiDirectory.mkdir();
            shutdown = true;
        }

        File renJavaDirectory = new File(System.getProperty("user.dir") + "/renjava/");
        if (renJavaDirectory.mkdir()) {
            RenLogger.LOGGER.warn("RenJava folder does not exist. User settings will be reset to defaults.");
        }
    }

    private void setupGame() {
        File directory = new File(System.getProperty("user.dir") + "/game/");
        File audioDirectory = new File(directory, "/audio/");
        audioDirectory.mkdir();

        int audioLoaded = 0;
        for (File file : audioDirectory.listFiles()) {
            audioLoaded++;
            renJava.getTracks().addTrack(new Track(file));
        }
        RenLogger.LOGGER.info("Loaded " + audioLoaded + " audio file(s)");
        File imageDirectory = new File(directory, "/images/");
        if (imageDirectory.mkdir()) {
            RenLogger.LOGGER.warn("Images folder does not exist, creating...");
            RenLogger.LOGGER.error("Default assets do not exist. Please run RSDK to install these assets or download them from RenPy.");
            shutdown = true;
        }
        File savesDirectory = new File(directory, "/saves/");
        if (savesDirectory.mkdir()) {
            RenLogger.LOGGER.warn("Saves folder does not exist, creating...");
        }
        File fontsDirectory = new File(directory, "/fonts/");
        if (fontsDirectory.mkdir()) {
            RenLogger.LOGGER.warn("Fonts folder does not exist, creating...");
        }
        File cssDirectory = new File(directory, "/css/");
        if (cssDirectory.mkdir() || cssDirectory.listFiles().length == 0) {
            RenLogger.LOGGER.error("Default css file(s) do not exist. Please run RSDK to install these assets.");
            shutdown = true;
        }

        // Register current user settings
        renJava.setSettings(new SettingsProperties());

        // Delete old stacktrace
        File file = new File(System.getProperty("user.dir") + "/stacktrace.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    private void startPreProcess() {
        RenLogger.LOGGER.info("Generating pre-load data...");
        RenLogger.LOGGER.info("Checking Game ID...");

        InfoFile infoFile = new InfoFile(new File(System.getProperty("user.dir") + "/renjava/build.info"), true);
        if (infoFile.containsKey("id")) {
            renJava.id = infoFile.getInt("id");
        } else {
            renJava.id = MDUtils.getGameID(renJava.getName() + renJava.getAuthor());
            infoFile.write("id", renJava.id + "");
        }

        loadRPAFiles();
        renJava.preEnabled();

        // Move Save files to APPDATA
        if (renJava.getName().equalsIgnoreCase("error") && renJava.getAuthor().equalsIgnoreCase("error")) return;
        File directory = new File(System.getenv("APPDATA") + "/RenJava/" + renJava.getID() + "/");
        File localSaves = new File(directory, "saves/");
        localSaves.mkdirs();

        // Transfer local saves to game saves if the slot doesn't exist.
        for (File file : localSaves.listFiles()) {
            File currentSaveFile = new File(System.getProperty("user.dir") + "/game/saves/" + file.getName());
            if (currentSaveFile.exists()) continue;
            try {
                Files.copy(Path.of(file.getPath()), Path.of(currentSaveFile.getPath()));
            } catch (IOException e) {
                RenLogger.LOGGER.error("Could not copy local saves!", e);
                RenJava.writeStackTrace(e);
            }
        }

        // After this method jump to GuiLoader. Loading is a little confusing if you want an idea of how the loader works, check out the Launch class.
        // Essentially, the RenJava constructor is declared first which runs this (RenLoader). After the declaration of the RenJava class the GuiLoader is called in the start() function within
        // the Launch class. That was a mouth-full, but hopefully you figure it out!
    }

    private void loadRPAFiles() {
        // load rpa files
    }

    public synchronized String getVersion() {
        String version = null;

        // try to load from maven properties first
        try {
            Properties p = new Properties();
            InputStream is = getClass().getResourceAsStream("/META-INF/maven/me.piitex/RenJava/pom.properties");
            if (is != null) {
                p.load(is);
                version = p.getProperty("version", "");
            }
        } catch (Exception e) {
            // ignore
        }

        // fallback to using Java API
        if (version == null) {
            Package aPackage = getClass().getPackage();
            if (aPackage != null) {
                version = aPackage.getImplementationVersion();
                if (version == null) {
                    version = aPackage.getSpecificationVersion();
                }
            }
        }

        if (version == null) {
            // we could not compute the version so use a blank
            version = "";
        }

        return version.replace("-SNAPSHOT", "");
    }

}
