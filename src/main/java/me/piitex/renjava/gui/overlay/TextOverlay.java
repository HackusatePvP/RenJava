package me.piitex.renjava.gui.overlay;

import javafx.scene.text.Text;

public record TextOverlay(Text text, double x, double y, int xScale, int yScale) implements Overlay {
}
