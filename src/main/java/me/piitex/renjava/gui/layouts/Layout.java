package me.piitex.renjava.gui.layouts;

import javafx.scene.layout.Pane;
import me.piitex.renjava.gui.overlay.Overlay;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class Layout {
    private final Pane pane; // Get's the pane type for javafx
    private final LinkedHashSet<Overlay> overlays = new LinkedHashSet<>();
    private double xPosition;
    private double yPosition;
    private int width, height;
    private double spacing;

    protected Layout(Pane pane) {
        this.pane = pane;
    }

    public Pane getPane() {
        return pane;
    }

    public double getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public double getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    public Collection<Overlay> getOverlays() {
        return overlays;
    }

    public void addOverlays(Overlay... overlays) {
        this.overlays.addAll(List.of(overlays));
    }
}
