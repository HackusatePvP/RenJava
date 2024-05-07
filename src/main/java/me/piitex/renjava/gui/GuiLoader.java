package me.piitex.renjava.gui;

import javafx.animation.PauseTransition;

import javafx.application.HostServices;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.*;
;

/**
 * Loader class for loading the GUI. Starts with the splash screen first.
 */
public class GuiLoader {
    private Stage stage;
    private final RenJava renJava;

    public GuiLoader(Stage stage, RenJava renJava, HostServices services) {
        this.stage = stage;
        this.renJava = renJava;
        renJava.setHost(services);
        buildSplashScreen();
    }

    private void buildSplashScreen() {
        RenLogger.LOGGER.info("Creating Splash screen...");
        stage.initStyle(StageStyle.UNDECORATED);
        // Update Stage
        renJava.setStage(stage, StageType.MAIN_MENU);

        Menu menu = renJava.buildSplashScreen();
        if (menu == null) {
            RenLogger.LOGGER.warn("No splash screen was rendered..");
            renJavaFrameworkBuild();
            return; // Don't create a splash screen if one wasn't set.
        }

        menu.render();
        PauseTransition wait = new PauseTransition(Duration.seconds(3)); // TODO: 2/17/2024 Make this configurable.
        wait.setOnFinished(actionEvent -> {
            stage.close(); // Closes stage for the splash screen (required)
            renJavaFrameworkBuild();
        });

        wait.play();
    }

    private void renJavaFrameworkBuild() {
        RenLogger.LOGGER.info("Creating base data...");
        renJava.createBaseData();
        RenLogger.LOGGER.info("Creating story...");
        renJava.createStory();
        postProcess();
        buildMainMenu();
    }

    private void buildMainMenu() {
        // Gonna put some default checks here.

        RenJavaConfiguration configuration = renJava.getConfiguration();
        if (configuration.getDefaultFont() == null) {
            RenLogger.LOGGER.error("Default font not set.");
            renJava.getConfiguration().setDefaultFont(new FontLoader("Arial", 24));
        }
        if (configuration.getUiFont() == null) {
            RenLogger.LOGGER.error("UI font not set.");
            renJava.getConfiguration().setUiFont(new FontLoader("Arial", 26));
        }
        if (configuration.getCharacterDisplayFont() == null) {
            RenLogger.LOGGER.warn("Character display font not set.");
            renJava.getConfiguration().setCharacterDisplayFont(new FontLoader("Arial", 26));
        }
        if (configuration.getDialogueFont() == null) {
            RenLogger.LOGGER.warn("Dialogue font not set.");
            renJava.getConfiguration().setDialogueFont(new FontLoader("Arial", 26));
        }
        if (configuration.getChoiceButtonFont() == null) {
            RenLogger.LOGGER.warn("Choice button font not set.");
            renJava.getConfiguration().setChoiceButtonFont(new FontLoader("Arial", 28));
        }

        stage = new Stage();
        renJava.buildStage(stage); // Builds the stage parameters (Game Window)

        Menu menu = renJava.buildTitleScreen();
        MainMenuBuildEvent event = new MainMenuBuildEvent(menu);
        RenJava.callEvent(event);

        if (menu == null) {
            RenLogger.LOGGER.error("No title screen was found. Please customize your own title screen for better user experience.");
            RenLogger.LOGGER.warn("Building RenJava default title screen...");
            menu = new Menu(renJava.getConfiguration().getHeight(), renJava.getConfiguration().getWidth()).setTitle(renJava.getName() + " v" + renJava.getVersion());

            menu.setBackgroundImage(new ImageOverlay("gui/main_menu.png"));
        }

        Menu sideMenu = renJava.buildSideMenu();
        SideMenuBuildEvent sideMenuBuildEvent = new SideMenuBuildEvent(sideMenu);
        RenJava.callEvent(sideMenuBuildEvent);

        menu.addMenu(sideMenu);

        MainMenuDispatchEvent dispatchEvent = new MainMenuDispatchEvent(menu);
        RenJava.callEvent(dispatchEvent);

        menu.render();
        MainMenuRenderEvent renderEvent = new MainMenuRenderEvent(menu);
        RenJava.callEvent(renderEvent);

        renJava.setStage(stage, StageType.MAIN_MENU);
    }

    private void postProcess() {
        renJava.getAddonLoader().load();
    }
}
