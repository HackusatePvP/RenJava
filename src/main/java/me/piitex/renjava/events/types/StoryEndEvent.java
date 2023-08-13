package me.piitex.renjava.events.types;

import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.Event;

public class StoryEndEvent extends Event {
    private final Story story;

    public StoryEndEvent(Story story) {
        this.story = story;
    }

    public Story getStory() {
        return story;
    }
}
