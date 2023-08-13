package me.piitex.renjava.gui.builders;

import javafx.scene.image.Image;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Used for loading images. When loading an image insert it inside the cache.
 */
public class ImageLoader {
    private final File file;

    /**
     * Loads an image via a filename from the base directory.
     * @param name Name of the image file.
     */
    public ImageLoader(String name) {
        File directory = new File(System.getProperty("user.dir") + "/game/images/");
        this.file = new File(directory, name);
    }

    public Image build() throws ImageNotFoundException {
        try {
            return new Image(new FileInputStream(file));
        } catch (FileNotFoundException ignored) {
            throw new ImageNotFoundException(this);
        }
    }

    public File getFile() {
        return file;
    }
}
