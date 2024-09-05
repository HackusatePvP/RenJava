package me.piitex.renjava.events.types;

import javafx.scene.control.Button;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class ButtonClickEvent extends Event {
    private final Button button;

    public ButtonClickEvent(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }
}