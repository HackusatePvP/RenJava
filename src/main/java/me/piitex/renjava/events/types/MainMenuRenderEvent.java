package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.Container;

public class MainMenuRenderEvent extends Event {
    private final Container menu;
    private boolean rightClicked = false;

    public MainMenuRenderEvent(Container menu) {
        this.menu = menu;
    }

    public MainMenuRenderEvent(Container menu, boolean rightClicked) {
        this.menu = menu;
        this.rightClicked = rightClicked;
    }

    public Container getMenu() {
        return menu;
    }

    public boolean isRightClicked() {
        return rightClicked;
    }
}
