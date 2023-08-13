package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class SceneEndEvent extends Event {
    private final RenScene scene;

    public RenScene getScene() {
        return scene;
    }

    public SceneEndEvent(RenScene scene) {
        this.scene = scene;
    }
}
