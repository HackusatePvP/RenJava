package me.piitex.renjava.events.defaults;

import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.transitions.types.FadingTransition;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.loggers.RenLogger;
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
import me.piitex.renjava.gui.StageType;
import org.slf4j.Logger;;


public class GameFlowEventListener implements EventListener {
    // Also experimental, task that runs while the ctrl key is held. Maybe I can change this to a do while or something... I'm not sure.
    private boolean skipHeld = false;

    private static final RenJava renJava = RenJava.getInstance();


    @Listener(priority = Priority.HIGHEST)
    public void onMouseClick(MouseClickEvent event) {
        // RenJa keeps track of current Stages and other stuff
        Window window = renJava.getGameWindow();
        Stage stage = renJava.getStage();
        StageType stageType = renJava.getPlayer().getCurrentStageType();
        RenScene scene = renJava.getPlayer().getCurrentScene();
        Player player = renJava.getPlayer();
        MouseButton button = event.getEvent().getButton();
        Logger logger = RenLogger.LOGGER;

        // Only do this if it's not the title screen or any other menu screen
        boolean gameMenu = stageType == StageType.IMAGE_SCENE || stageType == StageType.INPUT_SCENE || stageType == StageType.CHOICE_SCENE || stageType == StageType.INTERACTABLE_SCENE || stageType == StageType.ANIMATION_SCENE;

        switch (button) {
            case MIDDLE -> {
                if (gameMenu) {
                    // Hide ui elements from scene
                    logger.info("Toggling UI!");
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
                if (!player.isRightClickMenu() && renJava.getPlayer().getCurrentScene() != null) {
                    logger.info("Player is not in menu, opening menu...");
                    Container menu = renJava.buildMainMenu(true);
                    menu.addContainers(renJava.buildSideMenu(true));

                    MainMenuBuildEvent buildEvent = new MainMenuBuildEvent(menu);
                    RenJava.callEvent(buildEvent);

                    // Clear current window
                    window.clearContainers();

                    window.addContainer(menu);

                    window.render();

                    player.setRightClickMenu(true);

                    MainMenuRenderEvent renderEvent = new MainMenuRenderEvent(menu, true);
                    RenJava.callEvent(renderEvent);

                } else {
                    // Return to previous screen
                    RenScene renScene = player.getCurrentScene();
                    if (renScene == null) return;
                    Container menu = renScene.build(true);
                    SceneBuildEvent sceneBuildEvent = new SceneBuildEvent(renScene, menu);
                    RenJava.callEvent(sceneBuildEvent);

                    window.clearContainers();
                    window.addContainers(menu);
                    window.render();

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
            skipHeld = true;
        }
    }

    @Listener
    public void onScrollInput(ScrollInputEvent event) {
        RenLogger.LOGGER.info("Scroll Y: " + event.getScrollEvent().getDeltaY());

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
        Logger logger = RenLogger.LOGGER;
        Window window = renJava.getGameWindow();
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
                    renScene.render(window, true);
                    renJava.getPlayer().updateScene(renScene);
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
        RenLogger.LOGGER.debug("Scroll down called!");
        if (event.isCancelled()) return;
        RenLogger.LOGGER.debug("Not cancelled!");
        RenScene scene = renJava.getPlayer().getCurrentScene();
        Window window = renJava.getGameWindow();
        if (scene != null) {
            RenLogger.LOGGER.debug("Current scene not null");
            // This is off by one scene... Test the next scene?
            Story story = scene.getStory();
            RenScene nextScene = story.getNextSceneFromCurrent();

            // Stops here
            RenLogger.LOGGER.debug("Story ID: {}", story.getId());
            RenLogger.LOGGER.debug("Next Scene: {}", nextScene.getId());

            RenLogger.LOGGER.debug("Scanning for viewed scenes...");
            renJava.getPlayer().getViewedScenes().forEach((s, s2) -> {
                RenLogger.LOGGER.debug("Found: ({},{})", s, s2);
            });

            if (nextScene != null && renJava.getPlayer().hasSeenScene(story, nextScene.getId())) {
                RenLogger.LOGGER.debug("Next scene not null and player has viewed the next scene");
                // Render next scene
                renJava.getPlayer().updateScene(nextScene);
                nextScene.render(window, true);
                RenLogger.LOGGER.debug("Rendered");
            }
        }
    }

    private void playNextScene() {
        StageType stageType = renJava.getPlayer().getCurrentStageType();
        RenScene scene = renJava.getPlayer().getCurrentScene();
        Player player = renJava.getPlayer();
        Logger logger = RenLogger.LOGGER;

        // Only do this if it's not the title screen or any other menu screen
        boolean gameMenu = stageType == StageType.IMAGE_SCENE || stageType == StageType.INPUT_SCENE || stageType == StageType.CHOICE_SCENE || stageType == StageType.INTERACTABLE_SCENE || stageType == StageType.ANIMATION_SCENE;
        System.out.println("Boolean: " + gameMenu);
        if (gameMenu) {
            if (scene == null) {
                logger.error("The scene is null.");
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
                //logger.info("Calling story end event...");
                StoryEndEvent storyEndEvent = new StoryEndEvent(story);
                RenJava.callEvent(storyEndEvent);
                return;
            }

            if (endEvent.isAutoPlayNextScene()) {
                logger.info("Calling next scene...");
                // Call next if the story did not end.
                RenScene nextScene = story.getNextScene(scene.getId());

                logger.info("Transitioned Played: {}", player.isTransitionPlaying());
                if (scene.getEndTransition() != null && !player.isTransitionPlaying()) {
                    player.setTransitionPlaying(true);
                    Pane pane = renJava.getGameWindow().getRoot();
                    if (scene.getEndTransition() instanceof FadingTransition fadingTransition) {
                        BackgroundFill backgroundFill = new BackgroundFill(fadingTransition.getColor(), new CornerRadii(1), new Insets(0, 0, 0, 0));
                        pane.setBackground(new Background(backgroundFill));
                        pane.getScene().setFill(fadingTransition.getColor());
                        renJava.getStage().getScene().setFill(fadingTransition.getColor());
                    }
                    scene.getEndTransition().play(pane); // Starts transition.
                } else {
                    player.updateScene(nextScene);
                    nextScene.render(renJava.getGameWindow(), true);
                    player.setTransitionPlaying(false);
                }
            }
        }
    }
}