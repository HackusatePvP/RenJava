package me.piitex.renjava.api.scenes.types;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
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
 * When building the scene, RenJava automatically handles the scene building process. You do not need to manually call the {@link #build(Stage)} method.
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

    private final ImageLoader backgroundImage;

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
    public InteractableScene(String id, ImageLoader backgroundImage) {
        super(id, backgroundImage);
        this.backgroundImage = backgroundImage;
    }

    @Override
    public void build(Stage stage) {
        Group root = new Group();

        ImageView backgroundView = null;
        try {
            backgroundView = new ImageView(backgroundImage.build());
        } catch (ImageNotFoundException e) {
            e.printStackTrace();
        }
        root.getChildren().add(backgroundView);
        hookOverlays(root);
        setStage(stage, root, StageType.INTERACTABLE_SCENE);
    }
}
