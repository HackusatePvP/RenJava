package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.Event;

public class SceneEndTransitionFinishEvent extends Event {
    private final RenScene scene;
    private final Transitions transitions;
    private boolean skipped;

    public SceneEndTransitionFinishEvent(RenScene scene, Transitions transitions) {
        this.scene = scene;
        this.transitions = transitions;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public RenScene getScene() {
        return scene;
    }

    public Transitions getTransitions() {
        return transitions;
    }
}
