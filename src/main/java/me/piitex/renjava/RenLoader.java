package me.piitex.renjava;

import java.io.*;
import java.util.Properties;

import javafx.application.Platform;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.music.Track;
import me.piitex.renjava.configuration.SettingsProperties;

public class RenLoader {
    private final RenJava renJava;
    boolean shutdown = false;
    public RenLoader(RenJava renJava) {
        this.renJava = renJava;
        renJava.getLogger().info("Starting processes...");
        renJava.buildVersion = getVersion();
        setupMain();
        setupGame();
        if (shutdown) {
            // Shutdown application.
            renJava.getLogger().severe("Game assets do not exist. Please download default assets and place them inside the 'game' folder.");
            Platform.exit();
            System.exit(0);
            return;
        }
        startPreProcess();
    }

    private void setupMain() {
        renJava.getLogger().info("Checking game environment...");


        File gameDirectory = new File(System.getProperty("user.dir") + "/game/");
        if (gameDirectory.mkdir()) {
            renJava.getLogger().severe("Game directory does not exist. The game will not work properly, please move all assets into the newly created game directory.");
            shutdown = true;
        }
        File renJavaDirectory = new File(System.getProperty("user.dir") + "/renjava/");
        if (renJavaDirectory.mkdir()) {
            renJava.getLogger().warning("RenJava folder does not exist. User settings will be reset to defaults.");
        }
        File logFile = new File(System.getProperty("user.dir"), "log.txt");
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            renJava.getLogger().warning("Could not create log file. Ensure the application has read and write permissions.");
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
        renJava.getLogger().info("Loaded " + audioLoaded + " audio file(s)");
        File imageDirectory = new File(directory, "/images/");
        if (imageDirectory.mkdir()) {
            renJava.getLogger().warning("Images folder does not exist, creating...");
        }
        File savesDirectory = new File(directory, "/saves/");
        if (savesDirectory.mkdir()) {
            renJava.getLogger().warning("Saves folder does not exist, creating...");
        }
        File fontsDirectory = new File(directory, "/fonts/");
        if (fontsDirectory.mkdir()) {
            renJava.getLogger().warning("Fonts folder does not exist, creating...");
        }

        renJava.getLogger().info("Loading fonts...");
        int fonts = 0;
        for (File file : fontsDirectory.listFiles()) {
            if (file.getName().endsWith(".ttf")) {
                fonts++;
                new FontLoader(file.getName());
            }
        }
        renJava.getLogger().info("Loaded " + fonts + " font(s).");
        File cssDirectory = new File(directory, "/css/");
        cssDirectory.mkdir();
    }

    private void startPreProcess() {
        renJava.getLogger().info("Generating pre-load data...");
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
