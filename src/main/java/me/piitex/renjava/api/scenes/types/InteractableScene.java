package me.piitex.renjava.api.scenes.types;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.Overlay;
import me.piitex.renjava.gui.overlay.TextOverlay;

import java.util.Collection;
import java.util.HashSet;

/**
 * Intractable scenes are scenes that the player can interact with. Most common types of intractable scenes are maps and playable areas.
 * Playable areas are scenes in which a player can move from one location to another, typically to trigger character events.
 * An Intractable scene will not be ended automatically like image scenes. You will have to add your own code to end these scenes and trigger events with them.
 * Make use of the {@link me.piitex.renjava.events.EventListener}.
 */
public abstract class InteractableScene extends RenScene {
    private final Collection<Overlay> overlays = new HashSet<>();

    private final ImageLoader backgroundImage;

    /**
     * Creates an interact-able scene. An example of this type of scene is a map with buttons to go to places.
     * @param id Used to identify the scene.
     * @param backgroundImage The background image for the scene.
     */
    public InteractableScene(String id, ImageLoader backgroundImage) {
        super(id, backgroundImage);
        this.backgroundImage = backgroundImage;
    }

    /**
     * Adds an overlay to the scene. These are added on top of the background image.
     * @param overlay Button, Image, or Text overlay.
     */
    public void addOverlay(Overlay overlay) {
        overlays.add(overlay);
    }

    public void end() {

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

        for (Overlay overlay : overlays) {
            // Add the additional overlays to the scene
            if (overlay instanceof ImageOverlay imageOverlay) {
                ImageView imageView1 = new ImageView(imageOverlay.image());
                imageView1.setX(imageOverlay.x());
                imageView1.setY(imageOverlay.y());
                root.getChildren().add(imageView1);
            } else if (overlay instanceof TextOverlay textOverlay) {
                Text text1 = new Text(textOverlay.text());
                text1.setX(textOverlay.x());
                text1.setY(textOverlay.y());
                text1.setScaleX(textOverlay.xScale());
                text1.setScaleY(textOverlay.yScale());
                root.getChildren().add(text1);
            } else if (overlay instanceof ButtonOverlay buttonOverlay) {
                RenJava.getInstance().getLogger().info("Adding button...");
                Button button = buttonOverlay.button();
                button.setTranslateX(buttonOverlay.x());
                button.setTranslateY(buttonOverlay.y());
                root.getChildren().add(button);
            }
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
