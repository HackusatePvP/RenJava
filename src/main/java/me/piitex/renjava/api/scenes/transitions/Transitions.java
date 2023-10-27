package me.piitex.renjava.api.scenes.transitions;

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
}
