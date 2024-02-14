package me.piitex.renjava.gui.layouts.impl;

import javafx.scene.layout.VBox;
import me.piitex.renjava.gui.layouts.Layout;

public class VerticalLayout extends Layout {

    public VerticalLayout(int width, int height) {
        super(new VBox());
        setWidth(width);
        setHeight(height);
    }
}
