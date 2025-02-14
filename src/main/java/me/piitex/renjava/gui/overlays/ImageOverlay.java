package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.api.exceptions.ImageNotFoundException;
import me.piitex.renjava.loggers.RenLogger;

/**
 * The ImageOverlay is a visual element used to display images. The ImageOverlay uses the {@link ImageLoader} to load images into the application. The image must be in either the classpath or the game directory to be loaded.
 * The recommended directory is the 'images' directory inside the 'game' folder. Not all image formats are supported.
 * <pre>
 *  Below is an example directory tree.
 *     MyGame
 *       Lgame
 *         Limages
 *           Limage.png
 * </pre>
 * <pre>
 *   {@code
 *     ImageOverlay image = new ImageOverlay("image.png"); // By default the image will load from the 'game/images' directory.
 *
 *     // Load from root directory.
 *     ImageOverlay image = new ImageOverlay("", "image.png");
 *
 *     // Load outside of game directory.
 *     ImageOverlay image = new ImageOverlay("logs", "image.png");
 *   }
 * </pre>
 * An image is a {@link Region} since it has a defined shape. You can modify the width and height of an image to enlarge or shrink it. The image can become pixilated/stretched if the aspect ratio is not maintained.
 * <pre>
 *     {@code
 *       ImageOverlay image = new ImageOverlay("image.png");
 *       image.setWidth(1920);
 *       image.setHeight(1080);
 *     }
 * </pre>
 * Make sure images are compressed to normal sizes. Images which have expressive sizes will result in longer rendering times and slower performance.
 * If the image file could not be located or found it will throw {@link ImageNotFoundException}.
 *
 * @see ImageLoader
 * @see Region
 * @see Overlay
 */
public class ImageOverlay extends Overlay implements Region {
    private Image image;
    private double scaleX, scaleY, scaleWidth, scaleHeight;
    private double width;
    private double height;
    private boolean preserveRatio = true;
    private final String fileName;
    private String path = "Unknown";

    /**
     * Converts a JavaFX {@link Image} into an ImageOverlay.
     * @param image The JavaFx image.
     */
    public ImageOverlay(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.fileName = "Unknown";
    }

    /**
     * Creates an ImageOverlay generated by the application.
     * @param image The generated image.
     */
    public ImageOverlay(WritableImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.fileName = "Unknown";
    }

    /**
     * Creates an ImageOverlay with a specified loader.
     * @param imageLoader The image loader.
     * @see ImageLoader
     */
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

    /**
     * Creates an ImageOverlay given the file path from the game directory.
     * <p>
     * <pre>
     *  Below is an example directory tree.
     *     MyGame
     *       Lgame
     *         Limages
     *           Limage.png
     *         Lsubdir
     *           Limage.png
     * </pre>
     * <p>
     * The base path is assumed to be the 'images' folder. To load an image from the base path (game/images/image.png), pass "image.png".
     * <pre>
     *     {@code
     *       ImageOverlay image = new ImageOverlay("image.png"); // The file location would be /game/images/image.png
     *     }
     * </pre>
     * <p>
     * To load an image from a subdirectory you must declare the subdirectory (game/images/subdir/image.png), "subdir/image.png"
     * <pre>
     *     {@code
     *       ImageOverlay image = new ImageOverlay("subdir/image.png"); // The file location would be /game/images/subdir/image.png
     *     }
     * </pre>
     * @param imagePath The path to the file from the game directory.
     */
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

    /**
     * Creates an ImageOverlay from a specified directory and file path. The directory must be within the working directory, where the jar file is located.
     * <p>
     * <pre>
     *  Below is the directory tree for this example.
     *     MyGame
     *       Lgame
     *       Llogs
     *         Limage.png
     *       Limage.png
     *       Lrenjava
     *       Lmygame.jar
     *       Lmygame.exe
     *       Lmygame.bat
     * </pre>
     * <p>
     * The directory must be within the working directory. For example, to load an image from the root directory pass nothing.
     * <pre>
     *     {@code
     *       ImageOverlay image = new ImageOverlay("image.png"); // The file location would be, {workingdir}/image.png
     *     }
     * </pre>
     * <p>
     * The imagePath can still contain directories.
     * <pre>
     *     {@code
     *       ImageOverlay image = new ImageOverlay("", "logs/image.png"); // The file location would be {workingdir}/logs/image.png
     *
     *       ImageOverlay image = new ImageOverlay("logs", "image.png"); // The file location would be {workingdir}/logs/image.png
     *     }
     * </pre>
     * @param imagePath The path to the file from the game directory.
     * @exception ImageNotFoundException if the file could not be located. The engine will handle the throw in the logger to prevent crashing.
     */
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


    /**
     * Converts a JavaFX {@link Image} into an ImageOverlay with specified positioning.
     * @param image The JavaFx image.
     * @param x The x position of the image.
     * @param y The y position of the image.
     */
    public ImageOverlay(Image image, double x, double y) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        setX(x);
        setY(y);
        this.fileName = "Unknown";
    }

    /**
     * Creates an ImageOverlay with a specified loader and positioning.
     * @param imageLoader The image loader.
     * @param x The x position of the image.
     * @param y The y position of the image.
     * @see ImageLoader
     */
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
