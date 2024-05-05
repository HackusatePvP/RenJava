package me.piitex.renjava.gui.exceptions;

import me.piitex.renjava.api.loaders.ImageLoader;

public class ImageNotFoundException extends Exception {

    public ImageNotFoundException(ImageLoader loader) {
        super("Could not find image: " + loader.getFile().getAbsolutePath());
    }
}
