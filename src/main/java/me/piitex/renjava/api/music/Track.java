package me.piitex.renjava.api.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.loggers.RenLogger;

import java.io.File;

public class Track {
    private final String id;
    private final File file;
    private boolean loop;
    private MediaPlayer player;

    public Track(File file) {
        this.id = file.getName();
        this.file = file;
    }

    public String getId() {
        return id;
    }

    protected void play(boolean loop) {
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
        RenLogger.LOGGER.warn("Volume: " + RenJava.getInstance().getSettings().getVolume() / 500d);
        player.setVolume(RenJava.getInstance().getSettings().getVolume() / 500d);
        player.play();
    }

    public boolean isLoop() {
        return loop;
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