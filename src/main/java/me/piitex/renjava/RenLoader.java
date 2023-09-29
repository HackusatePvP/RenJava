package me.piitex.renjava;

import java.io.*;
import java.util.Properties;

import me.piitex.renjava.api.stories.StoryManager;

public class RenLoader {
    private final RenJava renJava;

    public RenLoader(RenJava renJava) {
        this.renJava = renJava;
        setupMain();
    }

    private void setupMain() {
        File gameDirectory = new File(System.getProperty("user.dir") + "/game/");
        gameDirectory.mkdir();
        File logFile = new File(System.getProperty("user.dir"), "log.txt");
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupGame();
    }

    private void setupGame() {
        File directory = new File(System.getProperty("user.dir") + "/game/");
        File audioDirectory = new File(directory, "/audio/");
        audioDirectory.mkdir();
        File imageDirectory = new File(directory, "/images/");
        imageDirectory.mkdir();
        File savesDirectory = new File(directory, "/saves/");
        savesDirectory.mkdir();
        File fontsDirectory = new File(directory, "/fonts/");
        fontsDirectory.mkdir();
        File cssDirectory = new File(directory, "/css/");
        cssDirectory.mkdir();
        startPreProcess();
    }

    private void startPreProcess() {
        renJava.getLogger().info("Generating pre-load data...");
        loadRPAFiles();
        renJava.preEnabled();
        renJava.setStoryManager(new StoryManager());

        // Build setting file
        File directory = new File(System.getProperty("user.dir") + "/renjava/");
        directory.mkdir();
        File file = new File(directory, "settings.properties");
        if (!file.exists()) {
            // Create file and preset values
            try {
                file.createNewFile();
                Properties properties = new Properties();
                properties.load(new FileInputStream(file));
                properties.setProperty("skip-unseen-text", "false");
                properties.setProperty("transitions", "true");
                properties.setProperty("fullscreen", "false");
                properties.setProperty("volume", "1");
                properties.store(new FileOutputStream(file), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadRPAFiles() {
        // load rpa files
    }

}
