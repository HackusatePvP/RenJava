package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.Event;

public class FadingTransitionEndEvent extends Event {
    private final Transitions transition;

    public FadingTransitionEndEvent(Transitions transition) {
        this.transition = transition;
    }

    public Transitions getTransition() {
        return transition;
    }
}
