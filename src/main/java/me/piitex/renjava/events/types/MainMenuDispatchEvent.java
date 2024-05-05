package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.Menu;

public class MainMenuDispatchEvent extends Event {
    private final Menu menu;

    public MainMenuDispatchEvent(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}
