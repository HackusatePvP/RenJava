package me.piitex.renjava.events.types;

import javafx.scene.Node;
import javafx.scene.Scene;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.Event;

public class SceneRenderEvent extends Event {
    private final RenScene renScene;
    private final Scene scene;
    private final Node pane;

    public SceneRenderEvent(RenScene renScene, Scene scene, Node pane) {
        this.renScene = renScene;
        this.scene = scene;
        this.pane = pane;
    }

    public RenScene getRenScene() {
        return renScene;
    }

    public Scene getScene() {
        return scene;
    }

    public Node getPane() {
        return pane;
    }
}