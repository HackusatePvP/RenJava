package me.piitex.renjava;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javafx.application.Platform;
import me.piitex.renjava.api.music.Track;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.loggers.RenLogger;

public class RenLoader {
    private final RenJava renJava;
    boolean shutdown = false;

    public RenLoader(RenJava renJava) {
        this.renJava = renJava;
        RenLogger.LOGGER.info("Starting processes...");
        renJava.buildVersion = getVersion();
        setupMain();
        setupGame();
        if (shutdown) {
            // Shutdown application if startup fails.
            Platform.exit();
            System.exit(0);
            return;
        }
        startPreProcess();
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
    }

    private void startPreProcess() {
        RenLogger.LOGGER.info("Generating pre-load data...");
        loadRPAFiles();
        renJava.preEnabled();

        // Build setting file
        renJava.setSettings(new SettingsProperties());

        // Move Save files to APPDATA
        if (renJava.getName().equalsIgnoreCase("error") && renJava.getAuthor().equalsIgnoreCase("error")) return;
        File directory = new File(System.getenv("APPDATA") + "/RenJava/" + renJava.getName() + "-" + renJava.getAuthor() + "/");
        File localSaves = new File(directory, "saves/");
        localSaves.mkdirs();

        // Transfer local saves to game saves if the slot doesn't exist.
        for (File file : localSaves.listFiles()) {
            File currentSaveFile = new File(System.getProperty("user.dir") + "/game/saves/" + file.getName());
            if (currentSaveFile.exists()) continue;
            try {
                Files.copy(Path.of(file.getPath()), Path.of(currentSaveFile.getPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
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
