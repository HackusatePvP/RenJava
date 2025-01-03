package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.loggers.RenLogger;

import java.io.File;

/**
 * Video scenes are not fully implemented yet and there are severe limitations.
 * Most video encoders are not supported and even if they are they most likely do not work on other operating systems.
 * If your video is rendering a black screen that means the video encoder is not supported by the os. H.264 is the more supported encoding but is superseded by HEVC.
 * HEVC is not supported by Windows by default.
 * <pre>
 * Supported types that may work are
 *     1. H.264 AVC (.mp4)
 *     2. H.265 HEVC (.mp4)
 *     3. VP6 (.vp6, .zzz)
 * </pre>
 * <p>
 * Make sure the video resolution is set the resolution of the game. If your game is configured to be 1080p then the video must also be 1080p.
 * RenJava can also auto-size the video to match the window resolution.
 */
public class MediaOverlay extends Overlay implements Region {
    private final String filePath;
    private double width = -1, height = -1;
    private double scaleWidth, scaleHeight;
    private boolean loop = false;
    private final Media media;

    public MediaOverlay(String filePath, int x, int y) {
        this.filePath = filePath;
        setX(x);
        setY(y);
        File file = new File(System.getProperty("user.dir") + "/game/" + filePath);
        if (!file.exists()) {
            RenLogger.LOGGER.error("Could not load media: {}", file.getPath());
        }
        media = new Media(file.toURI().toString());

        // Video should be on sound volune
        SettingsProperties settings = RenJava.SETTINGS;
        double masterVolume = settings.getMasterVolume();
        masterVolume = masterVolume / 100;
        double soundVolume = settings.getSoundVolume();
        soundVolume = masterVolume * soundVolume;
        RenJava.PLAYER.updatePlayingMedia(media);
    }

    public MediaOverlay(String filePath, int x, int y, int width, int height) {
        this.filePath = filePath;
        setX(x);
        setY(y);
        this.width = width;
        this.height = height;
        File file = new File(System.getProperty("user.dir") + "/game/" + filePath);
        if (!file.exists()) {
            RenLogger.LOGGER.error("Could not load media: {}", file.getPath());
        }
        media = new Media(file.toURI().toString());

        // Video should be on sound volune
        SettingsProperties settings = RenJava.SETTINGS;
        double masterVolume = settings.getMasterVolume();
        masterVolume = masterVolume / 100;
        double soundVolume = settings.getSoundVolume();
        soundVolume = masterVolume * soundVolume;

        RenJava.PLAYER.updatePlayingMedia(media);
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        if (loop) {
            RenJava.PLAYER.getCurrentMedia().setOnEndOfMedia(() -> {
                RenLogger.LOGGER.info("Media ended.");
                RenJava.PLAYER.getCurrentMedia().seek(Duration.ZERO);
                RenJava.PLAYER.getCurrentMedia().play();
            });
        }
    }

    public void play() {
        RenJava.PLAYER.getCurrentMedia().play();
    }

    public void stop() {
        RenJava.PLAYER.getCurrentMedia().stop();
    }

    @Override
    public Node render() {
        if (RenJava.PLAYER.getCurrentMedia() != null) {
            MediaPlayer player = new MediaPlayer(media);
            RenJava.PLAYER.setCurrentMedia(player);
            MediaView mediaView = new MediaView(player);
            if (width != -1) {
                mediaView.setFitWidth(width);
            }
            if (height != -1) {
                mediaView.setFitHeight(height);
            }
            RenLogger.LOGGER.info("Playing media '{}'", System.getProperty("user.dir") + "/" + filePath);
            setInputControls(mediaView);
            return mediaView;
        }
        RenLogger.LOGGER.warn("Could not get media player!");
        return null;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setWidth(double w) {
        this.width = w;
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
    }

    @Override
    public double getScaleWidth() {
        return scaleWidth;
    }

    @Override
    public void setScaleWidth(double w) {
        this.scaleWidth = w;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }
}
