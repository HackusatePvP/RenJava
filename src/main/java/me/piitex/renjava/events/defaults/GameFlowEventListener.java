package me.piitex.renjava.events.defaults;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.InteractableScene;
import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.Priority;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.tasks.KeyHeldTask;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Timer;
import java.util.logging.Logger;

import static javafx.scene.paint.Color.BLACK;

public class GameFlowEventListener implements EventListener {
    // Also experimental, task that runs while the ctrl key is held. Maybe I can change this to a do while or something... I'm not sure.
    private KeyHeldTask heldTask;

    private final Timer timer = new Timer();

    private static final RenJava renJava = RenJava.getInstance();


    @Listener(priority = Priority.HIGHEST)
    public void onMouseClick(MouseClickEvent event) {
        // RenJa keeps track of current Stages and other stuff
        Stage stage = renJava.getStage();
        StageType stageType = renJava.getStageType();
        RenScene scene = renJava.getPlayer().getCurrentScene();
        Player player = renJava.getPlayer();
        MouseButton button = event.getEvent().getButton();
        Logger logger = renJava.getLogger();

        // Only do this if it's not the title screen or any other menu screen

        boolean gameMenu = stageType == StageType.IMAGE_SCENE || stageType == StageType.INPUT_SCENE || stageType == StageType.CHOICE_SCENE || stageType == StageType.INTERACTABLE_SCENE || stageType == StageType.ANIMATION_SCENE;

        switch (button) {
            case MIDDLE -> {
                if (gameMenu) {
                    // Hide ui elements from scene
                    renJava.getLogger().info("Toggling UI!");
                    player.setUiToggled(!player.isUiToggled());
                    scene.build(player.isUiToggled());
                }
            }
            case PRIMARY -> {
                // Go Forward
                playNextScene();
            }
            case SECONDARY -> {
                logger.info("Player right clicked!");
                // Open Main Menu
                if (!player.isRightClickMenu()) {
                    logger.info("Player is not in menu, opening menu...");
                    Menu menu = renJava.buildTitleScreen();
                    menu.addMenu(renJava.buildSideMenu());

                    MainMenuBuildEvent buildEvent = new MainMenuBuildEvent(menu);
                    RenJava.callEvent(buildEvent);

                    menu.render(null, null);
                    player.setRightClickMenu(true);

                    MainMenuRenderEvent renderEvent = new MainMenuRenderEvent(menu);
                    RenJava.callEvent(renderEvent);

                } else {
                    // Return to previous screen
                    RenScene renScene = player.getCurrentScene();
                    Menu menu = renScene.build(true);
                    SceneBuildEvent sceneBuildEvent = new SceneBuildEvent(renScene, menu);
                    RenJava.callEvent(sceneBuildEvent);
                    menu.render(null, renScene);
                    player.setRightClickMenu(false);
                }
            }
        }
    }

    @Listener
    public void onKeyPress(KeyPressEvent event) {
        // Handle actions when a player presses a key.
        KeyCode code = event.getCode();
        Stage stage;
        if (code == KeyCode.F11) {
            SettingsProperties properties = renJava.getSettings();
            if (properties.isFullscreen()) {
                properties.setFullscreen(false);
                stage = renJava.getStage();
                stage.setFullScreen(false);
            } else {
                properties.setFullscreen(true);
                stage = renJava.getStage();
                stage.setFullScreen(true);
            }
        }

        if (code == KeyCode.SPACE || code == KeyCode.ENTER) {
            playNextScene();
        }

        if (code == KeyCode.CONTROL) {
            playNextScene();
        }
    }

    @Listener
    public void onScrollInput(ScrollInputEvent event) {
        renJava.getLogger().info("Scroll Y: " + event.getScrollEvent().getDeltaY());

        // If the scroll y is less than 0 they are scrolling down.
        double y = event.getScrollEvent().getDeltaY();

        if (y > 0) {
            ScrollUpEvent scrollUpEvent = new ScrollUpEvent();
            RenJava.callEvent(scrollUpEvent);
        } else {
            ScrollDownEvent scrollDownEvent = new ScrollDownEvent();
            RenJava.callEvent(scrollDownEvent);
        }

    }

