package me.piitex.renjava.api.scenes;

import javafx.stage.Stage;
import me.piitex.renjava.api.scenes.types.ImageScene;
import me.piitex.renjava.api.scenes.types.InteractableScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.gui.builders.ImageLoader;

/**
 * The RenScene class represents a scene in the RenJava framework.
 * It serves as a base class for different types of scenes, such as image scenes and interactable scenes.
 * Scenes are used to display visuals and interact with the player during the gameplay or narrative progression.
 *
 * <p>
 * Note: Developers should not create custom RenScene types. RenJava is designed to work with the specific types of scenes provided by the framework.
 * </p>
 *
 * @see ImageScene
 * @see InteractableScene
 */
public abstract class RenScene {
    private final String id;
    private final ImageLoader backgroundImage;
    private Story story;
    private int index;
    private SceneStartInterface startInterface;
    private SceneEndInterface endInterface;


    public RenScene(String id, ImageLoader backgroundImage) {
        this.id = id;
        this.backgroundImage = backgroundImage;
    }

    public RenScene onStart(SceneStartInterface sceneInterface) {
        this.startInterface = sceneInterface;
        return this;
    }

    public RenScene onEnd(SceneEndInterface endInterface) {
        this.endInterface = endInterface;
        return this;
    }

    public String getId() {
        return id;
    }

    public ImageLoader getBackgroundImage() {
        return backgroundImage;
    }

    public SceneStartInterface getStartInterface() {
        return startInterface;
    }

    public SceneEndInterface getEndInterface() {
        return endInterface;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public abstract void build(Stage stage);
}
