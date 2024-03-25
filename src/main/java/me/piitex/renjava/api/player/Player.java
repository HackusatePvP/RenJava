package me.piitex.renjava.api.player;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.APINote;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.exceptions.InvalidStoryException;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;

import java.util.*;

/**
 * Class that stores player data such as game progress and what scenes they have viewed. Some of the information stored is only useful to the framework but feel free to explore.
 */
public class Player implements PersistentData {
    private boolean rightClickMenu;
    @Data private String currentScene;
    @Data private String currentStory;

    // Entry to map the story for the image
    private Map.Entry<String, ImageLoader> lastDisplayedImage;

    private boolean transitionPlaying = false;
    private boolean uiToggled = true;

    private boolean skipAutoScene = false;

    //StoryID
    @Data private LinkedHashSet<String> viewedStories = new LinkedHashSet<>(); // Ordered map of what stories the player has viewed.
    private final Map<Integer, Story> viewedStoriesIndex = new HashMap<>(); // Indexing of the viewedStories

    // StoryID, SceneID
    @Data private Map<String, String> viewedScenes = new HashMap<>();

    private final Map<String, Story> storyIdMap = new HashMap<>();

    public boolean hasSeenScene(Story story, String sceneID) {
        return viewedScenes.containsKey(story.getId()) && viewedScenes.containsValue(sceneID);
    }

    public RenScene getCurrentScene() {
        if (getCurrentStory() != null) {
            return getCurrentStory().getScene(currentScene);
        }
        return null; // No story set has the game started?
    }

    public String getCurrentSceneID() {
        return currentScene;
    }

    public String getCurrentStoryID() {
        return currentStory;
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
        viewedStories.add(currentStoryID); // When setting story update the viewedStory for rollback.
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

    public LinkedHashSet<String> getViewedStories() {
        return viewedStories;
    }

    public Map<String, String> getViewedScenes() {
        return viewedScenes;
    }

    public Map<String, Story> getStoryIdMap() {
        return storyIdMap;
    }

    public void startStory(String id) {
        if (!storyIdMap.containsKey(id)) {
            RenLogger.LOGGER.error(new InvalidStoryException(id).getMessage());
            return;
        }
        RenLogger.LOGGER.info("Starting story '" + id + "'");
        RenJava.getInstance().getPlayer().setCurrentStory(id);
        getStory(id).start();
    }

    public boolean isRightClickMenu() {
        return rightClickMenu;
    }

    public void setRightClickMenu(boolean rightClickMenu) {
        this.rightClickMenu = rightClickMenu;
    }

    public Map.Entry<String, ImageLoader> getLastDisplayedImage() {
        return lastDisplayedImage;
    }

    public void setLastDisplayedImage(Map.Entry<String, ImageLoader> lastDisplayedImage) {
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

    public boolean isTransitionPlaying() {
        return transitionPlaying;
    }

    public void setTransitionPlaying(boolean transitionPlaying) {
        this.transitionPlaying = transitionPlaying;
    }

    public void updateScene(RenScene renScene) {
        setCurrentScene(renScene.getId()); // Update the scene.
        getViewedScenes().put(renScene.getStory().getId(), renScene.getId());
        setCurrentStory(renScene.getStory());
    }
}
