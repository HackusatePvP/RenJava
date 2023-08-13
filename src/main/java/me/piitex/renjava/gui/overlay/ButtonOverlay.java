package me.piitex.renjava.gui.overlay;

import javafx.scene.control.Button;

public record ButtonOverlay(Button button) implements Overlay {

    @Override
    public int x() {
        return (int) button.getTranslateX();
    }

    @Override
    public int y() {
        return (int) button.getTranslateY();
    }
}
