package me.piitex.renjava.gui.overlay;

import me.piitex.renjava.api.scenes.transitions.Transitions;

public interface Overlay {

    double x();

    double y();

    Transitions getTransition();
}
