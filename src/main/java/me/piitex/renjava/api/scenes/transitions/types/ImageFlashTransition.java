package me.piitex.renjava.api.scenes.transitions.types;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.overlays.ImageOverlay;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * This can be applied to a RenScene which will flash a series of images.
 */
public class ImageFlashTransition extends Transitions {
    private Collection<ImageOverlay> images = new LinkedHashSet<>();
    private final Color transitionColor;
    private FadingTransition currentTransition;

    /**
     *
     * @param duration The time for the transition effect, which is also the time in between each image.
     * @param transitionColor
     */
    public ImageFlashTransition(double duration, Color transitionColor) {
        super(duration);
        this.transitionColor = transitionColor;
    }

    @Override
    public boolean isPlaying() {
        return (currentTransition != null && currentTransition.isPlaying());
    }

    @Override
    public void play(Node node) {
        for (ImageOverlay imageOverlay : images) {
            ImageView imageView = new ImageView(imageOverlay.getImage());
            imageView.setFitWidth(imageOverlay.getWidth());
            imageView.setFitHeight(imageOverlay.getHeight());
            imageView.setX(imageOverlay.getX());
            imageView.setY(imageOverlay.getY());
            FadingTransition fadingTransition = new FadingTransition(0, 1, getDuration(), transitionColor);
            fadingTransition.play(imageView);
        }
    }

    @Override
    public void play(RenScene scene) {

    }

    @Override
    public void stop() {
        currentTransition.stop();
        images = null;
    }

    public void addImages(ImageOverlay... imageOverlays) {
        images.addAll(Arrays.asList(imageOverlays));
    }

    public Color getTransitionColor() {
        return transitionColor;
    }
    
}
