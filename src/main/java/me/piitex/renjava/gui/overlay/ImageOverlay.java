package me.piitex.renjava.gui.overlay;

import javafx.scene.image.Image;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

public class ImageOverlay implements Overlay {
    private final Image image;
    private double x;
    private double y;
    private final String fileName;

    private Transitions transitions;

    public ImageOverlay(Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.fileName = "Unknown";
    }

    public ImageOverlay(ImageLoader imageLoader, double x, double y) {
        try {
            this.image = imageLoader.build();
            this.fileName = imageLoader.getFile().getName();
        } catch (ImageNotFoundException e) {
            RenJava.getInstance().getLogger().severe(e.getMessage());
            throw new RuntimeException();
        }
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }
}
