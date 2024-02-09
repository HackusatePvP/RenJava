package me.piitex.renjava.gui.overlay;

import javafx.scene.image.Image;

public record ImageOverlay(Image image, double x, double y) implements Overlay {
}
