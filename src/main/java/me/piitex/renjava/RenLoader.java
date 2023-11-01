package me.piitex.renjava;

import java.io.*;

import me.piitex.renjava.api.music.Track;
import me.piitex.renjava.configuration.SettingsProperties;

public class RenLoader {
    private final RenJava renJava;

    public RenLoader(RenJava renJava) {
        this.renJava = renJava;
        setupMain();
        setupGame();
        startPreProcess();
    }

    private void setupMain() {
        File gameDirectory = new File(System.getProperty("user.dir") + "/game/");
        gameDirectory.mkdir();
        File renJavaDirectory = new File(System.getProperty("user.dir") + "/renjava/");
        renJavaDirectory.mkdir();
        File logFile = new File(System.getProperty("user.dir"), "log.txt");
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
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
        imageDirectory.mkdir();
        File savesDirectory = new File(directory, "/saves/");
        savesDirectory.mkdir();
        File fontsDirectory = new File(directory, "/fonts/");
        fontsDirectory.mkdir();
        File cssDirectory = new File(directory, "/css/");
        cssDirectory.mkdir();
    }

    private void startPreProcess() {
        renJava.getLogger().info("Generating pre-load data...");
        loadRPAFiles();
        renJava.preEnabled();

        // Build setting file
        File directory = new File(System.getProperty("user.dir") + "/renjava/");
        directory.mkdir();
        renJava.setSettings(new SettingsProperties());
    }

    private void loadRPAFiles() {
        // load rpa files
    }
}
