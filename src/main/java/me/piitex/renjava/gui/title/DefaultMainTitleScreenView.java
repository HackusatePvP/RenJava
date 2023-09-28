package me.piitex.renjava.gui.title;

import javafx.scene.paint.Color;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.ScreenView;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.builders.ButtonBuilder;
import me.piitex.renjava.gui.overlay.TextOverlay;

/**
 * This is just a default setup for the main menu. Call this if you are just testing stuff out, or you are early in development.
 */
public class DefaultMainTitleScreenView {
    private final ScreenView screenView;
    private final RenJavaConfiguration configuration = RenJava.getInstance().getConfiguration();

    public DefaultMainTitleScreenView(ScreenView screenView) {
        this.screenView = screenView;
        build();
    }

    public void build() {
        // FIXME: 8/8/2023 This currently does not utilize vboxes hboxes or other modern layout solutions. Meaning this is complete and utter trash and needs to be redone

        RenJava.getInstance().getLogger().warning("Building default layout. You should setup your own layout.");

        // Left Horizontal display bar
        ImageOverlay imageOverlay = null;
        try {
            imageOverlay = new ImageOverlay(new ImageLoader("/gui/overlay/main_menu.png").build(), 0, 0);
        } catch (ImageNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (imageOverlay == null) {
                try {
                    imageOverlay = new ImageOverlay(new ImageLoader("/gui/default.png").build(), 0,0);
                } catch (ImageNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        screenView.addOverlay(imageOverlay);

        // Bottom right text that displays the game name and version. (Similar to RenPy's default setup)
        TextOverlay textOverlay = new TextOverlay(configuration.getGameTitle(), configuration.getBottomRight().getKey() - 250, configuration.getBottomRight().getValue() - 100, 5, 5);
        screenView.addOverlay(textOverlay);

        textOverlay = new TextOverlay(RenJava.getInstance().getVersion(),configuration.getBottomRight().getKey() - 55, configuration.getBottomLeft().getValue() - 50, 2, 2);
        screenView.addOverlay(textOverlay);

        int yBuffer = 80;

        // If you are creating your own title screen you probably don't need to use scales. Scales are really only needed as they support multiple resolutions.
        // However, when you build your game you define the resolution in the configuration. If you know your resolution you can just define the exact coordinates no need to scale.
        double widthScale = configuration.getWidthScale();
        double heightScale = configuration.getHeightScale();

        // Sample logging as this is still be tested.
        RenJava.getInstance().getLogger().info("Width Scale: " + widthScale);
        RenJava.getInstance().getLogger().info("Height Scale: " + heightScale);

        // Only testing start button for now.
        ButtonBuilder startButton = new ButtonBuilder("menu-start-button", "Start", Color.WHITE, 100 * heightScale, 400 * widthScale ,2.5,2.5);
        screenView.setStartButton(startButton);

        ButtonBuilder loadButton = new ButtonBuilder("menu-load-button", "Load", Color.WHITE, startButton.getX(),startButton.getY() + yBuffer, 2.5, 2.5);
        screenView.setLoadButton(loadButton);

        ButtonBuilder preferenceButton = new ButtonBuilder("menu-preference-button", "Preferences", Color.WHITE, loadButton.getX(), loadButton.getY() + yBuffer, 2.5, 2.5);
        screenView.setOptionsButton(preferenceButton);

        ButtonBuilder aboutButton = new ButtonBuilder("menu-about-button", "About", Color.WHITE, startButton.getX() + 5, preferenceButton.getY() + yBuffer, 2.5, 2.5);
        screenView.setAboutButton(aboutButton);

        ButtonBuilder helpButton = new ButtonBuilder("menu-help-button", "Help", Color.WHITE, loadButton.getX() + 1, aboutButton.getY() + yBuffer, 2.5, 2.5);
        screenView.setHelpButton(helpButton);

        ButtonBuilder quitButton = new ButtonBuilder("menu-quit-button", "Quit", Color.WHITE, startButton.getX(), helpButton.getY() + yBuffer, 2.5, 2.5);
        screenView.setQuitButton(quitButton);
    }
}
