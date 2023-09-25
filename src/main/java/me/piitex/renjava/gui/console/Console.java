package me.piitex.renjava.gui.console;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class Console {

    private GameConsole console;

    public void start(Group root, Scene scene) {

        // Create the console
        console = new GameConsole();
        console.setVisible(false);

        // Register the key listener for F8 key press event
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F8) {
                console.setVisible(!console.isVisible());
            }
        });
    }
}
