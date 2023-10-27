package me.piitex.renjava.events.types;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

/**
 * Called every time a new scene is displayed.
 */
public class SceneStartEvent extends Event {
    private final RenScene renScene;

    public SceneStartEvent(RenScene renScene) {
        this.renScene = renScene;
    }

    public RenScene getScene() {
        return renScene;
    }
}
