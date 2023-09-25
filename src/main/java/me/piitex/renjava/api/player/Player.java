package me.piitex.renjava.api.player;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.APIChange;
import me.piitex.renjava.api.APINote;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class that stores player data such as game progress and what scenes they have viewed. Some of the information stored is only useful to the framework but feel free to explore.
 */
public class Player implements PersistentData {
    private boolean rightClickMenu;
    private int mostProgressedScene;
    @Data private String currentScene;
    @Data private String currentStory;

    private final LinkedHashMap<String, Story> viewedStories = new LinkedHashMap<>(); // Ordered map of what stories the player has viewed.
    private final Map<Integer, Story> viewedStoriesIndex = new HashMap<>(); // Indexing of the viewedStories

    public RenScene getCurrentScene() {
        if (getCurrentStory() != null) {
            return getCurrentStory().getScene(currentScene);
        }
        return null; // No story set has the game started?
    }

    public void setCurrentScene(String currentSceneID) {
        this.currentScene = currentSceneID;
    }

    public Story getCurrentStory() {
        return RenJava.getInstance().getStoryManager().getStory(currentStory);
    }

    public void setCurrentStory(String currentStoryID) {
        this.currentStory = currentStoryID;
        Story story = RenJava.getInstance().getStoryManager().getStory(currentStoryID);
        viewedStories.put(currentStoryID, story); // When setting story update the viewedStory for rollback.
        int index = viewedStoriesIndex.size();
        viewedStoriesIndex.put(index, story);
    }

    @APIChange(changedVersion = "0.0.153", description = "Added functionality to get the previous story the player has viewed.")
    @APINote(description = "There is no method to get the next Story as story routes and dictated by the choices the player makes. Meaning it's impossible to predict with accuracy where the player will go.")
    public Story getPreviousStory() {
        return viewedStoriesIndex.get(viewedStoriesIndex.size()); // Get last story
    }

    /**
     * @deprecated Old method that no longer works. This doesn't take into account Stories which is what scenes depend on.
     * @return null
     */
    @Deprecated
    public int getMostProgressedScene() {
        return mostProgressedScene;
    }

    public LinkedHashMap<String, Story> getViewedStories() {
        return viewedStories;
    }

    /**
     * @deprecated Old method that no longer works. This doesn't take into account Stories which is what scenes depend on.
     * @return null
     */
    @Deprecated
    public void setMostProgressedScene(int mostProgressedScene) {
        this.mostProgressedScene = mostProgressedScene;
    }

    public boolean isRightClickMenu() {
        return rightClickMenu;
    }

    public void setRightClickMenu(boolean rightClickMenu) {
        this.rightClickMenu = rightClickMenu;
    }

}
