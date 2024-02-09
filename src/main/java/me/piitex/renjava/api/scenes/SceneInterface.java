package me.piitex.renjava.api.scenes;

import me.piitex.renjava.events.types.SceneEndEvent;
import me.piitex.renjava.events.types.SceneStartEvent;

public interface SceneInterface {

    void onStart(SceneStartEvent event);
    void onEnd(SceneEndEvent event);


}
