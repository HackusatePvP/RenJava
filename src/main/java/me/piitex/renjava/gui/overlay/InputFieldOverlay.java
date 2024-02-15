package me.piitex.renjava.gui.overlay;

import me.piitex.renjava.api.builders.InputFieldBuilder;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class InputFieldOverlay implements Overlay {
    private final InputFieldBuilder inputFieldBuilder;
    private Transitions transitions;

    public InputFieldOverlay(InputFieldBuilder inputFieldBuilder) {
        this.inputFieldBuilder = inputFieldBuilder;
    }

    public InputFieldBuilder getInputFieldBuilder() {
        return inputFieldBuilder;
    }

    @Override
    public double x() {
        return inputFieldBuilder.getX();
    }

    @Override
    public double y() {
        return inputFieldBuilder.getY();
    }

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }
}
