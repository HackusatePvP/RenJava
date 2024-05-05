package me.piitex.renjava;

import java.io.*;
import java.util.Properties;

import javafx.application.Platform;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.music.Track;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.loggers.RenLogger;
;

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
            // Shutdown application.
            RenLogger.LOGGER.error("Game assets do not exist. Please download default assets and place them inside the 'game' folder.");
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
            RenLogger.LOGGER.error("Game directory does not exist. The game will not work properly, please move all assets into the newly created game directory.");
            shutdown = true;
        }
        File renJavaDirectory = new File(System.getProperty("user.dir") + "/renjava/");
        if (renJavaDirectory.mkdir()) {
            RenLogger.LOGGER.warn("RenJava folder does not exist. User settings will be reset to defaults.");
        }
        for (File file : new File(System.getProperty("user.dir")).listFiles()) {
            if (file.getName().endsWith(".txt.lck")) {
                file.delete();
            }
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
        }
        File savesDirectory = new File(directory, "/saves/");
        if (savesDirectory.mkdir()) {
            RenLogger.LOGGER.warn("Saves folder does not exist, creating...");
        }
        File fontsDirectory = new File(directory, "/fonts/");
        if (fontsDirectory.mkdir()) {
            RenLogger.LOGGER.warn("Fonts folder does not exist, creating...");
        }

        RenLogger.LOGGER.info("Loading fonts...");
        int fonts = 0;
        for (File file : fontsDirectory.listFiles()) {
            if (file.getName().endsWith(".ttf")) {
                fonts++;
                new FontLoader(file.getName());
            }
        }
        RenLogger.LOGGER.info("Loaded " + fonts + " font(s).");
        File cssDirectory = new File(directory, "/css/");
        cssDirectory.mkdir();
    }

    private void startPreProcess() {
        RenLogger.LOGGER.info("Generating pre-load data...");
        loadRPAFiles();
        renJava.preEnabled();

        // Build setting file
        renJava.setSettings(new SettingsProperties());

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
