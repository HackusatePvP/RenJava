package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.Container;

public class MainMenuBuildEvent extends Event {
    private final Container container;

    public MainMenuBuildEvent(Container container) {
        this.container = container;
    }

    public Container getContainer() {
        return container;
    }
}
