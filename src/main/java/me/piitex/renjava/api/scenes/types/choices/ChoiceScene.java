package me.piitex.renjava.api.scenes.types.choices;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.MouseClickEvent;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.builders.ButtonBuilder;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

import java.util.LinkedHashSet;


/**
 * The ChoiceScene class represents a scene in the RenJava framework that presents the player with multiple choices.
 * Choice scenes allow players to make decisions that affect the game's progression or narrative.
 * Each choice is associated with an event handler, allowing developers to define custom actions when a choice is selected.
 *
 * <p>
 * To create a ChoiceScene, provide a unique identifier (id) and the background image for the scene.
 * The id is used to identify the scene and can be used to retrieve the scene later.
 * The background image sets the visual background for the scene.
 * </p>
 *
 * <p>
 * ChoiceScene supports up to 5 choices per scene. To add choices to the scene, use the {@link #addChoice(Choice)} method.
 * Each choice is represented by a {@link Choice} object, which contains the choice's identifier and text.
 * </p>
 *
 * <p>
 * To handle the player's choice selection, use the {@link #onChoice(ChoiceSelectInterface)} method.
 * The ChoiceSelectInterface is a functional interface that allows you to define custom actions when a choice is selected.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * ImageLoader backgroundImage = new ImageLoader("background.png");
 * ChoiceScene scene = new ChoiceScene("myScene", backgroundImage);
 * scene.addChoice(new Choice("choice1", "Option 1"));
 * scene.addChoice(new Choice("choice2", "Option 2"));
 * scene.onChoice(event -> {
 *     if (event.getChoice().getId().equals("choice1")) {
 *         // Handle choice 1 selection
 *     } else if (event.getChoice().getId().equals("choice2")) {
 *         // Handle choice 2 selection
 *     }
 * });
 * }</pre>
 * </p>
 *
 * @see RenScene
 * @see Choice
 * @see ChoiceSelectInterface
 */
public class ChoiceScene extends RenScene {
    private final ImageLoader backgroundImage;

    private ChoiceSelectInterface selectInterface;

    private final LinkedHashSet<Choice> choices = new LinkedHashSet<>();

    /**
     * Creates a ChoiceScene object with the specified identifier and background image.
     *
     * @param id               The unique identifier for the scene.
     * @param backgroundImage  The background image for the scene.
     */
    public ChoiceScene(String id, ImageLoader backgroundImage) {
        super(id, backgroundImage);
        this.backgroundImage = backgroundImage;
    }

    public ChoiceScene addChoice(Choice choice) {
        choices.add(choice);
        return this;
    }

    /**
     * Sets the event handler for the player's choice selection in the ChoiceScene.
     * The ChoiceSelectInterface is a functional interface that allows you to define custom actions when a choice is selected.
     *
     * <p>
     * Example usage:
     * <pre>{@code
     * ChoiceScene scene = new ChoiceScene("myScene", backgroundImage);
     * scene.addChoice(new Choice("choice1", "Option 1"));
     * scene.addChoice(new Choice("choice2", "Option 2"));
     * scene.onChoice(event -> {
     *     if (event.getChoice().getId().equals("choice1")) {
     *         // Handle choice 1 selection
     *     } else if (event.getChoice().getId().equals("choice2")) {
     *         // Handle choice 2 selection
     *     }
     * });
     * }</pre>
     * </p>
     *
     * @param selectInterface The ChoiceSelectInterface implementation that defines the custom actions for choice selection.
     *
     * @return The ChoiceScene object itself, allowing for method chaining.
     *
     * @see Choice
     * @see ChoiceSelectInterface
     */
    public ChoiceScene onChoice(ChoiceSelectInterface selectInterface) {
        this.selectInterface = selectInterface;
        return this;
    }

    public ChoiceSelectInterface getSelectInterface() {
        return selectInterface;
    }

    public Choice getChoice(String id) {
        return choices.stream().filter(choice -> choice.getId().equalsIgnoreCase(id)).findAny().orElse(null);
    }

    public LinkedHashSet<Choice> getChoices() {
        return choices;
    }

    @Override
    public void build(Stage stage) {
        RenJava.getInstance().setStage(stage, StageType.GAME_WINDOW);
        Group root = new Group();

        // Add background image
        Image background = null;
        try {
            background = backgroundImage.build();
        } catch (ImageNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (background != null) {
                ImageView imageView = new ImageView(background);
                root.getChildren().add(imageView);
            }
        }

        int limit = 5; // Max of 5 choices per scene.
        int i = 0; // Counter
        VBox vBox = new VBox(); // Choices will be stacked so we will make a vbox for this
        for (Choice choice : choices) {
            if (i >= limit) {
                return;
            }
            i++;
            // LinkedHashSet should loop in order from first to last entry (hopefully)
            // Make a choicebox
            ButtonBuilder builder = new ButtonBuilder(choice.getId(), new ImageLoader("gui/choice_idle_background.png"), choice.getText(), 0, 0, 1, 1);
            vBox.getChildren().add(builder.build());
        }

        int x = RenJava.getInstance().getConfiguration().getWidth();
        int y = RenJava.getInstance().getConfiguration().getHeight();

        vBox.setTranslateX(x / 2); // Floating point if you see this and know a good solution let me know!
        vBox.setTranslateY(y / 2); // Floating point
        root.getChildren().add(vBox);

        Scene scene = new Scene(root);
        scene.setOnMouseClicked(event -> {
            MouseClickEvent event1 = new MouseClickEvent(event);
            RenJava.callEvent(event1);
        });
        stage.setScene(scene);
        stage.show();
        RenJava.getInstance().getPlayer().setCurrentScene(this.getId());
        SceneStartEvent startEvent = new SceneStartEvent(this);
        RenJava.callEvent(startEvent);
    }
}
