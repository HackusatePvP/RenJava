package me.piitex.renjava.events.types;

import javafx.scene.input.KeyCode;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class KeyPressEvent extends Event {
    private final RenScene scene;
    private final KeyCode code;

    public KeyPressEvent(RenScene renScene, KeyCode code) {
        this.scene = renScene;
        this.code = code;
    }

    public RenScene getScene() {
        return scene;
    }

    public KeyCode getCode() {
        return code;
    }
}
