package me.piitex.renjava.gui.layouts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import me.piitex.renjava.gui.Renderer;

public abstract class Layout extends Renderer {
    private final Pane pane;
    private double x, y;
    private Insets padding;
    private Pos alignment;

    protected Layout(Pane pane, double width, double height) {
        this.pane = pane;
        setNode(pane);
        setWidth(width);
        setHeight(height);
    }

    public Pane getPane() {
        return pane;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Pos getAlignment() {
        return alignment;
    }

    public void setAlignment(Pos alignment) {
        this.alignment = alignment;
    }

    public void setPadding(Insets padding) {
        this.padding = padding;
    }

    public Insets getPadding() {
        return padding;
    }

    public abstract Node render();
}
