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
import me.piitex.renjava.api.scenes.types.animation.VideoScene;
import me.piitex.renjava.api.scenes.transitions.types.FadingTransition;
import me.piitex.renjava.api.scenes.types.input.InputScene;
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
import me.piitex.renjava.tasks.Tasks;
import org.slf4j.Logger;
import java.util.Map;

public class GameFlowEventListener implements EventListener {
    private static final RenJava renJava = RenJava.getInstance();


    @Listener(priority = Priority.HIGHEST)
    public void onMouseClick(MouseClickEvent event) {
        // RenJa keeps track of current Stages and other stuff
        Window window = renJava.getGameWindow();
        StageType stageType = RenJava.PLAYER.getCurrentStageType();
        RenScene scene = RenJava.PLAYER.getCurrentScene();
        Player player = RenJava.PLAYER;
        MouseButton button = event.getEvent().getButton();
        Logger logger = RenLogger.LOGGER;


        // Only do this if it's not the title screen or any other menu screen
        boolean gameMenu = stageType == StageType.IMAGE_SCENE || stageType == StageType.INPUT_SCENE || stageType == StageType.CHOICE_SCENE || stageType == StageType.INTERACTABLE_SCENE || stageType == StageType.ANIMATION_SCENE;

        switch (button) {
            case MIDDLE -> {
                if (gameMenu) {
                    // Hide ui elements from scene
                    logger.info("Toggling scene ui");
                    player.setUiToggled(!player.isUiToggled());
                    scene.build(player.isUiToggled());
                }
            }
            case PRIMARY -> {
                // Go Forward
                playNextScene(event.getEvent().getY());
            }
            case SECONDARY -> {
                // Open Main Menu
                if (!player.isRightClickMenu() && RenJava.PLAYER.getCurrentScene() != null) {
                    logger.info("Player is not in menu, opening menu...");

                    // When opening the right-clicked menu let's see if they are playing any media
                    if (scene instanceof VideoScene videoScene) {
                       // They are playing a video. Let's stop the video.
                        if (RenJava.PLAYER.getCurrentMedia() != null) {
                            RenJava.PLAYER.getCurrentMedia().stop();
                        }
                    }

                    Container menu = renJava.getMainMenu().mainMenu(true);
                    menu.addContainers(renJava.getMainMenu().sideMenu(true));

                    MainMenuBuildEvent buildEvent = new MainMenuBuildEvent(menu);
                    RenJava.callEvent(buildEvent);

                    // Clear current window
                    window.clearContainers();

                    window.addContainer(menu);

                    // Set flag before rendering. Important for engine checks.
                    player.setRightClickMenu(true);

                    window.render();

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
        KeyCode code = event.getEvent().getCode();
        Stage stage;
        if (code == KeyCode.F11) {
            SettingsProperties properties = renJava.getSettings();
            if (properties.isFullscreen()) {
                properties.setFullscreen(false);
                stage = renJava.getGameWindow().getStage();
                stage.setFullScreen(false);
            } else {
                properties.setFullscreen(true);
                stage = renJava.getGameWindow().getStage();
                stage.setFullScreen(true);
            }
        }

        if (code == KeyCode.SPACE || code == KeyCode.ENTER) {
            playNextScene();
        }

        // Key-held is a little weird.
        if (code == KeyCode.CONTROL) {
            // Check to see if they viewed the scene first.
            RenScene currentScene = RenJava.PLAYER.getCurrentScene();
            if (currentScene != null && !inputScene(currentScene)) {
                RenScene nextScene = currentScene.getStory().getNextScene(currentScene.getId());
                if (nextScene != null && (RenJava.PLAYER.hasSeenScene(nextScene.getStory(), nextScene.getId()) || renJava.getSettings().isSkipUnseenText())) {
                    Tasks.runJavaFXThread(this::playNextScene);
                } else {
                    if (nextScene == null) {
                        // If the next scene is null call the scene end event
                        SceneEndEvent endEvent = new SceneEndEvent(currentScene);
                        if (currentScene.getEndInterface() != null) {
                            currentScene.getEndInterface().onEnd(endEvent);
                        }
                        RenJava.callEvent(endEvent);
                    }
                }
            }
        }
    }

    private boolean inputScene(RenScene scene) {
        return scene instanceof InputScene || scene instanceof ChoiceScene;
    }

    @Listener
    public void onScrollInput(ScrollInputEvent event) {
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
        if (event.isCancelled()) return; // If the event is canceled, do not roll back.
        // Instead of rendering the previous scene in the story, use the Player class instead
        if (RenJava.PLAYER.getCurrentScene() != null) {
            if (event.isDisplayPreviousScene()) {
                RenScene previousScene = RenJava.PLAYER.getLastViewedScene();
                if (previousScene != null) {
                    // Call SceneRollBackEvent
                    RenScene currentScene = RenJava.PLAYER.getCurrentScene();
                    SceneRollbackEvent rollbackEvent = new SceneRollbackEvent(previousScene, currentScene);
                    RenJava.callEvent(rollbackEvent);
                    previousScene.getStory().displayScene(previousScene, true);
                }
            }
        }
    }

    @Listener
    public void onScrollDown(ScrollDownEvent event) {
        // If they scroll down it acts like skipping.
        if (event.isCancelled()) return;
        RenScene scene = RenJava.PLAYER.getCurrentScene();
        if (RenJava.PLAYER.getRolledScenes().isEmpty()) {
            RenLogger.LOGGER.warn("Rollback data is not present.");
        }
        if (scene != null) {
            // Instead of playing the next, the mouse down will return to previous scene from roll back.
            // Example: Scroll up means you are going back to the previous scene. Scroll down you go back to the scene you were at.

            Map.Entry<Integer, Map.Entry<String, String>> entry = RenJava.PLAYER.getRolledScenes().entrySet().stream().filter(integerEntryEntry -> integerEntryEntry.getValue().getKey().equalsIgnoreCase(scene.getId()) && integerEntryEntry.getValue().getValue().equalsIgnoreCase(scene.getStory().getId())).findAny().orElse(null);

            if (entry != null) {
                // Remove scene from rollback
                int index = entry.getKey();

                // Play last entry in rollback
                int lastIndex = index + 1;
                Map.Entry<String, String> lastEntry = RenJava.PLAYER.getRolledScenes().get(lastIndex);
                if (lastEntry != null) {
                    Story story = RenJava.PLAYER.getStory(lastEntry.getValue());
                    RenScene lastScene = story.getScene(lastEntry.getKey());
                    story.displayScene(lastScene, true);
                }
            }
        }
    }

    private void playNextScene() {
        playNextScene(-1);
    }

    private void playNextScene(double pressedY) {
        StageType stageType = RenJava.PLAYER.getCurrentStageType();
        RenScene scene = RenJava.PLAYER.getCurrentScene();
        Player player = RenJava.PLAYER;
        Logger logger = RenLogger.LOGGER;

        // Only do this if it's not the title screen or any other menu screen
        boolean gameMenu = stageType == StageType.IMAGE_SCENE || stageType == StageType.INPUT_SCENE || stageType == StageType.CHOICE_SCENE || stageType == StageType.INTERACTABLE_SCENE || stageType == StageType.ANIMATION_SCENE;
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

            // If the scene is a InputScene check if the click was located inside of the textbox
            if (scene instanceof InputScene) {
                double textBoxY = renJava.getConfiguration().getTextY();
                System.out.println("TextBox Y: " + textBoxY);
                if (pressedY > 0) {
                    if (pressedY > textBoxY - 200 && pressedY < textBoxY + 200) {
                        return;
                    }
                }
            }

            // Handle endScene first
            SceneEndEvent endEvent = new SceneEndEvent(scene);
            if (scene.getEndInterface() != null) {
                scene.getEndInterface().onEnd(endEvent);
            }
            RenJava.callEvent(endEvent);

            Story story = scene.getStory();
            if (story == null) {
                return;
            }

            if (scene.getIndex() == story.getLastIndex()) {
                StoryEndEvent storyEndEvent = new StoryEndEvent(story);
                RenJava.callEvent(storyEndEvent);
                return;
            }


            RenScene nextScene = null;
            if (endEvent.isAutoPlayNextScene()) {
                nextScene = story.getNextScene(scene.getId());
            }

            if (player.isTransitionPlaying()) {
                if (scene.getEndTransition() != null) {
                    scene.getEndTransition().stop();
                    if (nextScene != null) {
                        story.displayScene(nextScene, false, false);
                    }
                    player.setTransitionPlaying(false);
                    return;
                }
                if (scene.getStartTransition() != null) {
                    // Force display scene
                    scene.getStartTransition().stop();
                    story.displayScene(scene, false, false);
                    player.setTransitionPlaying(false);
                    return;
                }
            }

            Pane pane = renJava.getGameWindow().getRoot();
            if (scene.getEndTransition() != null && !player.isTransitionPlaying()) {
                // Fix pane coloring for fade transitions
                if (scene.getEndTransition() instanceof FadingTransition fadingTransition) {
                    BackgroundFill backgroundFill = new BackgroundFill(fadingTransition.getColor(), new CornerRadii(1), new Insets(0, 0, 0, 0));
                    pane.setBackground(new Background(backgroundFill));
                    pane.getScene().setFill(fadingTransition.getColor());
                    renJava.getGameWindow().getStage().getScene().setFill(fadingTransition.getColor());
                }

                // Play transition
                scene.getEndTransition().play(scene, pane);

                return; // When the transition is playing prevent insta displaying the next scene
            }

            if (nextScene != null && !player.isTransitionPlaying()) {
                story.displayScene(nextScene, false, true);
            }
        }
    }

    @Listener
    public void onEndTransitionEnd(SceneEndTransitionFinishEvent event) {
        // Called when the scenes end transition finishes playing

        RenScene scene = event.getScene();
        RenScene nextScene = scene.getStory().getNextScene(scene.getId());
        if (!event.isSkipped()) {
            if (nextScene != null) {
                scene.getStory().displayScene(nextScene);
            }
        }
    }
}