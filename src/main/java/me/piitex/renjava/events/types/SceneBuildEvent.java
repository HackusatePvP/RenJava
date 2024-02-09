package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.Menu;

public class SceneBuildEvent extends Event {
    private final RenScene scene;
    private final Menu menu;

    public SceneBuildEvent(RenScene scene, Menu menu) {
        this.scene = scene;
        this.menu = menu;
    }

    public RenScene getScene() {
        return scene;
    }

    public Menu getMenu() {
        return menu;
    }
}
