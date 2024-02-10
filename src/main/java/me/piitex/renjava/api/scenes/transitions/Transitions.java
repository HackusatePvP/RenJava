package me.piitex.renjava.api.scenes.transitions;

import javafx.scene.Node;

public abstract class Transitions {
    private final TransitionType transitionType;
    private double duration;

    public Transitions(TransitionType transitionType, double duration) {
        this.transitionType = transitionType;
        this.duration = duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public TransitionType getTransitionType() {
        return transitionType;
    }

    public abstract void play(Node node);

    public abstract void stop();

}
