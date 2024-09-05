package me.piitex.renjava.api.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.loggers.RenLogger;

import java.io.File;

public class Track {
    private final String id;
    private final File file;
    private boolean loop, playing;
    private MediaPlayer player;

    // Flags
    private boolean music = false, sound = false, voice = false;

    public Track(File file) {
        this.id = file.getName();
        this.file = file;
    }

    public String getId() {
        return id;
    }

    protected void playMusic(boolean loop) {
        this.music = true;
        this.playing = true;
        this.loop = loop;
        player = new MediaPlayer(new Media(file.toURI().toString()));
        if (loop) {
            player.setOnEndOfMedia(() -> {
                player.seek(Duration.ZERO);
                player.play();
            });
        } else {
            player.setOnEndOfMedia(() -> {
                RenJava.getInstance().getTracks().setPlaying(false);
            });
        }
        //RenLogger.LOGGER.warn("Volume: " + RenJava.getInstance().getSettings().getVolume() / 500d);
        SettingsProperties settings = RenJava.getInstance().getSettings();
        double masterVolume = settings.getMasterVolume();
        masterVolume = masterVolume / 100;
        double musicVolume = settings.getMusicVolume();
        musicVolume = masterVolume * musicVolume;
        RenLogger.LOGGER.warn("Volume: " + musicVolume / 500d);
        // 100 master
        // 50

        // 50 master
        // 100 music

        // x = master / 100
        // 50 / 100 x = .5
        //
        player.setVolume(musicVolume / 500d);
        player.play();
        player.setOnStopped(new Runnable() {
            @Override
            public void run() {
                playing = false;
            }
        });
    }

    protected void playSound(boolean loop) {
        this.sound = true;
        this.loop = loop;
        this.playing = true;
        player = new MediaPlayer(new Media(file.toURI().toString()));
        if (loop) {
            player.setOnEndOfMedia(() -> {
                player.seek(Duration.ZERO);
                player.play();
            });
        } else {
            player.setOnEndOfMedia(() -> {
                RenJava.getInstance().getTracks().setPlaying(false);
            });
        }
        SettingsProperties settings = RenJava.getInstance().getSettings();
        double masterVolume = settings.getMasterVolume();
        masterVolume = masterVolume / 100;
        double soundVolume = settings.getSoundVolume();
        soundVolume = masterVolume * soundVolume;
        player.setVolume(soundVolume / 500d);
        player.play();
        player.setOnStopped(new Runnable() {
            @Override
            public void run() {
                playing = false;
            }
        });
    }

    protected void playVoice(boolean loop) {
        this.voice = true;
        this.loop = loop;
        this.playing = true;
        player = new MediaPlayer(new Media(file.toURI().toString()));
        if (loop) {
            player.setOnEndOfMedia(() -> {
                player.seek(Duration.ZERO);
                player.play();
            });
        } else {
            player.setOnEndOfMedia(() -> {
                RenJava.getInstance().getTracks().setPlaying(false);
            });
        }
        SettingsProperties settings = RenJava.getInstance().getSettings();
        double masterVolume = settings.getMasterVolume();
        masterVolume = masterVolume / 100;
        double voiceVolume = settings.getVoiceVolume();
        voiceVolume = masterVolume * voiceVolume;
        player.setVolume(voiceVolume / 500d);
        player.play();
        player.setOnStopped(new Runnable() {
            @Override
            public void run() {
                playing = false;
            }
        });
    }

    public void setVolume(double volume) {
        double masterVolume = RenJava.getInstance().getSettings().getMasterVolume();
        masterVolume = masterVolume / 100;
        getPlayer().setVolume(volume * masterVolume / 500d);
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public boolean isLoop() {
        return loop;
    }

    public boolean isMusic() {
        return music;
    }

    public boolean isSound() {
        return sound;
    }

    public boolean isVoice() {
        return voice;
    }

    public boolean isPlaying() {
        return playing;
    }

    protected void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public File getFile() {
        return file;
    }
}