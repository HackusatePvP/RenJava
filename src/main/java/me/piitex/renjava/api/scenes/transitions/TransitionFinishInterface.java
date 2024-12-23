package me.piitex.renjava.api.scenes.transitions;

import me.piitex.renjava.events.types.TransitionStopEvent;

public interface TransitionFinishInterface {

    void onEnd(TransitionStopEvent event);
}
