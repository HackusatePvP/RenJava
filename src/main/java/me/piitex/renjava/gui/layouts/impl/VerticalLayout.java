package me.piitex.renjava.gui.layouts.impl;

import me.piitex.renjava.gui.layouts.Layout;

public class VerticalLayout extends Layout {
    public double spacing;

    public VerticalLayout(int height, int width) {
        setHeight(height);
        setWidth(width);
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }
}
