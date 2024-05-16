package me.piitex.renjava.gui.overlay;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.gui.overlay.events.IOverlayClick;
import me.piitex.renjava.gui.overlay.events.IOverlayHover;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

public class ImageOverlay implements Overlay {
    private Image image;
    private double x;
    private double y;
    private double scaleX, scaleY;
    private double width;
    private double height;
    private boolean preserveRatio = true;
    private String fileName;

    private IOverlayHover iOverlayHover;

    private IOverlayClick iOverlayClick;

    private Transitions transitions;

    public ImageOverlay(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.x = 0;
        this.y = 0;
        this.fileName = "Unknown";
    }

    public ImageOverlay(WritableImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.x = 0;
        this.y = 0;
        this.fileName = "Unknown";
    }

    public ImageOverlay(ImageLoader imageLoader) {
        try {
            this.image = imageLoader.build();
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.fileName = imageLoader.getFile().getName();
        } catch (ImageNotFoundException e) {
            RenJava.getInstance().getLogger().error(e.getMessage());
        }
        this.x = 0;
        this.y = 0;
    }

    public ImageOverlay(String imagePath) {
        ImageLoader loader = new ImageLoader(imagePath);
        try {
            this.image = loader.build();
            this.fileName = loader.getFile().getName();
            this.width = image.getWidth();
            this.height = image.getHeight();
        } catch (ImageNotFoundException e) {
            RenJava.getInstance().getLogger().error(e.getMessage());
        }
        this.x = 0;
        this.y = 0;
    }

    public ImageOverlay(String directory, String imagePath) throws ImageNotFoundException {
        ImageLoader loader = new ImageLoader(directory, imagePath);
        this.image = loader.build();
        this.fileName = loader.getFile().getName();
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.x = 0;
        this.y = 0;
    }

    public ImageOverlay(Image image, double x, double y) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.x = x;
        this.y = y;
        this.fileName = "Unknown";
    }

    public ImageOverlay(ImageLoader imageLoader, double x, double y) {
        try {
            this.image = imageLoader.build();
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.fileName = imageLoader.getFile().getName();
        } catch (ImageNotFoundException e) {
            RenLogger.LOGGER.error(e.getMessage());
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
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double scaleX() {
        return scaleX;
    }

    @Override
    public double scaleY() {
        return scaleY;
    }

    @Override
    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    @Override
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    @Override
    public double width() {
        return width;
    }

    @Override
    public double height() {
        return height;
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isPreserveRatio() {
        return preserveRatio;
    }

    public void setPreserveRatio(boolean preserveRatio) {
        this.preserveRatio = preserveRatio;
    }

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    @Override
    public void setOnclick(IOverlayClick iOverlayClick) {
        this.iOverlayClick = iOverlayClick;
    }

    @Override
    public void setOnHover(IOverlayHover iOverlayHover) {
        this.iOverlayHover = iOverlayHover;
    }

    @Override
    public IOverlayClick getOnClick() {
        return iOverlayClick;
    }

    @Override
    public IOverlayHover getOnHover() {
        return iOverlayHover;
    }

    public ImageOverlay setTransitions(Transitions transitions) {
        this.transitions = transitions;
        return this;
    }
}
