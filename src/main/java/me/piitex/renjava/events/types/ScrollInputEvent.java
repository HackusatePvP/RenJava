package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;

// FIXME: 11/1/2023 Clean this up so its not using javafx
public class ScrollInputEvent extends Event {
    private final javafx.scene.input.ScrollEvent scrollEvent;

    public ScrollInputEvent(javafx.scene.input.ScrollEvent scrollEvent) {
        this.scrollEvent = scrollEvent;
    }

    public javafx.scene.input.ScrollEvent getScrollEvent() {
        return scrollEvent;
    }
}
