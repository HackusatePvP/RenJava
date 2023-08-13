package me.piitex.renjava;

import java.io.*;
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
    }

    private void loadRPAFiles() {
        // load rap files
    }

}
