package me.piitex.renjava.prompts;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.gui.overlays.Overlay;
import me.piitex.renjava.gui.overlays.TextFlowOverlay;

import java.util.LinkedList;

/**
 * Prompts are used to confirm user actions like exiting the game. A prompt will create a {@link Window} in the center of the screen with a message.
 * This window blocks and freezes the main window until the prompt is confirmed or denied.
 */
public class Prompt {
    private final String message;

    private final LinkedList<Overlay> overlays = new LinkedList<>();

    private final Window promptWindow = new Window("", StageStyle.UNDECORATED, null, 900, 375, false);

    private boolean blockMainWindow = true;


    public Prompt(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBlockMainWindow() {
        return blockMainWindow;
    }

    public void setBlockMainWindow(boolean blockMainWindow) {
        this.blockMainWindow = blockMainWindow;
    }

    public void addOverlay(Overlay overlay) {
        this.overlays.add(overlay);
    }


    public Container build() {
        Container container = new EmptyContainer(900, 375);
        ImageOverlay background = new ImageOverlay("gui/frame.png");
        background.setOrder(DisplayOrder.LOW);
        container.addOverlay(background);

        TextFlowOverlay textFlowOverlay = new TextFlowOverlay(message, RenJava.CONFIGURATION.getDefaultFont(), 850, 300);
        textFlowOverlay.setTextFillColor(Color.WHITE);
        textFlowOverlay.setY(50);
        textFlowOverlay.setX(50);

        container.addOverlay(textFlowOverlay);

        container.addOverlays(overlays);

        return container;
    }

    public void render() {
        // Prompts create a new window which blocks input on the game window.
        promptWindow.addContainers(build());

        if (blockMainWindow) {
            promptWindow.getStage().initModality(Modality.WINDOW_MODAL);
            promptWindow.getStage().initOwner(RenJava.getInstance().getGameWindow().getStage());
        }
        handleInput();

        promptWindow.render();
    }

    public Window getPromptWindow() {
        return promptWindow;
    }

    public void closeWindow() {
        promptWindow.close();
    }

    private void handleInput() {
        // Handle some input controls.
        // If the player presses Esc close the window
        promptWindow.getStage().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                promptWindow.close();
            }
        });
    }
}
