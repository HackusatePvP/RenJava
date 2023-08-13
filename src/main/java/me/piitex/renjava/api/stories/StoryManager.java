package me.piitex.renjava.api.stories;

import me.piitex.renjava.api.scenes.types.ImageScene;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class dedicated to keeping track of the story. Useful for rolling back and loading.
 */
public class StoryManager {
    private final Map<String, Story> storyIdMap = new HashMap<>();
    private final LinkedHashMap<Integer, Story> storyIndexMap = new LinkedHashMap<>(); // Not used currently still wrapping my head around it.

    private Story currentStory;

    @Deprecated private final TreeMap<Integer, ImageScene> storyMap = new TreeMap<>();


    public Story getCurrentStory() {
        return currentStory;
    }

    public void setCurrentStory(Story currentStory) {
        this.currentStory = currentStory;
    }

    public void addStory(Story story) {
        storyIdMap.put(story.getId(), story);

    }

    public Story getStory(String id) {
        return storyIdMap.get(id);
    }

    /* Deprecation methods below. These were added before the engine even had a title screen. */

    /**
     * When adding story scenes make sure they are in order. (Useful for rolling back correctly)
     * @param scene The story scene you want to add.
     * @deprecated System has been moved to {@link Story}.
     */
    @Deprecated public void addStoryScene(ImageScene scene) {
        int index = storyMap.size() + 1;
        scene.setIndex(index);
        storyMap.put(index, scene);
    }

    /**
     * Get a scene by its index.
     * @param index Integer which represents the order.
     * @return Story scene.
     * @deprecated System has been moved to {@link Story}.
     */
    @Deprecated public ImageScene getScene(int index) {
        return storyMap.get(index);
    }
}