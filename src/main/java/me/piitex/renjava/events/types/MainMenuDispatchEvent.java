package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.Container;

public class MainMenuDispatchEvent extends Event {
    private final Container menu;

    public MainMenuDispatchEvent(Container menu) {
        this.menu = menu;
    }

    public Container getMenu() {
        return menu;
    }
}
