package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.Container;

public class SceneBuildEvent extends Event {
    private final RenScene scene;
    private final Container container;

    public SceneBuildEvent(RenScene scene, Container container) {
        this.scene = scene;
        this.container = container;
    }

    public RenScene getScene() {
        return scene;
    }

    public Container getContainer() {
        return container;
    }
}
