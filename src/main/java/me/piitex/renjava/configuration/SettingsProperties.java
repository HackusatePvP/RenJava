package me.piitex.renjava.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsProperties {
    private int volume = 50; // Don't want to ear blast people right off the start so half volume should work.
    private boolean fullscreen = false;
    private boolean skipTransitions = false;
    private boolean skipUnseenText = false;

    private final Properties properties = new Properties();

    public SettingsProperties() {
        File directory = new File(System.getProperty("user.dir") + "/renjava/");
        File file = new File(directory, "settings.properties");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                properties.load(new FileInputStream(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            properties.setProperty("skip-unseen-text", "false");
            properties.setProperty("transitions", "true");
            properties.setProperty("fullscreen", "false");
            properties.setProperty("volume", "50");
            try {
                properties.store(new FileOutputStream(file), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                properties.load(new FileInputStream(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.volume = Integer.parseInt(properties.getProperty("volume"));
            this.fullscreen = Boolean.parseBoolean(properties.getProperty("fullscreen"));
            this.skipTransitions = Boolean.parseBoolean(properties.getProperty("transitions"));
            this.skipUnseenText = Boolean.parseBoolean(properties.getProperty("skip-unseen-text"));
        }
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public boolean isSkipTransitions() {
        return skipTransitions;
    }

    public void setSkipTransitions(boolean skipTransitions) {
        this.skipTransitions = skipTransitions;
    }

    public boolean isSkipUnseenText() {
        return skipUnseenText;
    }

    public void setSkipUnseenText(boolean skipUnseenText) {
        this.skipUnseenText = skipUnseenText;
    }
}
