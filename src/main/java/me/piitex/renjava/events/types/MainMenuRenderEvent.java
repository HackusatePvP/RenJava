package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.Menu;

public class MainMenuRenderEvent extends Event {
    private final Menu menu;
    private boolean rightClicked = false;

    public MainMenuRenderEvent(Menu menu) {
        this.menu = menu;
    }

    public MainMenuRenderEvent(Menu menu, boolean rightClicked) {
        this.menu = menu;
        this.rightClicked = rightClicked;
    }

    public Menu getMenu() {
        return menu;
    }

    public boolean isRightClicked() {
        return rightClicked;
    }
}
