package me.piitex.renjava.gui.overlay;

public interface Region {
    double scaleX();

    double scaleY();

    void setScaleX(double scaleX);

    void setScaleY(double scaleY);

    double width();

    double height();

    void setWidth(double width);

    void setHeight(double height);
}
