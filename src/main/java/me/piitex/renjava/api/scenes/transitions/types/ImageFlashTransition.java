package me.piitex.renjava.api.scenes.transitions.types;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.overlay.ImageOverlay;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * This can be applied to a RenScene which will flash a series of images.
 */
public class ImageFlashTransition extends Transitions {
    private final Collection<ImageOverlay> images = new LinkedHashSet<>();
    private final Color transitionColor;

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
    public void play(Node node) {
        for (ImageOverlay imageOverlay : images) {
            ImageView imageView = new ImageView(imageOverlay.getImage());
            imageView.setFitWidth(imageOverlay.width());
            imageView.setFitHeight(imageOverlay.width());
            imageView.setX(imageOverlay.x());
            imageView.setY(imageOverlay.y());

            FadingTransition fadingTransition = new FadingTransition(0, 1, getDuration(), transitionColor);
            fadingTransition.play(imageView);
        }
    }

    @Override
    public void stop() {

    }

    public void addImages(ImageOverlay... imageOverlays) {
        images.addAll(Arrays.asList(imageOverlays));
    }

    public Color getTransitionColor() {
        return transitionColor;
    }
    
}
