package me.piitex.renjava.gui.overlay;

import me.piitex.renjava.api.builders.TextFlowBuilder;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class TextFlowOverlay implements Overlay {
    private final TextFlowBuilder textFlowBuilder;
    private double x;
    private double y;
    private Transitions transitions;

    public TextFlowOverlay(TextFlowBuilder textFlowBuilder, double x, double y) {
        this.textFlowBuilder = textFlowBuilder;
        this.x = x;
        this.y = y;
    }

    public TextFlowBuilder getTextFlowBuilder() {
        return textFlowBuilder;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }
}
