package me.piitex.renjava.api.scenes.types.input;

import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

/**
 * The InputScene class represents a scene in the RenJava framework that allows the player to input text.
 * It is used to capture player input for purposes such as setting character names or other options.
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * InputScene inputScene = new InputScene("inputScene", backgroundImage);
 * inputScene.onSet(event -> {
 *     String userInput = event.getInputField().getText();
 *     // Handle the user input
 * });
 * }</pre>
 * </p>
 *
 * @see RenScene
 * @see InputSetInterface
 */
public class InputScene extends RenScene {
    private final String text;
    private final ImageLoader loader;
    private TextField inputField;
    private InputSetInterface setInterface;

    /**
     * Constructs an InputScene with the specified ID and image loader.
     *
     * @param id     The ID of the InputScene.
     * @param loader The image loader used to load the background image.
     */
    public InputScene(String id, String text, ImageLoader loader) {
        super(id, loader);
        this.text = text;
        this.loader = loader;
    }

    public TextField getInputField() {
        return inputField;
    }

    public void onSet(InputSetInterface inputSetInterface) {
        this.setInterface = inputSetInterface;
    }

    public InputSetInterface getSetInterface() {
        return setInterface;
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

        double setX = imageView.getX();
        double setY = imageView.getY();
        inputField = new TextField(text);
        inputField.setTranslateX(setX + 250);
        inputField.setTranslateY(setY + 100);
        root.getChildren().add(inputField);

        hookOverlays(root);
        setStage(stage, root, StageType.INPUT_SCENE);
    }
}
