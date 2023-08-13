package me.piitex.renjava.events.types;

import javafx.scene.input.MouseEvent;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.events.Event;

public class MouseClickEvent extends Event {
    private final MouseEvent event;

    public MouseClickEvent(MouseEvent event) {
        RenJava.getInstance().getLogger().info("User clicked!");
        this.event = event;
    }

    public MouseEvent getEvent() {
        return event;
    }
}
