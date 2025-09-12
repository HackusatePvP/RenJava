package me.piitex.renjava.api.scenes.types.input;

import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.text.StringFormatter;
import me.piitex.renjava.configuration.RenJavaConfiguration;

import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.overlays.*;
import me.piitex.renjava.gui.overlays.events.IInputSetEvent;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

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
 * @see IInputSetEvent
 */
public class InputScene extends RenScene {
    private final String text;
    private String defaultInput = "";
    private final ImageOverlay loader;
    private FontLoader font;
    private InputFieldOverlay inputField;
    private IInputSetEvent setInterface;

    private final RenJavaConfiguration configuration = RenJava.CONFIGURATION;

    /**
     * Constructs an InputScene with the specified ID and image loader.
     *
     * @param id     The ID of the InputScene.
     * @param loader The image loader used to load the background image.
     */
    public InputScene(String id, @Nullable String text, ImageOverlay loader) {
        super(id, loader);
        this.text = text;
        this.loader = loader;
    }

    /**
     * Constructs an InputScene with the specified ID.
     *
     * @param id     The ID of the InputScene.
     */
    public InputScene(String id, @Nullable String text) {
        super(id, null);
        this.text = text;
        loader = RenJava.PLAYER.getLastDisplayedImage().getValue();
        setBackgroundImage(loader);
    }


    public FontLoader getFont() {
        return font;
    }

    public void setFont(FontLoader font) {
        this.font = font;
    }

    public InputFieldOverlay getInputField() {
        return inputField;
    }

    public String getDefaultInput() {
        return defaultInput;
    }

    public void setDefaultInput(String defaultInput) {
        this.defaultInput = defaultInput;
    }

    public void onSet(IInputSetEvent inputSetInterface) {
        this.setInterface = inputSetInterface;
    }

    public IInputSetEvent getSetInterface() {
        return setInterface;
    }

    @Override
    public Container build(boolean ui) {
        Container container = new EmptyContainer(0, 0,1920, 1080);
        container.addElement(loader);
        if (ui) {
            Container textBox = buildTextBox(null, "", text, font);
            this.inputField = new InputFieldOverlay(defaultInput, 0,0,500,0);
            TextFlowOverlay textFlowOverlay = (TextFlowOverlay) textBox.getElements().stream().filter(element -> element instanceof TextFlowOverlay).findAny().orElse(null);
            if (textFlowOverlay != null) {
                System.out.println("Adding text flow...");
                textFlowOverlay.add(inputField);
                inputField.onInputSetEvent(event -> {
                    getSetInterface().onInputSet(event);
                    RenJava.getEventHandler().callEvent(event);
                });

                if (font == null) {
                    // Default font
                    font = configuration.getDialogueFont();
                }
                inputField.setFont(font);
                textFlowOverlay.setFont(font);

            }

            container.addElement(textBox);
        }

        return container;
    }

    @Override
    public StageType getStageType() {
        return StageType.INPUT_SCENE;
    }
}
