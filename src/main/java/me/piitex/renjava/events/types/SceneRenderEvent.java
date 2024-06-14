package me.piitex.renjava.events.types;

import javafx.scene.Scene;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class SceneRenderEvent extends Event {
    private final RenScene renScene;
    private final Scene scene;

    public SceneRenderEvent(RenScene renScene, Scene scene) {
        this.renScene = renScene;
        this.scene = scene;
    }

    public RenScene getRenScene() {
        return renScene;
    }

    public Scene getScene() {
        return scene;
    }
}