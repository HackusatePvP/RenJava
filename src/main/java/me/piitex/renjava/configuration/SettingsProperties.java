package me.piitex.renjava.configuration;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.loggers.RenLogger;

import java.io.*;
import java.util.Properties;

public class SettingsProperties {
    private final File file;
    private double masterVolume = 50; // Don't want to ear blast people right off the start so half volume should work.
    private double musicVolume = 100;
    private double soundVolume = 100;
    private double voiceVolume = 100;
    private boolean fullscreen = false;
    private boolean skipTransitions = false;
    private boolean skipUnseenText = false;
    private boolean multiThreading = true;

    private final Properties properties = new Properties();

    public SettingsProperties() {
        File directory = RenJava.getInstance().getBaseDirectory();
        this.file = new File(directory, "settings.properties");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                RenLogger.LOGGER.error("Error occurred while creating settings file!", e);
                RenJava.writeStackTrace(e);
            }
            try {
                properties.load(new FileInputStream(file));
            } catch (IOException e) {
                RenLogger.LOGGER.error("Error occurred while loading settings file!", e);
                RenJava.writeStackTrace(e);
            }
            properties.setProperty("skip-unseen-text", "false");
            properties.setProperty("transitions", "true");
            properties.setProperty("fullscreen", "false");
            properties.setProperty("master-volume", "50");
            properties.setProperty("music-volume", "50");
            properties.setProperty("sound-volume", "50");
            properties.setProperty("voice-volume", "50");
            properties.setProperty("multi-threading", "true");
            try {
                properties.store(new FileOutputStream(file), null);
            } catch (IOException e) {
                RenLogger.LOGGER.error("Error occurred while saving settings file!", e);
                RenJava.writeStackTrace(e);
            }
        } else {
            try {
                properties.load(new FileInputStream(file));
            } catch (IOException e) {
                RenLogger.LOGGER.error("Error occurred while loading settings file!", e);
                RenJava.writeStackTrace(e);
            }
            this.masterVolume = Double.parseDouble(properties.getProperty("master-volume", "50"));
            this.musicVolume = Double.parseDouble(properties.getProperty("music-volume", "100"));
            this.soundVolume = Double.parseDouble(properties.getProperty("sound-volume", "100"));
            this.voiceVolume = Double.parseDouble(properties.getProperty("voice-volume", "100"));
            this.fullscreen = Boolean.parseBoolean(properties.getProperty("fullscreen", "false"));
            this.skipTransitions = Boolean.parseBoolean(properties.getProperty("transitions", "false"));
            this.skipUnseenText = Boolean.parseBoolean(properties.getProperty("skip-unseen-text", "false"));
            this.multiThreading = Boolean.parseBoolean(properties.getProperty("multi-threading", "true"));
        }
    }

    public double getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(double volume) {
        this.masterVolume = volume;
        write("master-volume", volume + "");
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = volume;
        write("music-volume", volume + "");
    }

    public double getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(double volume) {
        this.soundVolume = volume;
        write("sound-volume", volume + "");
    }

    public double getVoiceVolume() {
        return voiceVolume;
    }

    public void setVoiceVolume(double volume) {
        this.voiceVolume = volume;
        write("voice-volume", volume + "");
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public boolean isMultiThreading() {
        return multiThreading;
    }

    public void setMultiThreading(boolean multiThreading) {
        this.multiThreading = multiThreading;
        write("multi-threading", multiThreading + "");
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        write("fullscreen", fullscreen + "");
    }

    public boolean isSkipTransitions() {
        return skipTransitions;
    }

    public void setSkipTransitions(boolean skipTransitions) {
        this.skipTransitions = skipTransitions;
        write("skip-unseen-tex", skipTransitions + "");
    }

    public boolean isSkipUnseenText() {
        return skipUnseenText;
    }

    public void setSkipUnseenText(boolean skipUnseenText) {
        this.skipUnseenText = skipUnseenText;
    }

    private void write(String key, String value) {
        try {
            OutputStream outputStream = new FileOutputStream(file);
            properties.setProperty(key, value);
            properties.store(outputStream, null);
            outputStream.close();
        } catch (IOException e) {
            RenLogger.LOGGER.error("Error occurred while writing settings file!", e);
            RenJava.writeStackTrace(e);
        }
    }
}
