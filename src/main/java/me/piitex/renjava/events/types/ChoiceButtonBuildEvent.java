package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.overlay.ButtonOverlay;

public class ChoiceButtonBuildEvent extends Event {
    private final ButtonOverlay buttonOverlay;

    public ChoiceButtonBuildEvent(ButtonOverlay buttonOverlay) {
        this.buttonOverlay = buttonOverlay;
    }

    public ButtonOverlay getButtonOverlay() {
        return buttonOverlay;
    }
}
