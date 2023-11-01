package me.piitex.renjava.api.scenes.transitions;

import javafx.scene.Node;

public abstract class Transitions {
    private int duration;

    public Transitions(int duration) {
        this.duration = duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public abstract void play(Node node);

    public abstract void stop();
}
