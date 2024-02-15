package me.piitex.renjava.gui.overlay;

import javafx.scene.control.Button;
import me.piitex.renjava.api.builders.ButtonBuilder;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class ButtonOverlay implements Overlay {
    private final Button button;
    private Transitions transitions;

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

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }

    public Button build() {
        return button;
    }
}
