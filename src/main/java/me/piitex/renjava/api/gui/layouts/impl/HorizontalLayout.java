package me.piitex.renjava.api.gui.layouts.impl;

import me.piitex.renjava.api.gui.layouts.Layout;
import me.piitex.renjava.gui.overlay.Overlay;

import java.util.LinkedHashSet;

/**
 * Groups overlays and elements horizontally.
 */
public class HorizontalLayout extends Layout {
    private double spacing;

    // Overlays are ordered based on insertion. The first overlay added is the first one to be displayed in the layout.

    public HorizontalLayout(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    public void addOverlay(Overlay overlay) {
        getOverlays().add(overlay);
    }

}
