package me.piitex.renjava.gui.overlay;

import javafx.scene.control.Button;
import me.piitex.renjava.api.builders.ButtonBuilder;

public record ButtonOverlay(ButtonBuilder button) implements Overlay {

    @Override
    public double x() {
        return button.getX();
    }

    @Override
    public double y() {
        return button.getY();
    }

    public Button build() {
        return button.build();
    }
}
