package me.piitex.renjava.gui.prompts;

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

    private Window promptWindow = new Window("", StageStyle.UNDECORATED, null, 900, 375, false);

    private double x= 900, y = 400;

    private int width, height;

    private boolean blockMainWindow = true;

    private boolean anchorToGame = true;

    private final TextFlowOverlay textFlowOverlay;

    private Container cachedContainer;


    public Prompt(String message) {
        this.message = message;

        // Pre-set the textflow so it can be modified before rendering
        this.textFlowOverlay = new TextFlowOverlay(message, RenJava.CONFIGURATION.getDefaultFont(), 850, 300);
    }

    public String getMessage() {
        return message;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public TextFlowOverlay getTextFlowOverlay() {
        return textFlowOverlay;
    }

    public boolean isBlockMainWindow() {
        return blockMainWindow;
    }

    public void setBlockMainWindow(boolean blockMainWindow) {
        this.blockMainWindow = blockMainWindow;
    }

    public boolean isAnchorToGame() {
        return anchorToGame;
    }

    public void setAnchorToGame(boolean anchorToGame) {
        this.anchorToGame = anchorToGame;
    }

    public LinkedList<Overlay> getOverlays() {
        return overlays;
    }

    public void addOverlay(Overlay overlay) {
        this.overlays.add(overlay);
    }


    public Container build() {
        if (cachedContainer == null) {
            Container container = new EmptyContainer(width, height);
            container.setOrder(DisplayOrder.HIGH);

            container.setX(x);
            container.setY(y);
            ImageOverlay background = new ImageOverlay("gui/frame.png");
            background.setOrder(DisplayOrder.LOW);
            container.addOverlay(background);

            textFlowOverlay.setTextFillColor(Color.WHITE);
            textFlowOverlay.setY(50);
            textFlowOverlay.setX(50);

            container.addOverlay(textFlowOverlay);

            container.addOverlays(overlays);

            this.cachedContainer = container;
        }

        return cachedContainer;
    }

    public void render() {
        // Prompts create a new window which blocks input on the game window.
        if (!isAnchorToGame()) {
            promptWindow.addContainers(build());

            promptWindow.getStage().initOwner(RenJava.getInstance().getGameWindow().getStage());

            if (blockMainWindow) {
                promptWindow.getStage().initModality(Modality.APPLICATION_MODAL);
            }

            handleInput();
            promptWindow.render();
        } else {
            // Add to current window
            Window window = RenJava.getInstance().getGameWindow();
            window.addContainer(build());
            window.render();
        }
    }

    public Window getPromptWindow() {
        return promptWindow;
    }

    public void setPromptWindow(Window promptWindow) {
        this.promptWindow = promptWindow;
    }

    public void closeWindow() {
        if (anchorToGame) {
            // Can throw null error.
            Window window = RenJava.getInstance().getGameWindow();
            window.removeContainer(cachedContainer);
            window.render();
        } else {
            promptWindow.close();
        }
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
