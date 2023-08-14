package me.piitex.renjava.api.player;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;

/**
 * Class that stores player data such as game progress and what scenes they have viewed. Some of the information stored is only useful to the framework but feel free to explore.
 */
public class Player implements PersistentData {
    private boolean rightClickMenu;
    private int mostProgressedScene;
    @Data private String currentScene;
    @Data private String currentStory;

    public RenScene getCurrentScene() {
        if (getCurrentStory() != null) {
            return getCurrentStory().getScene(currentScene);
        }
        return null; // No story set?
    }

    public void setCurrentScene(String currentSceneID) {
        this.currentScene = currentSceneID;
    }

    public Story getCurrentStory() {
        return RenJava.getInstance().getStoryManager().getStory(currentStory);
    }

    public void setCurrentStory(String currentStoryID) {
        this.currentStory = currentStoryID;
    }

    public int getMostProgressedScene() {
        return mostProgressedScene;
    }

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
