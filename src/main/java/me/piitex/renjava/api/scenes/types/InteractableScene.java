package me.piitex.renjava.api.scenes.types;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.types.SceneBuildEvent;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.Overlay;

/**
 * The InteractableScene class represents an interactable scene in the RenJava framework.
 * Interactable scenes are scenes that allow player interaction, such as maps and playable areas.
 * These scenes typically enable players to move between locations and trigger character events.
 * Unlike image scenes, InteractableScene does not automatically end. You need to add your own code to end these scenes and trigger events.
 * To handle events, make use of the {@link me.piitex.renjava.events.EventListener} interface.
 *
 * <p>
 * InteractableScene is designed to be instantiated rather than extended. To create an interactable scene, create a new instance of the InteractableScene class.
 * You can then add overlays to the scene using the {@link #addOverlay(Overlay)} method, which allows you to display buttons, images, and text on top of the background image.
 * The {@link Overlay} interface provides the necessary methods for positioning the overlays.
 * </p>
 *
 * <p>
 * When building the scene, RenJava automatically handles the scene building process.
 * Instead, create a separate story for each InteractableScene and add the scene to the story using the {@link Story#addScene(RenScene)} method.
 * RenJava will handle the scene transitions and building based on the story progression.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * InteractableScene scene = new InteractableScene("myScene", backgroundImage);
 * scene.addOverlay(new ButtonOverlay(button));
 * scene.addOverlay(new ImageOverlay(image, x, y));
 * scene.addOverlay(new TextOverlay(text, x, y, xScale, yScale));
 *
 * Story story = new Story("myStory");
 * story.addScene(scene);
 *
 * story.onEnd(event -> {
 *    // Handle story end event.
 *    // Most cases you want to start the next story.
 *    nextStory.start();
 * });
 *
 * }</pre>
 * </p>
 *
 * @see RenScene
 * @see Overlay
 * @see EventListener
 * @see Story
 */
public class InteractableScene extends RenScene {

    private final ImageOverlay backgroundImage;

    private static final RenJava renJava = RenJava.getInstance();

    @Override
    public StageType getStageType() {
        return StageType.INTERACTABLE_SCENE;
    }

    /**
     * Creates an InteractableScene object representing an interactable scene in the RenJava framework.
     * Interactable scenes are scenes that allow player interaction, such as maps and playable areas.
     * These scenes typically enable players to move between locations and trigger character events.
     * Unlike image scenes, InteractableScene does not automatically end. You need to add your own code to end these scenes and trigger events.
     * To handle events, make use of the {@link me.piitex.renjava.events.EventListener} interface.
     *
     * <p>
     * To create an InteractableScene, provide a unique identifier (id) and the background image for the scene.
     * The id is used to identify the scene and can be used to retrieve the scene later.
     * The background image sets the visual background for the scene.
     * </p>
     *
     * <p>
     * Example usage:
     * <pre>{@code
     * ImageLoader backgroundImage = new ImageLoader("background.png");
     * InteractableScene scene = new InteractableScene("myScene", backgroundImage);
     * }</pre>
     * </p>
     *
     * @param id                The unique identifier for the scene.
     * @param backgroundImage  The background image for the scene.
     *
     * @see RenScene
     * @see Overlay
     * @see EventListener
     */
    public InteractableScene(String id, ImageOverlay backgroundImage) {
        super(id, backgroundImage);
        this.backgroundImage = backgroundImage;
    }

    @Override
    public Menu build(boolean ui) {

        Menu menu = new Menu(renJava.getConfiguration().getWidth(), renJava.getConfiguration().getHeight(), backgroundImage);

        SceneBuildEvent event = new SceneBuildEvent(this, menu);
        RenJava.callEvent(event);

        return menu;
    }

    @Override
    public void render(Menu menu) {
        menu.render(this);
        renJava.setStage(renJava.getStage(), StageType.INTERACTABLE_SCENE);
    }
}
