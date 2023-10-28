package me.piitex.renjava.api.scenes.types.choices;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.ButtonBuilder;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

import java.io.File;
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

    @Override
    public StageType getStageType() {
        return StageType.CHOICE_SCENE;
    }

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
    public void build(Stage stage, boolean ui) {
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

        // FIXME: 10/26/2023 Remove limit create scroll pane
        int limit = 5; // Max of 5 choices per scene.
        int i = 0; // Counter
        VBox vBox = new VBox(); // Choices will be stacked so we will make a vbox for this
        vBox.setPrefWidth(200);
        vBox.setPrefHeight(300);
        vBox.setLayoutX(((RenJava.getInstance().getConfiguration().getWidth() - vBox.getWidth()) / 2) - 600);
        vBox.setLayoutY(((RenJava.getInstance().getConfiguration().getHeight() - vBox.getHeight()) / 2) - 200);
        vBox.setSpacing(20);
        // For performace preload image
        ImageLoader imageLoader = new ImageLoader("gui/button/choice_idle_background.png");
        Image image = null;
        try {
            image = imageLoader.build();
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Choice choice : choices) {
            if (i >= limit) {
                // TODO: 9/24/2023 Throw error in console
                return;
            }
            i++;
            ButtonBuilder builder = new ButtonBuilder(choice.getId(), choice.getText(), RenJava.getInstance().getDefaultFont().getFont(), Color.BLACK, 0, 0, 1, 1);
            Button button = builder.build();

            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);

            button.setId(choice.getId());
            button.setGraphic(imageView);
            button.setContentDisplay(ContentDisplay.CENTER);
            button.setMinWidth(image.getWidth());
            button.setMaxWidth(image.getWidth());
            button.setMinHeight(image.getHeight());
            button.setMaxHeight(image.getHeight());

            button.setOnAction(actionEvent -> {
                ButtonClickEvent event = new ButtonClickEvent(this, button);
                RenJava.callEvent(event);
            });
            vBox.getChildren().add(button);
        }
        root.getChildren().add(vBox);
        hookOverlays(root);
        addStyleSheets(new File(System.getProperty("user.dir") + "/game/css/button.css"));
        setStage(stage, root, StageType.CHOICE_SCENE, false);
    }
}
