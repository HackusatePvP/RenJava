package me.piitex.renjava.api.scenes.types;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.MouseClickEvent;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.builders.FontLoader;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.Overlay;
import me.piitex.renjava.gui.overlay.TextOverlay;

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
    private final Character character;
    private final String dialogue;
    private final ImageLoader loader;

    private final String characterDisplayName;

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
    public ImageScene(String id, Character character, String dialogue, ImageLoader loader) {
        super(id, loader);
        this.character = character;
        this.dialogue = dialogue;
        this.loader = loader;
        this.characterDisplayName = character.getDisplayName();
    }

    public Character getCharacter() {
        return character;
    }

    public String getCharacterNameDisplay() {
        return characterDisplayName;
    }

    @Override
    public void build(Stage stage) {
        Group root = new Group();

        // Add background image
        Image background;
        try {
            background = loader.build();
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imageView = new ImageView(background);
        root.getChildren().add(imageView);

        Text text = null;
        Text characterDisplay = null;
        if (dialogue != null && !dialogue.isEmpty()) {
            text = new Text(dialogue);
            if (getCharacterNameDisplay() != null && !getCharacterNameDisplay().isEmpty()) {
                // Set character display
                RenJava.getInstance().getLogger().info("Character Display Name Validation: " + getCharacterNameDisplay());
                characterDisplay = new Text(getCharacterNameDisplay());
            } else {
                RenJava.getInstance().getLogger().info("Character Display Name Validation: " + character.getDisplayName());
                characterDisplay = new Text(character.getDisplayName());
            }
            characterDisplay.setFill(character.getColor());
            text.setFont(new FontLoader("JandaManateeSolid.ttf", 24).getFont());
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
                imageView.setY(1080 - textbox.getHeight()); // Set the text box to the bottom
                root.getChildren().add(imageView);
            }

            // This is the size of the text box (This is set the top right corner of the image)
            double setX = imageView.getX();
            double setY = imageView.getY();
            // Create a text flow pane for the text
            TextFlow texFlow = new TextFlow();
            texFlow.setTextAlignment(TextAlignment.JUSTIFY);
            texFlow.setPrefSize(1000, 600);
            texFlow.setTranslateX(setX + 250); // Over 250 to the right
            texFlow.setTranslateY(setY + 100); // Down 100
            texFlow.getChildren().add(text); // Add the text to the textflow
            root.getChildren().add(texFlow);

            characterDisplay.setFont(new FontLoader("JandaManateeSolid.ttf", 36).getFont());
            characterDisplay.setX(setX + 200);
            characterDisplay.setY(setY + 70);

            // Set the text a little down from the top and over to the right.
            // Name of character will be top right of the text box
            root.getChildren().add(characterDisplay);
            // Add the displayName in the top
        }

        hookOverlays(root);
        setStage(stage, root, StageType.IMAGE_SCENE);
    }
}
