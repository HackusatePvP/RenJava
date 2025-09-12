package me.piitex.renjava.api.scenes;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.api.scenes.text.StringFormatter;
import me.piitex.renjava.api.scenes.transitions.types.FadingTransition;
import me.piitex.renjava.api.scenes.transitions.types.ImageFlashTransition;
import me.piitex.renjava.api.scenes.types.animation.VideoScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.api.scenes.types.*;

import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.api.stories.Story;

import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.SceneRenderEvent;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.Element;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.gui.overlays.Overlay;
import me.piitex.renjava.gui.overlays.TextFlowOverlay;
import me.piitex.renjava.gui.overlays.TextOverlay;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;


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

    private final LinkedList<Element> elements = new LinkedList<>();

    private final Collection<File> styleSheets = new HashSet<>();

    private final Window window = RenJava.getInstance().getGameWindow();

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

    public Window getWindow() {
        return window;
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
     * @deprecated Use {@link #addElement(Element)} instead for index rendering. Will be removed for the next main release.
     */
    @Deprecated
    public void addOverlay(Overlay overlay) {
        elements.add(overlay);
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public LinkedList<Element> getElements() {
        return elements;
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

    protected Container buildTextBox(Character character, String displayName, String dialogue, FontLoader font) {
        RenJavaConfiguration configuration = RenJava.getConfiguration();

        Container textboxMenu = new EmptyContainer(0, 0, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
        String characterDisplay = null;
        if (character != null) {
            if (displayName != null) {
                // Set character display
                characterDisplay = displayName;
            } else {
                characterDisplay = character.getDisplayName();
            }
        }

        if (dialogue != null && !dialogue.isEmpty()) {
            ImageLoader textbox = new ImageLoader("gui/textbox.png");

            ImageOverlay textBoxImage = new ImageOverlay(textbox, configuration.getDialogueBoxX() + configuration.getDialogueOffsetX(), configuration.getDialogueBoxY() + configuration.getDialogueOffsetY());
            textboxMenu.addElement(textBoxImage);

            LinkedList<Overlay> texts = StringFormatter.formatText(dialogue);
            TextFlowOverlay textFlowOverlay;
            if (texts.isEmpty()) {
                TextOverlay text = new TextOverlay(dialogue);
                text.setFont(RenJava.CONFIGURATION.getDialogueFont());
                textFlowOverlay = new TextFlowOverlay(text, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
            } else {
                textFlowOverlay = new TextFlowOverlay(texts, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
            }
            textFlowOverlay.setX(configuration.getTextX() + configuration.getTextOffsetX());
            textFlowOverlay.setY(configuration.getTextY() + configuration.getTextOffsetY());
            textFlowOverlay.setTextFillColor(configuration.getDialogueColor());
            textFlowOverlay.setFont(font);
            textboxMenu.addElement(textFlowOverlay);

            if (characterDisplay != null) {
                TextOverlay characterText = new TextOverlay(characterDisplay, new FontLoader(configuration.getCharacterDisplayFont(), configuration.getCharacterTextSize()),
                        configuration.getCharacterTextX() + configuration.getCharacterTextOffsetX(),
                        configuration.getCharacterTextY() + configuration.getCharacterTextOffsetY());
                characterText.setTextFill(character.getColor());
                textboxMenu.addElement(characterText);
            }
        }

        return textboxMenu;
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