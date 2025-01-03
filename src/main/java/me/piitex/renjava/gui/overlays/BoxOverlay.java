package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoxOverlay extends Overlay implements Region {
    private double width, height;
    private double scaleWidth, scaleHeight;
    private Color fillColor = Color.WHITE;

    public BoxOverlay(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public BoxOverlay(double width, double height, double x, double y) {
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    public BoxOverlay(double width, double height, Color color) {
        this.width = width;
        this.height = height;
        this.fillColor = color;
    }
    public BoxOverlay(double width, double height, double x, double y, Color color) {
        this.width = width;
        this.height = height;
        this.fillColor = color;
        setX(x);
        setY(y);
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public double getScaleWidth() {
        return scaleWidth;
    }

    @Override
    public void setScaleWidth(double w) {
        this.scaleWidth = w;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }

    @Override
    public Node render() {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setX(getX());
        rectangle.setY(getY());
        rectangle.setFill(getFillColor());
        setInputControls(rectangle);
        return rectangle;
    }
}
