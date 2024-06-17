package me.piitex.renjava.api.scenes.types.choices;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.events.types.ChoiceButtonBuildEvent;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.layouts.impl.VerticalLayout;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Map;

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
    private final ImageOverlay backgroundImage;

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
    public ChoiceScene(String id, ImageOverlay backgroundImage) {
        super(id, backgroundImage);
        this.backgroundImage = backgroundImage;
    }

    /**
     * Creates a ChoiceScene object with the specified identifier.
     *
     * @param id               The unique identifier for the scene.
     */
    public ChoiceScene(String id) {
        super(id, null);
        this.backgroundImage = RenJava.getInstance().getPlayer().getLastDisplayedImage().getValue();
        setBackgroundImage(backgroundImage);
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
    public Menu build(boolean ui) {
        Menu menu = new Menu(1920.0, 1080.0, this.backgroundImage);

        if (ui) {
            VerticalLayout layout = new VerticalLayout(500, 500);
            Map.Entry<Integer, Integer> midPoint = RenJava.getInstance().getConfiguration().getMidPoint();

            layout.setX(midPoint.getKey() - 600);
            layout.setY(midPoint.getValue() - 200);
            layout.setSpacing(20.0);
            ImageLoader choiceBoxImage = new ImageLoader("gui/button/choice_idle_background.png");

            int scrollStart = 5;
            if (choices.size() > scrollStart) {
                layout.setScrollbar(true);
            }
            for (Choice choice : choices) {
                ButtonOverlay buttonOverlay;
                try {
                    buttonOverlay = new ButtonOverlay(choice.getId(), choice.getText(), Color.BLACK, RenJava.getInstance().getConfiguration().getChoiceButtonFont().getFont(), 0, 0, 1, 1);
                    buttonOverlay.setBorderColor(Color.TRANSPARENT);
                    buttonOverlay.setBackgroundColor(Color.TRANSPARENT);
                    buttonOverlay.setHover(true);
                    buttonOverlay.setTextFill(RenJava.getInstance().getConfiguration().getChoiceButtonColor());
                    buttonOverlay.build(); // Sets all the parameters for text
                    buildImageButton(buttonOverlay, choice, choiceBoxImage.build()); // Sets all the parameters for the image.
                    layout.addOverlays(buttonOverlay);
                } catch (ImageNotFoundException e) {
                    RenLogger.LOGGER.error(e.getMessage());
                }

            }
            menu.addLayout(layout);
        }
        return menu;
    }

    @Override
    public void render(Menu menu) {
        RenJava.getInstance().setStage(RenJava.getInstance().getStage(), StageType.CHOICE_SCENE);
        menu.render(this);

        SceneStartEvent event = new SceneStartEvent(this);
        RenJava.callEvent(event);
    }

    private void buildImageButton(ButtonOverlay buttonOverlay, Choice choice, Image image) {
        ChoiceButtonBuildEvent choiceButtonBuildEvent = new ChoiceButtonBuildEvent(buttonOverlay);
        Button button = choiceButtonBuildEvent.getButtonOverlay().getButton();

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        button.setId(choice.getId());
        button.setGraphic(imageView);
        button.setContentDisplay(ContentDisplay.CENTER);
        button.setMinWidth(image.getWidth());
        button.setMaxWidth(image.getWidth());
        button.setMinHeight(image.getHeight());
        button.setMaxHeight(image.getHeight());
    }
}
