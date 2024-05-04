package me.piitex.renjava.api.loaders;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import me.piitex.renjava.RenJava;

import java.io.File;

public class VideoLoader {
    private final File file;
    private final Media media;

    private MediaPlayer player;

    public VideoLoader(String name) {
        this.file = new File(System.getProperty("user.dir") + "/images/" + name);
        media = new Media(file.toURI().toString());
    }

    public Media getMedia() {
        return media;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    /**
     * Plays the video file for the current scene.
     * @param loop If set to true the video will loop until the next scene is displayed.
     */
    public void play(boolean loop) {
        player = new MediaPlayer(media);
        player.setVolume(RenJava.getInstance().getSettings().getVolume() / 500d);
        player.play();
        if (loop) {
            player.setCycleCount(MediaPlayer.INDEFINITE);
        }
    }

    /**
     * Stops the video from playing.
     */
    public void stop() {
        if (player != null) {
            player.stop();
        }
    }
}
