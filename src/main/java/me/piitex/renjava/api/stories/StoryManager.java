package me.piitex.renjava.api.stories;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.APIChange;
import me.piitex.renjava.api.APINote;

import java.util.HashMap;
import java.util.Map;

/**
 * Class dedicated to keeping track of the story. Useful for rolling back and loading.
 *
 * @deprecated Use {@link RenJava#getPlayer()} instead.
 */

@Deprecated
@APINote(description = "This class will be removed in the future.")
@APIChange(description = "As of version 0.0.289 this class is marked as deprecated and will be removed for future versions.", changedVersion = "0.0.289")
public class StoryManager {

    public Story getCurrentStory() {
        return RenJava.getInstance().getPlayer().getCurrentStory();
    }

    public void setCurrentStory(String id) {
        setCurrentStory(getStory(id));
    }

    public void setCurrentStory(Story currentStory) {
        RenJava.getInstance().getPlayer().setCurrentStory(currentStory.getId());
    }

    public void addStory(Story story) {
        RenJava.getInstance().getPlayer().addStory(story);
    }

    public Story getStory(String id) {
        return RenJava.getInstance().getPlayer().getStory(id);
    }
}