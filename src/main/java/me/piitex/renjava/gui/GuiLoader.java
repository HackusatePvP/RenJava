package me.piitex.renjava.gui;

import javafx.animation.PauseTransition;

import javafx.application.HostServices;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.splashscreen.SplashScreenView;

import java.util.logging.Logger;

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
        GameStartEvent event = new GameStartEvent(renJava);
        RenJava.callEvent(event);
        buildSplashScreen();
    }

    private void buildSplashScreen() {
        renJava.getLogger().info("Creating Splash screen...");
        SplashScreenView splashScreenView = renJava.buildSplashScreen();
        if (splashScreenView == null) {
            renJava.getLogger().warning("Splash screen not found.");
            renJavaFrameworkBuild();
            return; // Don't create a splash screen if one wasn't set.
        }

        splashScreenView.build(stage);

        int seconds = splashScreenView.getDuration(); // Defaults to 3 seconds.
        PauseTransition wait = new PauseTransition(Duration.seconds(seconds));
        wait.setOnFinished(actionEvent -> {
            renJavaFrameworkBuild();
            stage.close(); // Closes stage for the splash screen (required)
        });

        wait.play();
    }

    private void renJavaFrameworkBuild() {
        renJava.getLogger().info("Creating base data...");
        renJava.createBaseData();
        renJava.getLogger().info("Creating story...");
        renJava.createStory();
        postProcess();
        buildMainMenu();
    }

    private void buildMainMenu() {
        Logger logger = renJava.getLogger();

        // Gonna put some default checks here.
        // If there is no default font set one
        if (renJava.getConfiguration().getDefaultFont() == null) {
            renJava.getLogger().severe("No default font set.");
            renJava.getConfiguration().setDefaultFont(new FontLoader("Arial", 24));
            renJava.getConfiguration().setUiFont(new FontLoader("Arial", 26));
        }

        renJava.buildStage(stage); // Builds the stage parameters (Game Window)

        Menu menu = renJava.buildTitleScreen();

        MainMenuBuildEvent event = new MainMenuBuildEvent(menu);
        RenJava.callEvent(event);

        if (menu == null) {
            logger.severe("No title screen was built. Please customize your own title screen for better user experience.");
            logger.warning("Building RenJava default title screen...");
            menu = new Menu(renJava.getConfiguration().getHeight(), renJava.getConfiguration().getWidth()).setTitle(renJava.getName() + " v" + renJava.getVersion());
            try {
                menu.setBackgroundImage(new ImageLoader("gui/main_menu.png").build());
            } catch (ImageNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.info("Rendering main menu...");
        }

        Menu sideMenu = renJava.buildSideMenu();
        SideMenuBuildEvent sideMenuBuildEvent = new SideMenuBuildEvent(sideMenu);
        RenJava.callEvent(sideMenuBuildEvent);

        menu.addMenu(sideMenu);

        MainMenDispatchEvent dispatchEvent = new MainMenDispatchEvent(menu);
        RenJava.callEvent(dispatchEvent);

        menu.render(null, null);
        MainMenuRenderEvent renderEvent = new MainMenuRenderEvent(menu);
        RenJava.callEvent(renderEvent);

        renJava.setStage(stage, StageType.MAIN_MENU);
    }

    private void postProcess() {
        renJava.getAddonLoader().load();
    }
}
