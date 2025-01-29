package me.piitex.renjava.api.loaders;

import javafx.scene.image.*;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.APIChange;
import me.piitex.renjava.api.APINote;
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
 * Used for loading images. When loading an image insert it inside the cache.
 */
public class ImageLoader {
    private final File file;

    private static final Map<String, Image> imageCache = new LimitedHashMap<>(50);

    /**
     * Loads an image via a filename from the base directory.
     * @param name Name of the image file.
     */
    public ImageLoader(String name) {
        File directory = new File(System.getProperty("user.dir") + "/game/images/");

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

    public ImageLoader(String directory, String name) {
        File fileDirectory = new File(System.getProperty("user.dir") + "/" + directory + "/");
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

    @APIChange(description = "Images will now be added to a cache.", changedVersion = "0.1.141")
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

    @APINote(description = "If loading a file using the regular build function does not display an image trying using this function instead. Keep in mind this has limited support for file formats.")
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
