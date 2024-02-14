package me.piitex.renjava.gui.layouts.impl;

import javafx.scene.layout.HBox;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlay.Overlay;

/**
 * Groups overlays and elements horizontally.
 */
public class HorizontalLayout extends Layout {

    // Overlays are ordered based on insertion. The first overlay added is the first one to be displayed in the layout.

    public HorizontalLayout(int width, int height) {
        super(new HBox());
        setWidth(width);
        setHeight(height);
    }

    public void addOverlay(Overlay overlay) {
        getOverlays().add(overlay);
    }

}
