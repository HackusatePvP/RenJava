package me.piitex.renjava.events.types;

import javafx.scene.Scene;
import javafx.stage.Stage;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.Menu;

public class MainMenDispatchEvent extends Event {
    private final Menu menu;

    public MainMenDispatchEvent(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}
