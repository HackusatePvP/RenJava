package me.piitex.renjava.api.scenes.types.input;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.ImageScene;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.InputFieldOverlay;
import me.piitex.renjava.gui.overlay.TextFlowOverlay;
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

    public void onSet(InputSetInterface inputSetInterface) {
        this.setInterface = inputSetInterface;
    }

    public InputSetInterface getSetInterface() {
        return setInterface;
    }

    @Override
    public Menu build(boolean ui) {
        Menu menu = new Menu(1920, 1080);
        Menu imageMenu = (new ImageScene(null, null, this.text, this.loader)).build(ui);
        if (ui) {
            TextFlowOverlay textFlowOverlay;
            for (Menu otherMenu : menu.getChildren()) {
                textFlowOverlay = (TextFlowOverlay) otherMenu.getOverlays().stream().filter(overlay -> overlay instanceof TextFlowOverlay).findFirst().orElse(null);
                if (textFlowOverlay != null) {
                    Text beforeText = textFlowOverlay.getTexts().getLast();
                    InputFieldOverlay inputFieldOverlay = new InputFieldOverlay(beforeText.getTranslateY() - 30.0, beforeText.getY() + 210.0, new FontLoader(RenJava.getInstance().getConfiguration().getDefaultFont().getFont(), 24.0));
                    otherMenu.addOverlay(inputFieldOverlay);
                }
            }
        }
        menu.addMenu(imageMenu);
        return menu;
    }

    @Override
    public void render(Menu menu, boolean update) {
        if (update) {
            RenJava.getInstance().setStage(RenJava.getInstance().getStage(), StageType.INPUT_SCENE);
        }
        menu.render(this); // FIXME: 12/29/2023 Render depending on if ui is toggled

        SceneStartEvent event = new SceneStartEvent(this);
        RenJava.callEvent(event);
    }

    @Override
    public StageType getStageType() {
        return StageType.INPUT_SCENE;
    }
}
