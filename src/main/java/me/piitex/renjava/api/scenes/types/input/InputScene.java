package me.piitex.renjava.api.scenes.types.input;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.ImageScene;
import me.piitex.renjava.events.types.SceneStartEvent;

import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.gui.overlays.InputFieldOverlay;
import me.piitex.renjava.gui.overlays.TextFlowOverlay;
import org.jetbrains.annotations.Nullable;

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
    private String defaultInput = "";
    private final ImageOverlay loader;
    private TextField inputField;
    private InputSetInterface setInterface;

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

    public TextField getInputField() {
        return inputField;
    }

    public String getDefaultInput() {
        return defaultInput;
    }

    public void setDefaultInput(String defaultInput) {
        this.defaultInput = defaultInput;
    }

    public void onSet(InputSetInterface inputSetInterface) {
        this.setInterface = inputSetInterface;
    }

    public InputSetInterface getSetInterface() {
        return setInterface;
    }

    @Override
    public Container build(boolean ui) {
        Container menu = new EmptyContainer(0, 0,1920, 1080);

        Container imageMenu = (new ImageScene(null, null, this.text, this.loader)).build(ui);
        if (ui) {
            TextFlowOverlay textFlowOverlay;
            for (Container otherMenu : menu.getContainers()) {
                textFlowOverlay = (TextFlowOverlay) otherMenu.getOverlays().stream().filter(overlay -> overlay instanceof TextFlowOverlay).findFirst().orElse(null);
                if (textFlowOverlay != null) {
                    Text beforeText = textFlowOverlay.getTexts().getLast();
                    InputFieldOverlay inputFieldOverlay = new InputFieldOverlay(defaultInput, beforeText.getTranslateX() - 30.0, beforeText.getY() + 210.0, 400, 1920);
                    inputFieldOverlay.setOrder(DisplayOrder.HIGH);
                    otherMenu.addOverlay(inputFieldOverlay);
                }
            }
        }
        menu.addContainers(imageMenu);
        return menu;
    }

    @Override
    public void render(Window window, boolean ui) {
        Container container = build(ui);

        window.clearContainers();

        window.addContainer(container);

        window.render();

        SceneStartEvent event = new SceneStartEvent(this);
        RenJava.callEvent(event);
    }

    @Override
    public StageType getStageType() {
        return StageType.INPUT_SCENE;
    }
}
