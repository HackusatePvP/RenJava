package me.piitex.renjava.gui;

import javafx.animation.PauseTransition;

import javafx.application.HostServices;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.events.types.GameStartEvent;
import me.piitex.renjava.gui.splashscreen.SplashScreenView;
import me.piitex.renjava.gui.title.MainTitleScreenView;

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
        renJava.getLogger().warning("Splash screen not found.");
        renJava.getLogger().info("Creating base data...");
        renJava.createBaseData();
        renJava.getLogger().info("Creating story...");
        renJava.createStory();
        buildMainMenu();
    }

    private void buildMainMenu() {
        // Gonna put some default checks here.
        // IF there is no default font set one
        if (renJava.getDefaultFont() == null) {
            renJava.getLogger().severe("No default font set.");
            renJava.getConfiguration().setDefaultFont(new FontLoader("JandaManateeSolid.ttf", 24));
        }

        MainTitleScreenView view = renJava.buildTitleScreen();
        if (view == null) {
            RenJava.getInstance().getLogger().severe("Main title screen returned null. Make sure your RenJava class is properly setup.");
            view = new MainTitleScreenView(renJava);
        }
        view.build(stage, false);
        renJava.setMainTitleScreenView(view);

        postProcess();
    }

    private void postProcess() {
        renJava.getAddonLoader().load();
    }
}
