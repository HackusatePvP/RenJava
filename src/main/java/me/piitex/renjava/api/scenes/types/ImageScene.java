package me.piitex.renjava.api.scenes.types;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.api.scenes.transitions.types.FadingTransition;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * The ImageScene class represents an image scene in the RenJava framework.
 * It is used to display an image and text associated with a character.
 * Image scenes are typically used to present visuals and dialogue during gameplay or narrative progression.
 *
 * <p>
 * To create an image scene, instantiate the ImageScene class with the necessary parameters, such as the scene ID, character, dialogue, and background image loader.
 * The character and dialogue parameters can be set to null if they are optional and not needed for a particular image scene.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * Character myCharacter = new Character("character-1", "John", Color.BLUE);
 * ImageLoader backgroundLoader = new ImageLoader("background.png");
 * ImageScene scene = new ImageScene("scene-1", myCharacter, "This is character text!", backgroundLoader);
 * }</pre>
 * </p>
 *
 * <p>
 * Note: The ImageScene class is used to create image scenes in the RenJava framework.
 * </p>
 */
public class ImageScene extends RenScene {
    private Character character;
    private String dialogue;
    private final ImageLoader loader;

    private String characterDisplayName;

    private Transitions beginningTransition;

    private final RenJavaConfiguration configuration;

    /**
     * Creates an ImageScene object representing an image scene in the RenJava framework.
     * An image scene is used to display an image and text associated with a character.
     * Image scenes are typically used to present visuals and dialogue during gameplay or narrative progression.
     *
     * @param id        The ID used to identify the scene.
     * @param character The character who is talking. Pass null if no character is talking in the scene.
     * @param dialogue  The dialogue of the character. Pass null or an empty string if no one is talking.
     * @param loader    The background image loader for the scene.
     */
    public ImageScene(String id, @Nullable Character character, String dialogue, ImageLoader loader) {
        super(id, loader);
        this.character = character;
        this.dialogue = dialogue;
        this.loader = loader;
        if (character != null) {
            this.characterDisplayName = character.getDisplayName();
        }
        configuration = RenJava.getInstance().getConfiguration();
    }

    public ImageScene setBeginningTransition(Transitions transition) {
        this.beginningTransition = transition;
        return this;
    }

    public Character getCharacter() {
        return character;
    }

    public String getCharacterNameDisplay() {
        return characterDisplayName;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public void build(Stage stage, boolean ui) {
        Group root = new Group();
        Logger logger = RenJava.getInstance().getLogger();
        // Add background image
        Image background;
        try {
            background = loader.build();
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imageView = new ImageView(background);
        // play beginning transition for image
        if (beginningTransition != null) {
            ImageView previousView = RenJava.getInstance().getPlayer().getLastDisplayedImage();
            root.getChildren().add(previousView);
            if (beginningTransition instanceof FadingTransition fadingTransition) {
                fadingTransition.play(imageView);
            }
        }
        root.getChildren().add(imageView);
        RenJava.getInstance().getPlayer().setLastDisplayedImage(imageView);

        if (ui) {
            Text text = null;
            Text characterDisplay = null;
            if (dialogue != null && !dialogue.isEmpty()) {
                text = new Text(dialogue);
                if (getCharacterNameDisplay() != null) {
                    // Set character display
                    characterDisplay = new Text(getCharacterNameDisplay());
                } else {
                    characterDisplay = new Text(character.getDisplayName());
                }
                characterDisplay.setFill(character.getColor());
                text.setFont(new FontLoader(RenJava.getInstance().getDefaultFont().getFont(), configuration.getTextSize()).getFont());
                text.setFill(configuration.getDialogueColor());
            }
            if (text != null) {
                // Create the text box
                Image textbox = null;
                try {
                    textbox = new ImageLoader("/gui/textbox.png").build();
                } catch (ImageNotFoundException e) {
                    e.printStackTrace();
                }
                if (textbox != null) {
                    imageView = new ImageView(textbox);
                    imageView.setY(configuration.getWidth() + configuration.getDialogueOffsetY());
                    root.getChildren().add(imageView);
                }

                double setX = configuration.getDialogueBoxX();
                double setY = configuration.getDialogueBoxY();

                logger.info("Text Box X: " + setX);
                logger.info("Text Box Y: " + setY);

                // Create a text flow pane for the text
                TextFlow texFlow = new TextFlow();
                texFlow.getChildren().add(text);

                // Adjust the textFlow settings.
                texFlow.setTextAlignment(TextAlignment.JUSTIFY);
                texFlow.setPrefSize(configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
                texFlow.setTranslateX(configuration.getTextX() + configuration.getTextOffsetX());
                texFlow.setTranslateY(configuration.getTextY() + configuration.getTextOffsetY());
                root.getChildren().add(texFlow);

                logger.info("Text X: " + (setX + configuration.getTextOffsetX()));
                logger.info("Text Y: " + (setY + configuration.getTextOffsetY()));

                characterDisplay.setFont(new FontLoader(RenJava.getInstance().getDefaultFont().getFont(), configuration.getCharacterTextSize()).getFont());
                characterDisplay.setX(configuration.getCharacterTextX() + configuration.getCharacterTextOffsetX());
                characterDisplay.setY(configuration.getCharacterTextY() + configuration.getCharacterTextOffsetY());
                root.getChildren().add(characterDisplay);
            }
        } else {
            RenJava.getInstance().getLogger().info("No user interface is displayed.");
        }

        //hookOverlays(root);
        setStage(stage, root, StageType.IMAGE_SCENE, false);
    }
}
