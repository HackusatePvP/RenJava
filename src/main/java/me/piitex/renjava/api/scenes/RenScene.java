package me.piitex.renjava.api.scenes;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.types.animation.VideoScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.api.scenes.types.*;

import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.api.stories.Story;

import me.piitex.renjava.events.types.SceneRenderEvent;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.gui.overlays.Overlay;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;


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
 * @see AutoPlayScene
 * @see InteractableScene
 * @see VideoScene
 * @see InputScene
 * @see ChoiceScene
 */
public abstract class RenScene {
    private final String id;
    private ImageOverlay backgroundImage;
    private Story story;
    private int index;
    private SceneStartInterface startInterface;
    private SceneEndInterface endInterface;
    private SceneBuildInterface buildInterface;

    private Transitions startTransition;
    private Transitions endTransition;

    private final Collection<Overlay> additionalOverlays = new HashSet<>();

    private final Collection<File> styleSheets = new HashSet<>();

    public RenScene(String id, ImageOverlay backgroundImage) {
        this.id = id;
        this.backgroundImage = backgroundImage;
        setStory(RenJava.PLAYER.getCurrentStory()); // Update the current story.
    }

    /**
     * Creates an event handler to execute when a scene starts.
     * @param sceneInterface Code to execute.
     * @return The modified RenScene.
     */
    public RenScene onStart(SceneStartInterface sceneInterface) {
        this.startInterface = sceneInterface;
        return this;
    }

    /**
     * Create an event handler to execute when a scene ends.
     * @param endInterface Code to execute.
     * @return The modified RenScene.
     */
    public RenScene onEnd(SceneEndInterface endInterface) {
        this.endInterface = endInterface;
        return this;
    }

    /**
     * Create an event handler to execute when a menu is built not rendered.
     * @param buildInterface Code to execute.
     * @return The modified RenScene.
     */
    public RenScene onBuild(SceneBuildInterface buildInterface) {
        this.buildInterface = buildInterface;
        return this;
    }


    public RenScene setBeginningTransition(Transitions transition) {
        transition.setScene(this);
        this.startTransition = transition;
        return this;
    }

    public RenScene setEndTransition(Transitions transition) {
        transition.setScene(this);
        this.endTransition = transition;
        return this;
    }

    public Transitions getStartTransition() {
        return startTransition;
    }

    public Transitions getEndTransition() {
        return endTransition;
    }

    public String getId() {
        return id;
    }

    public ImageOverlay getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(ImageOverlay backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public SceneStartInterface getStartInterface() {
        return startInterface;
    }

    public SceneEndInterface getEndInterface() {
        return endInterface;
    }

    public SceneBuildInterface getBuildInterface() {
        return buildInterface;
    }

    public void addOverlay(Overlay overlay) {
        additionalOverlays.add(overlay);
    }

    public Collection<Overlay> getAdditionalOverlays() {
        return additionalOverlays;
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

    public abstract Container build(boolean ui);

    public void render(Window window, boolean ui) {
        render(window, ui, true);
    }

    public void render(Window window, boolean ui, boolean events) {
        Container container = build(ui);

        // Clear window
        window.clearContainers();

        window.addContainer(container);

        if (events) { // This is kind of stupid but a temporary fix for transitions.
            SceneRenderEvent renderEvent = new SceneRenderEvent(this, window.getScene(), window.getRoot());
            RenJava.callEvent(renderEvent);
        }

        window.render();
    }

    public abstract StageType getStageType();

    public void addStyleSheets(File file) {
        styleSheets.add(file);
    }

    public Collection<File> getStyleSheets() {
        return styleSheets;
    }
}