    @Listener(priority = Priority.LOWEST)
    public void onScrollUp(ScrollUpEvent event) {
        Logger logger = renJava.getLogger();
        logger.info("Scroll up called!");
        if (event.isCancelled()) return; // If the event is canceled, do not roll back.
        if (renJava.getPlayer().getCurrentScene() != null) {
            if (event.isDisplayPreviousScene()) {
                Story story = renJava.getPlayer().getCurrentStory();
                RenScene renScene = story.getPreviousSceneFromCurrent();
                if (renScene == null) {
                    // log for testing
                    logger.info("Previous scene not found.");
                } else {
                    renScene.build(true);
                }
            } else {
                logger.info("Cannot display next scene...");
            }
        } else {
            logger.info("Current scene is null...");
        }
    }

    @Listener
    public void onScrollDown(ScrollDownEvent event) {
        // If they scroll down it acts like skipping.
        if (event.isCancelled()) return;
        RenScene scene = renJava.getPlayer().getCurrentScene();
        if (scene != null) {
            // This is off by one scene... Test the next scene?
            Story story = scene.getStory();
            RenScene nextScene = story.getNextSceneFromCurrent();
            if (nextScene != null && renJava.getPlayer().hasSeenScene(story, nextScene.getId())) {
                nextScene.build(true);
            }
        }
    }

    private void playNextScene() {
        StageType stageType = renJava.getStageType();
        RenScene scene = renJava.getPlayer().getCurrentScene();
        Player player = renJava.getPlayer();
        Logger logger = renJava.getLogger();

        // Only do this if it's not the title screen or any other menu screen
        boolean gameMenu = stageType == StageType.IMAGE_SCENE || stageType == StageType.INPUT_SCENE || stageType == StageType.CHOICE_SCENE || stageType == StageType.INTERACTABLE_SCENE || stageType == StageType.ANIMATION_SCENE;

        if (gameMenu) {
            if (scene == null) {
                logger.severe("The scene is null.");
                return;
            }
            // Go to the next scene map

            // Some scenes can't end with a click or space.
            if (scene instanceof InteractableScene || scene instanceof ChoiceScene) {
                return;
            }
            // Handle endScene first
            SceneEndEvent endEvent = new SceneEndEvent(scene);
            if (scene.getEndInterface() != null) {
                scene.getEndInterface().onEnd(endEvent);
            }
            RenJava.callEvent(endEvent);

            // Jump to next scene second
            Story story = scene.getStory();
            if (story == null) {
                return;
            }

            if (scene.getIndex() == story.getLastIndex()) {
                logger.info("Calling story end event...");
                StoryEndEvent storyEndEvent = new StoryEndEvent(story);
                RenJava.callEvent(storyEndEvent);
                return;
            }

            if (endEvent.isAutoPlayNextScene()) {
                logger.info("Calling next scene...");
                // Call next if the story did not end.
                RenScene nextScene = story.getNextSceneFromCurrent();
                logger.info("Expected: " + nextScene.getId() + " Current: " + story.getCurrentScene().getId());

                Menu previousMenu = Menu.getRootMenu();
                logger.info("Transitioned Played: " + player.isTransitionPlaying());
                if (scene.getEndTransition() != null && !player.isTransitionPlaying()) {
                    player.setTransitionPlaying(true);
                    Pane pane = previousMenu.getPane();
                    scene.getEndTransition().play(pane); // Starts transition.
                } else {
                    nextScene.render(nextScene.build(true));
                    player.updateScene(nextScene);
                    player.setTransitionPlaying(false);
                }
            }
        }
    }
}