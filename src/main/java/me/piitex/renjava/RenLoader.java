package me.piitex.renjava;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Stream;

import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.music.Track;
import me.piitex.renjava.configuration.SettingsProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class RenLoader {
    private final RenJava renJava;

    public RenLoader(RenJava renJava) {
        this.renJava = renJava;
        renJava.getLogger().info("Starting processes...");
        setupMain();
        setupGame();
        startPreProcess();
    }

    private void setupMain() {
        renJava.getLogger().info("Checking game environment...");
        File gameDirectory = new File(System.getProperty("user.dir") + "/game/");
        if (gameDirectory.mkdir()) {
            renJava.getLogger().severe("Game directory does not exist. The game will not work properly, please move all assets into the newly created game directory.");
        }
        File renJavaDirectory = new File(System.getProperty("user.dir") + "/renjava/");
        renJavaDirectory.mkdir();
        File logFile = new File(System.getProperty("user.dir"), "log.txt");
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
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
        imageDirectory.mkdir();
        File savesDirectory = new File(directory, "/saves/");
        savesDirectory.mkdir();
        File fontsDirectory = new File(directory, "/fonts/");
        fontsDirectory.mkdir();

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
        File directory = new File(System.getProperty("user.dir") + "/renjava/");
        directory.mkdir();
        renJava.setSettings(new SettingsProperties());

        // After this method jump to GuiLoader. Loading is a little confusing if you want an idea of how the loader works, check out the Launch class.
        // Essentially, the RenJava constructor is declared first which runs this (RenLoader). After the declaration of the RenJava class the GuiLoader is called in the start() function within
        // the Launch class. That was a mouth-full, but hopefully you figure it out!
    }

    private void loadRPAFiles() {
        // load rpa files
    }
}
