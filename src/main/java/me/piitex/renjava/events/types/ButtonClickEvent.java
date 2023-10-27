package me.piitex.renjava.events.types;

import javafx.scene.control.Button;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class ButtonClickEvent extends Event {
    private final RenScene scene;
    private final Button button;

    public ButtonClickEvent(RenScene scene, Button button) {
        this.scene = scene;
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public RenScene getScene() {
        return scene;
    }
}