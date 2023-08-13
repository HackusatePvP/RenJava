package me.piitex.renjava.api.stories.handler;

import me.piitex.renjava.events.types.StoryEndEvent;

public interface StoryEndInterface {

    void onStoryEnd(StoryEndEvent endEvent);
}
