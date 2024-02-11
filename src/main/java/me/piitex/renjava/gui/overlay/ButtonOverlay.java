package me.piitex.renjava.gui.overlay;

import javafx.scene.control.Button;
import me.piitex.renjava.api.builders.ButtonBuilder;

public class ButtonOverlay implements Overlay {
    private final Button button;

    public ButtonOverlay(Button button) {
        this.button = button;
    }

    public ButtonOverlay(ButtonBuilder builder) {
        this.button = builder.build();
    }

    @Override
    public double x() {
        return button.getTranslateX();
    }

    @Override
    public double y() {
        return button.getTranslateY();
    }

    public Button build() {
        return button;
    }
}
