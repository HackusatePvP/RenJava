package me.piitex.renjava.events.types;

import javafx.scene.input.KeyCode;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class KeyPressEvent extends Event {
    private final KeyCode code;

    public KeyPressEvent(KeyCode code) {
        this.code = code;
    }


    public KeyCode getCode() {
        return code;
    }
}
