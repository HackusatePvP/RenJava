package me.piitex.renjava.gui;

import javafx.animation.PauseTransition;

import javafx.stage.Stage;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.events.types.GameStartEvent;
import me.piitex.renjava.gui.splashscreen.SplashScreenView;
import me.piitex.renjava.gui.title.MainTitleScreenView;


/**
 * Loader class for loading the GUI. Starts with the splash screen first.
 */
public class GuiLoader {
    private final Stage stage;
    private final RenJava renJava;

    public GuiLoader(Stage stage, RenJava renJava) {
        this.stage = stage;
        this.renJava = renJava;
        renJava.setStage(stage, StageType.MAIN_MENU);
        GameStartEvent event = new GameStartEvent(renJava);
        RenJava.callEvent(event);
        buildSplashScreen();
    }

    private void buildSplashScreen() {
        renJava.getLogger().info("Creating Splash screen...");
        SplashScreenView splashScreenView = renJava.buildSplashScreen();
        if (splashScreenView == null) {
            renJava.getLogger().warning("Splash screen not found.");
            return; // Don't create a splash screen if one wasn't set.
        }

        splashScreenView.build(stage);

        int seconds = 3;
        PauseTransition wait = new PauseTransition(Duration.seconds(seconds));
        wait.setOnFinished(actionEvent -> {
            buildMainMenu();
        });

        wait.play();
        renJava.getLogger().info("Creating base data...");
        renJava.createBaseData();
        renJava.createStory();
    }

    private void buildMainMenu() {
        MainTitleScreenView view = renJava.buildTitleScreen();
        if (view == null) {
            RenJava.getInstance().getLogger().severe("Main title screen returned null. Make sure your RenJava class is properly setup.");
            view = new MainTitleScreenView(renJava);
        }
        view.build(stage);
    }

    public RenJava getRenJava() {
        return renJava;
    }
}
