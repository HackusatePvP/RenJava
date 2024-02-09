package me.piitex.renjava.gui.layouts.impl;

import me.piitex.renjava.gui.layouts.Layout;

public class VerticalLayout extends Layout {
    public double spacing;

    public VerticalLayout(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }
}
