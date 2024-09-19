package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class SceneRollbackEvent extends Event {
    private final RenScene toScene;
    private final RenScene fromScene;

    public SceneRollbackEvent(RenScene toScene, RenScene fromScene) {
        this.toScene = toScene;
        this.fromScene = fromScene;
    }

    public RenScene getToScene() {
        return toScene;
    }

    public RenScene getFromScene() {
        return fromScene;
    }
}
