package me.piitex.renjava.gui.overlay;

import me.piitex.renjava.api.scenes.transitions.Transitions;

public interface Overlay {

    double x();

    double y();

    void setX(double x);

    void setY(double y);

    double scaleX();

    double scaleY();

    void setScaleX(double scaleX);

    void setScaleY(double scaleY);

    double width();

    double height();

    void setWidth(double width);

    void setHeight(double height);

    Transitions getTransition();
}
