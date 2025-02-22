package me.piitex.renjava.api.loaders;

import javafx.scene.image.*;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.exceptions.ImageNotFoundException;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.utils.LimitedHashMap;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import java.io.*;
import java.nio.IntBuffer;
import java.util.Map;

/**
 * The ImageLoader is used to load an image from a file into the runtime. Once an image is loaded it is added to a cache. The cache contains at most 50 images.
 * <p>
 * When loading an image from a file, it is recommended to place the file into `~/games/images/~.
 * <pre>
 *     {@code
 *       ImageLoader image = new ImageLoader("image.png"); // The file would be located in '~/games/images/image.png'
 *       ImageLoader image = new ImageLoader("directory/image.png"); // The file would be located in '~/games/images/directory/image.png'
 *     }
 * </pre>
 *
 * An image can be loaded from the class-path (A.K.A jar file). This only occurs when the image cannot be located from the class-path.
 * Images are extracted from the class-path in the games pre-initialization stage.
 */
public class ImageLoader {
    private final File file;

    private static final Map<String, Image> imageCache = new LimitedHashMap<>(50);

    /**
     * Loads an image via a filename from the base directory or defaults to class-path.
     * <pre>
     *     {@code
     *       ImageLoader image = new ImageLoader("image.png"); // ~/game/image.png
     *       ImageLoader image = new ImageLoader("directory/image.png"); ~/game/directory/image.png
     *     }
     * </pre>
     * @param name Name of the image file.
     */
    public ImageLoader(String name) {
        File directory = new File(RenJava.getInstance().getBaseDirectory(), "game/images/");

        File f = new File(directory, name);

        // If the file does not exist check to see if its in the class path
        if (!f.exists()) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name);
            try {
                if (inputStream != null) {
                    // This will copy the file from the jar file into the game folder.
                    // When loading from class path there is a performance impact.
                    // Copying the file prevents further performance impacts.
                    IOUtils.copy(inputStream, new FileOutputStream(f));
                    inputStream.close();

                    RenLogger.LOGGER.info("Copied '{}' to '{}'", name, f.getAbsolutePath());
                } else {
                    // Debug purposes
                    RenLogger.LOGGER.error("Could not find resource: '{}'", name);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        // Can return null. The build function can handle a null value.
        this.file = f;
    }

    /**
     * Loads an image from a specified root directory.
     * <pre>
     *     {@code
     *       ImageLoader image = new ImageLoader("C:\Test\", "image.png"); // C:\Test\image.png
     *       ImageLoader image = new ImageLoader(System.getProperty("user.dir"), "image.png"); ~/image.png
     *       ImageLoader image = new ImageLoader(System.getProperty("user.dir") + "/game/, "image.png"); ~/game/image.png
     *       ImageLoader image = new ImageLoader(System.getProperty("user.home"), "image.png"); ~%APPDATA%/Roaming/image.png
     *     }
     * </pre>
     * @param directory
     * @param name
     */
    public ImageLoader(String directory, String name) {
        File fileDirectory = new File(RenJava.getInstance().getBaseDirectory(), directory + "/");
        File f = new File(fileDirectory, name);

        // If the file does not exist check to see if its in the class path
        if (!f.exists()) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(directory + "/" + name);
            try {
                if (inputStream != null) {
                    // This will copy the file from the jar file into the game folder.
                    // When loading from class path there is a performance impact.
                    // Copying the file prevents further performance impacts.
                    IOUtils.copy(inputStream, new FileOutputStream(f));
                    inputStream.close();

                    RenLogger.LOGGER.info("Copied '{}' to '{}'", name, f.getAbsolutePath());
                } else {
                    // Debug purposes
                    RenLogger.LOGGER.error("Could not find resource: '{}'", name);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        // Can return null. The build function can handle a null value.
        this.file = f;
    }

    public Image build() throws ImageNotFoundException {
        if (imageCache.containsKey(file.getPath())) {
            return imageCache.get(file.getPath());
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = getImage(bufferedImage);
            imageCache.put(file.getPath(), image);
            return image;
        } catch (FileNotFoundException ignored) {
            // Better logging
            ImageNotFoundException exception = new ImageNotFoundException(this);
            RenLogger.LOGGER.error(exception.getMessage(), exception);
            RenJava.writeStackTrace(exception);
            throw exception;
        } catch (IOException e) {
            return buildRaw();
        }
    }

    public Image buildRaw() throws ImageNotFoundException {
        try {
            return new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            ImageNotFoundException exception = new ImageNotFoundException(this);
            RenLogger.LOGGER.error(exception.getMessage(), exception);
            RenJava.writeStackTrace(exception);
            throw exception;
        }
    }

    public File getFile() {
        return file;
    }


    // Credit: https://stackoverflow.com/questions/30970005/bufferedimage-to-javafx-image
    private Image getImage(BufferedImage img) {
        //converting to a good type, read about types here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        newImg.createGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

        //converting the BufferedImage to an IntBuffer
        int[] type_int_agrb = ((DataBufferInt) newImg.getRaster().getDataBuffer()).getData();
        IntBuffer buffer = IntBuffer.wrap(type_int_agrb);

        //converting the IntBuffer to an Image, read more about it here: https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(newImg.getWidth(), newImg.getHeight(), buffer, pixelFormat);
        return new WritableImage(pixelBuffer);
    }
}
