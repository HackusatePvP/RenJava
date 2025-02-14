package me.piitex.renjava.api.scenes;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.transitions.types.FadingTransition;
import me.piitex.renjava.api.scenes.transitions.types.ImageFlashTransition;
import me.piitex.renjava.api.scenes.types.animation.VideoScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.api.scenes.types.*;

import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.api.stories.Story;

import me.piitex.renjava.events.types.SceneRenderEvent;
import me.piitex.renjava.events.types.SceneStartEvent;
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


    /**
     * Sets the beginning {@link Transitions} for the scene. The transition is played and handled by the engine automatically.
     *
     * @param transition The transition to be played at the beginning the of scene.
     * @return The modified RenScene.
     *
     * @see FadingTransition
     * @see ImageFlashTransition
     */
    public RenScene setBeginningTransition(Transitions transition) {
        transition.setScene(this);
        this.startTransition = transition;
        return this;
    }

    /**
     * Sets the ending transition for the scene. The transition is played and handled by the engine automatically.
     *
     * @param transition The transition to be played at the end the of scene.
     * @return The modified RenScene.
     *
     * @see FadingTransition
     * @see ImageFlashTransition
     */
    public RenScene setEndTransition(Transitions transition) {
        transition.setScene(this);
        this.endTransition = transition;
        return this;
    }

    /**
     * @return The beginning {@link Transitions}.
     */
    public Transitions getStartTransition() {
        return startTransition;
    }

    /**
     * @return The ending {@link Transitions}
     */
    public Transitions getEndTransition() {
        return endTransition;
    }

    public String getId() {
        return id;
    }

    /**
     * @return The {@link ImageOverlay} used for the background of the scene.
     */
    public ImageOverlay getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Sets the background image for the scene.
     * @param backgroundImage The {@link ImageOverlay} to be used as the background.
     */
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

    /**
     * Adds an overlay to the scene.
     * @param overlay The {@link Overlay} to be added.
     */
    public void addOverlay(Overlay overlay) {
        additionalOverlays.add(overlay);
    }

    /**
     * @return All added overlays for the scene. This excludes the mandatory ones, like the background image and text-box.
     */
    public Collection<Overlay> getAdditionalOverlays() {
        return additionalOverlays;
    }

    /**
     * @return The {@link Story} index of the scene.
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return The {@link Story} the scene is in.
     */
    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    /**
     * Assembles the scene as a {@link Container}. This does not render anything it only builds the necessary components. Call before any rendering.
     * @param ui If the ui elements should be displayed.
     * @return {@link Container} of the assembled components in the scene.
     */
    public abstract Container build(boolean ui);

    /**
     * Renders the scene to a {@link Window}.
     * @param window The {@link Window} to be rendered too.
     * @param ui If the ui elements should be displayed.
     */
    public void render(Window window, boolean ui) {
        render(window, ui, true);
    }

    /**
     * Renders the scene to a {@link Window}
     * @param window The {@link Window} to be rendered too.
     * @param ui If the ui elements should be displayed.
     * @param events If the engine events should be handled.
     */
    public void render(Window window, boolean ui, boolean events) {
        RenJava.PLAYER.updateScene(this);
        Container container = build(ui);
        // Clear window
        window.clearContainers();
        window.addContainer(container);

        if (events) {
            SceneRenderEvent renderEvent = new SceneRenderEvent(this, window.getScene(), window.getRoot());
            RenJava.getEventHandler().callEvent(renderEvent);

            SceneStartEvent startEvent = new SceneStartEvent(this);
            RenJava.getEventHandler().callEvent(startEvent);
        }

        window.render();

        if (this instanceof VideoScene videoScene) {
            videoScene.play();
        }
    }

    /**
     * @return The scene type.
     */
    public abstract StageType getStageType();

    /**
     * Add a .css style sheet to design the scene.
     * @param file The .css file.
     */
    public void addStyleSheets(File file) {
        styleSheets.add(file);
    }

    public Collection<File> getStyleSheets() {
        return styleSheets;
    }
}