package me.piitex.renjava.api.scenes.types.input;

import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.logging.Logger;

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
    public InputScene(String id, @Nullable String text, ImageLoader loader) {
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
    public Menu build(boolean ui) {

        return null;
    }

    @Override
    public void render(Menu menu) {

    }

    @Override
    public StageType getStageType() {
        return StageType.INPUT_SCENE;
    }
}
