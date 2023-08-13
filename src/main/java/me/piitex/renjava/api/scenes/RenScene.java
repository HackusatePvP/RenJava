package me.piitex.renjava.api.scenes;

import javafx.stage.Stage;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.gui.builders.ImageLoader;

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
