package me.piitex.renjava.gui.overlay;

import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.overlay.events.IOverlayClick;
import me.piitex.renjava.gui.overlay.events.IOverlayClickRelease;
import me.piitex.renjava.gui.overlay.events.IOverlayHover;

public interface Overlay {
    double x();

    double y();

    void setX(double x);

    void setY(double y);

    Transitions getTransition();

    void setOnclick(IOverlayClick iOverlayClick);

    void setOnHover(IOverlayHover iOverlayHover);

    void setOnClickRelease(IOverlayClickRelease iOverlayClickRelease);

    IOverlayClick getOnClick();

    IOverlayHover getOnHover();

    IOverlayClickRelease getOnRelease();
}
