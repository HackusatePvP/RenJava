package me.piitex.renjava.api.player;

import javafx.scene.image.ImageView;

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
    @Data private String currentScene;
    @Data private String currentStory;
    private ImageView lastDisplayedImage;

    private boolean uiToggled = true;

    private boolean skipAutoScene = false;

    private final LinkedHashMap<String, Story> viewedStories = new LinkedHashMap<>(); // Ordered map of what stories the player has viewed.
    private final Map<Integer, Story> viewedStoriesIndex = new HashMap<>(); // Indexing of the viewedStories

    private final Map<Map.Entry<Story, String>, RenScene> viewedScenes = new HashMap<>();

    private final Map<String, Story> storyIdMap = new HashMap<>();

    public boolean hasSeenScene(Story story, String sceneID) {
        return viewedScenes.containsValue(story.getScene(sceneID));
    }

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
        return getStory(currentStory);
    }

    public void setCurrentStory(String currentStoryID) {
        this.currentStory = currentStoryID;
        Story story = getStory(currentStoryID);
        viewedStories.put(currentStoryID, story); // When setting story update the viewedStory for rollback.
        int index = viewedStoriesIndex.size();
        viewedStoriesIndex.put(index, story);
    }

    public void setCurrentStory(Story currentStory) {
       setCurrentStory(currentStory.getId());
    }

    public void addStory(Story story) {
        storyIdMap.put(story.getId(), story);
    }

    public Story getStory(String id) {
        return storyIdMap.get(id);
    }

    @APINote(description = "There is no method to get the next Story as story routes and dictated by the choices the player makes. Meaning it's impossible to predict with accuracy where the player will go.")
    public Story getPreviousStory() {
        return viewedStoriesIndex.get(viewedStoriesIndex.size()); // Get last story
    }

    public LinkedHashMap<String, Story> getViewedStories() {
        return viewedStories;
    }

    public Map<Map.Entry<Story, String>, RenScene> getViewedScenes() {
        return viewedScenes;
    }

    public boolean isRightClickMenu() {
        return rightClickMenu;
    }

    public void setRightClickMenu(boolean rightClickMenu) {
        this.rightClickMenu = rightClickMenu;
    }

    public ImageView getLastDisplayedImage() {
        return lastDisplayedImage;
    }

    public void setLastDisplayedImage(ImageView lastDisplayedImage) {
        this.lastDisplayedImage = lastDisplayedImage;
    }

    public boolean isUiToggled() {
        return uiToggled;
    }

    public void setUiToggled(boolean uiToggled) {
        this.uiToggled = uiToggled;
    }

    public boolean isSkipAutoScene() {
        return skipAutoScene;
    }

    public void setSkipAutoScene(boolean skipAutoScene) {
        this.skipAutoScene = skipAutoScene;
    }
}
