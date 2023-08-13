package me.piitex.renjava.gui.overlay;

import javafx.scene.image.Image;

public record ImageOverlay(Image image, int x, int y) implements Overlay {
}
