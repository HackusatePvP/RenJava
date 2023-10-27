package me.piitex.renjava.events.types;

import javafx.scene.Scene;
import javafx.stage.Stage;
import me.piitex.renjava.events.Event;

public class MainMenDispatchEvent extends Event {
    private final Stage stage;
    private final Scene scene;

    public MainMenDispatchEvent(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }
}
