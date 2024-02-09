package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class SceneAnimationStartEvent extends Event {
    private final RenScene scene;

    public SceneAnimationStartEvent(RenScene scene) {
        this.scene = scene;
    }

    public RenScene getScene() {
        return scene;
    }
}
