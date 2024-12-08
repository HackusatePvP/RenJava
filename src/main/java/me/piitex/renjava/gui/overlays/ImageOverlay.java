package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.loggers.RenLogger;

public class ImageOverlay extends Overlay implements Region {
    private Image image;
    private double scaleX, scaleY, scaleWidth, scaleHeight;
    private double width;
    private double height;
    private boolean preserveRatio = true;
    private final String fileName;
    private String path = "Unknown";

    public ImageOverlay(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.fileName = "Unknown";
    }

    public ImageOverlay(WritableImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.fileName = "Unknown";
    }

    public ImageOverlay(ImageLoader imageLoader) {
        try {
            this.image = imageLoader.build();
        } catch (ImageNotFoundException e) {
            RenLogger.LOGGER.error(e.getMessage(), e);
            RenJava.writeStackTrace(e);
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.fileName = imageLoader.getFile().getName();
    }

    public ImageOverlay(String imagePath) {
        ImageLoader loader = new ImageLoader(imagePath);
        try {
            this.image = loader.build();
        } catch (ImageNotFoundException e) {
            RenLogger.LOGGER.error(e.getMessage(), e);
            RenJava.writeStackTrace(e);
        }
        this.fileName = loader.getFile().getName();
        this.path = imagePath;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public ImageOverlay(String directory, String imagePath) {
        ImageLoader loader = new ImageLoader(directory, imagePath);
        try {
            this.image = loader.build();
        } catch (ImageNotFoundException e) {
            RenLogger.LOGGER.error(e.getMessage(), e);
            RenJava.writeStackTrace(e);
        }
        this.fileName = loader.getFile().getName();
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public ImageOverlay(Image image, double x, double y) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        setX(x);
        setY(y);
        this.fileName = "Unknown";
    }

    public ImageOverlay(ImageLoader imageLoader, double x, double y) {
        try {
            this.image = imageLoader.build();
        } catch (ImageNotFoundException e) {
            RenLogger.LOGGER.error(e.getMessage(), e);
            RenJava.writeStackTrace(e);
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.fileName = imageLoader.getFile().getName();
        setX(x);
        setY(y);
    }

    public Image getImage() {
        return image;
    }

    public String getFileName() {
        return fileName;
    }


    public boolean isPreserveRatio() {
        return preserveRatio;
    }

    public void setPreserveRatio(boolean preserveRatio) {
        this.preserveRatio = preserveRatio;
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
    public void setScaleWidth(double scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }

    @Override
    public Node render() {
        Image image = getImage();
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        if (width != 0) {
            imageView.setFitWidth(width);
        }
        if (height != 0) {
            imageView.setFitHeight(height);
        }
        imageView.setTranslateX(getX());
        imageView.setTranslateY(getY());
        setInputControls(imageView);
        renderTransitions(imageView);
        return imageView;
    }
}
