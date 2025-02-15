package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.Event;

public class TransitionStopEvent extends Event {
    private final Transitions transitions;
    private RenScene scene;

    private boolean startTransition = false;

    public TransitionStopEvent(Transitions transitions) {
        this.transitions = transitions;
    }

    public TransitionStopEvent(Transitions transitions, RenScene scene) {
        this.transitions = transitions;
        this.scene = scene;
    }

    public Transitions getTransitions() {
        return transitions;
    }

    public void setScene(RenScene scene) {
        this.scene = scene;
    }

    public RenScene getScene() {
        return scene;
    }

    public boolean isStartTransition() {
        return startTransition;
    }

    public void setStartTransition(boolean startTransition) {
        this.startTransition = startTransition;
    }
}
