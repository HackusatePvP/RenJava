package me.piitex.renjava.gui.overlays;

/**
 * Regions are overlays that have a shape. The region is used to define that shape.
 */
public interface Region {
    double getWidth();
    double getHeight();
    void setWidth(double w);
    void setHeight(double h);
    double getScaleWidth();
    void setScaleWidth(double w);
    double getScaleHeight();
    void setScaleHeight(double h);
}
