package me.piitex.renjava.api.stories;

import me.piitex.renjava.RenJava;

import java.util.HashMap;
import java.util.Map;

/**
 * Class dedicated to keeping track of the story. Useful for rolling back and loading.
 */
public class StoryManager {
    private final Map<String, Story> storyIdMap = new HashMap<>();

    public Story getCurrentStory() {
        return RenJava.getInstance().getPlayer().getCurrentStory();
    }

    public void setCurrentStory(Story currentStory) {
        RenJava.getInstance().getPlayer().setCurrentStory(currentStory.getId());
    }

    public void addStory(Story story) {
        storyIdMap.put(story.getId(), story);
    }

    public Story getStory(String id) {
        return storyIdMap.get(id);
    }
}