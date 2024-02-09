package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.Menu;

public class MainMenuBuildEvent extends Event {
    private final Menu menu;

    public MainMenuBuildEvent(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}
