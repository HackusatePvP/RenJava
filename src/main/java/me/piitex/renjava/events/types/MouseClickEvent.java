package me.piitex.renjava.events.types;

import javafx.scene.input.MouseEvent;
import me.piitex.renjava.events.Event;

public class MouseClickEvent extends Event {
    private final MouseEvent event;

    public MouseClickEvent(MouseEvent event) {
        this.event = event;
    }

    public MouseEvent getEvent() {
        return event;
    }
}
