package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class SceneEndEvent extends Event {
    private final RenScene scene;
    private boolean autoPlayNextScene = true;

    public RenScene getScene() {
        return scene;
    }

    public SceneEndEvent(RenScene scene) {
        this.scene = scene;
    }

    public void setAutoPlayNextScene(boolean autoPlayNextScene) {
        this.autoPlayNextScene = autoPlayNextScene;
    }

    public boolean isAutoPlayNextScene() {
        return autoPlayNextScene;
    }
}
