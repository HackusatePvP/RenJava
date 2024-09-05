package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.overlays.ButtonOverlay;

public class ChoiceButtonBuildEvent extends Event {
    private ButtonOverlay buttonOverlay;

    public ChoiceButtonBuildEvent(ButtonOverlay buttonOverlay) {
        this.buttonOverlay = buttonOverlay;
    }

    public ButtonOverlay getButtonOverlay() {
        return buttonOverlay;
    }

    public void setButtonOverlay(ButtonOverlay buttonOverlay) {
        this.buttonOverlay = buttonOverlay;
    }
}
