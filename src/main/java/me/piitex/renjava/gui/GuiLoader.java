package me.piitex.renjava.gui;

import javafx.animation.PauseTransition;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.menus.DefaultMainMenu;
import me.piitex.renjava.gui.menus.MainMenu;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.tasks.Tasks;
import me.piitex.renjava.utils.MDUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loader class for loading the GUI. Starts with the splash screen first.
 */
public class GuiLoader {
    private final Stage stage;
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
        RenJava.PLAYER.setCurrentStageType(StageType.MAIN_MENU);

        Window window = renJava.buildSplashScreen();
        if (window == null) {
            RenLogger.LOGGER.warn("No splash screen was rendered..");
            renJavaFrameworkBuild();
            buildMainMenu();
            return; // Don't create a splash screen if one wasn't set.
        }

        window.render();

        Tasks.runAsync(this::renJavaFrameworkBuild);

        PauseTransition wait = new PauseTransition(Duration.seconds(3)); // TODO: 2/17/2024 Make this configurable.
        wait.setOnFinished(actionEvent -> {
            window.close(); // Closes stage for the splash screen (required)
            buildMainMenu();
        });

        wait.play();
    }

    private void renJavaFrameworkBuild() {
        RenLogger.LOGGER.info("Creating base data...");
        renJava.createBaseData();
        RenLogger.LOGGER.info("Creating story...");
        renJava.createStory();
        postProcess();
    }

    private void buildMainMenu() {
        // Gonna put some default checks here.

        RenJavaConfiguration configuration = RenJava.CONFIGURATION;
        if (configuration.getDefaultFont() == null) {
            RenLogger.LOGGER.error("Default font not set.");
            RenJava.CONFIGURATION.setDefaultFont(new FontLoader("Arial", 24));
        }
        if (configuration.getUiFont() == null) {
            RenLogger.LOGGER.error("UI font not set.");
            RenJava.CONFIGURATION.setUiFont(new FontLoader("Arial", 26));
        }
        if (configuration.getCharacterDisplayFont() == null) {
            RenLogger.LOGGER.warn("Character display font not set.");
            RenJava.CONFIGURATION.setCharacterDisplayFont(new FontLoader("Arial", 26));
        }
        if (configuration.getDialogueFont() == null) {
            RenLogger.LOGGER.warn("Dialogue font not set.");
            RenJava.CONFIGURATION.setDialogueFont(new FontLoader("Arial", 26));
        }
        if (configuration.getChoiceButtonFont() == null) {
            RenLogger.LOGGER.warn("Choice button font not set.");
            RenJava.CONFIGURATION.setChoiceButtonFont(new FontLoader("Arial", 28));
        }

        // Preset width and height
        RenJava.CONFIGURATION.setCurrentWindowWidth(RenJava.CONFIGURATION.getWidth());
        RenJava.CONFIGURATION.setCurrentWindowHeight(RenJava.CONFIGURATION.getHeight());

        RenLogger.LOGGER.info("Rendering main menu...");
        // When building title screen create a new window and eventually store the window for easy access
        Window window = new Window(RenJava.CONFIGURATION.getGameTitle(), StageStyle.DECORATED, new ImageLoader("gui/window_icon.png"));
        // Specifically for the gameWindow it is needed to setup the shutdown events.
        window.getStage().setOnHiding(windowEvent -> {
            ShutdownEvent shutdownEvent = new ShutdownEvent();
            RenJava.callEvent(shutdownEvent);

            RenJava.ADDONLOADER.disable();

            // Transfer saves to localsaves
            File localSaves = new File(System.getenv("APPDATA") + "/RenJava/" + renJava.getID() + "/saves/");
            for (File file : renJava.getSaves()) {
                File newDirFile = new File(localSaves, file.getName());

                // If the save file already exists check to see if the saves are different. If they are different, replace.
                if (newDirFile.exists()) {
                    String localSaveChecksum = MDUtils.getFileCheckSum(newDirFile);
                    String currentSaveChecksum = MDUtils.getFileCheckSum(file);
                    RenLogger.LOGGER.debug("Local Save : {}", localSaveChecksum);
                    RenLogger.LOGGER.debug("Current Save: {}", currentSaveChecksum);
                    if (localSaveChecksum.equalsIgnoreCase(currentSaveChecksum)) continue;
                    newDirFile.delete();
                }
                try {
                    Files.copy(Path.of(file.getPath()), Path.of(newDirFile.getPath()));
                    RenLogger.LOGGER.info("Copied '{}' to local saves.", file.getName());
                } catch (IOException ignored) {
                    // If caught ignore and let the application close.
                }
            }

            Platform.exit();
            System.exit(0);
        });


        renJava.setGameWindow(window);
        // Next get the container for the main menu
//        Container menu = renJava.buildMainMenu(false);
//        MainMenuBuildEvent event = new MainMenuBuildEvent(menu);
//        RenJava.callEvent(event);

        MainMenu menu = renJava.getMainMenu();


        // Check if the menu is null
        if (menu == null) {
            RenLogger.LOGGER.error("No title screen was found. Please customize your own title screen for better user experience.");
            RenLogger.LOGGER.warn("Building RenJava default title screen...");
            menu = new DefaultMainMenu();
            renJava.setMainMenu(menu);
        }

        // Render main menu
        Container container = menu.mainMenu(false);
        MainMenuBuildEvent event = new MainMenuBuildEvent(container);
        RenJava.callEvent(event);
        window.addContainer(container);

        Container sideMenu = menu.sideMenu(false);

        window.addContainers(sideMenu);

        MainMenuDispatchEvent dispatchEvent = new MainMenuDispatchEvent(container);
        RenJava.callEvent(dispatchEvent);

        window.setMaximized(configuration.isMaximizedGameWindow());

        window.render(); // Renders the window

        MainMenuRenderEvent renderEvent = new MainMenuRenderEvent(container);
        RenJava.callEvent(renderEvent);

        RenJava.PLAYER.setCurrentStageType(StageType.MAIN_MENU);

    }


    private void postProcess() {
        RenJava.ADDONLOADER.load();
    }
}
