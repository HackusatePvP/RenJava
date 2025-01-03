package me.piitex.renjava.events.defaults;

import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.api.scenes.types.animation.VideoScene;
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
                    if (scene instanceof VideoScene) {
                       // They are playing a video. Let's stop the video.
                        if (RenJava.PLAYER.getCurrentMedia() != null) {
                            RenJava.PLAYER.getCurrentMedia().stop();
                        }
                    }

                    if (player.isTransitionPlaying()) {
                        player.getCurrentTransition().stop();
                    }

                    Container menu = renJava.getMainMenu().mainMenu(true);
                    menu.addContainers(renJava.getMainMenu().sideMenu(true));

                    MainMenuBuildEvent buildEvent = new MainMenuBuildEvent(menu);
                    RenJava.getEventHandler().callEvent(buildEvent);

                    // Clear current window
                    window.clearContainers();

                    window.addContainer(menu);

                    // Set flag before rendering. Important for engine checks.
                    player.setRightClickMenu(true);
                    player.setCurrentStageType(StageType.MAIN_MENU);

                    window.render();

                    MainMenuRenderEvent renderEvent = new MainMenuRenderEvent(menu, true);
                    RenJava.getEventHandler().callEvent(renderEvent);

                } else {
                    // Return to previous screen
                    RenScene renScene = player.getCurrentScene();
                    if (renScene == null) return;
                    Container menu = renScene.build(true);
                    SceneBuildEvent sceneBuildEvent = new SceneBuildEvent(renScene, menu);
                    RenJava.getEventHandler().callEvent(sceneBuildEvent);

                    window.clearContainers();
                    window.addContainers(menu);
                    window.render();

                    player.setRightClickMenu(false);
                    player.setCurrentStageType(renScene.getStageType());
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
            SettingsProperties properties = RenJava.SETTINGS;
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
                if (nextScene != null && (RenJava.PLAYER.hasSeenScene(nextScene.getStory(), nextScene.getId()) || RenJava.SETTINGS.isSkipUnseenText())) {
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
            RenJava.getEventHandler().callEvent(scrollUpEvent);
        } else {
            ScrollDownEvent scrollDownEvent = new ScrollDownEvent();
            RenJava.getEventHandler().callEvent(scrollDownEvent);
        }

    }

    @Listener(priority = Priority.LOWEST)
    public void onScrollUp(ScrollUpEvent event) {
        Logger logger = RenLogger.LOGGER;
        Window window = renJava.getGameWindow();
        if (event.isCancelled()) return; // If the event is canceled, do not roll back.
        // Instead of rendering the previous scene in the story, use the Player class instead

        if (RenJava.PLAYER.getCurrentTransition() != null && RenJava.PLAYER.getCurrentTransition().isPlaying()) {
            RenJava.PLAYER.getCurrentTransition().stop();
        }

        if (RenJava.PLAYER.getCurrentScene() != null) {
            if (event.isDisplayPreviousScene()) {
                RenScene previousScene = RenJava.PLAYER.getLastViewedScene();
                if (previousScene != null) {
                    // Call SceneRollBackEvent
                    RenScene currentScene = RenJava.PLAYER.getCurrentScene();
                    SceneRollbackEvent rollbackEvent = new SceneRollbackEvent(previousScene, currentScene);
                    RenJava.getEventHandler().callEvent(rollbackEvent);
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
        if (scene != null) {
            if (RenJava.PLAYER.getRolledScenes().isEmpty()) {
                RenLogger.LOGGER.warn("Rollback data is not present.");
            }
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
        Window window = RenJava.getInstance().getGameWindow();
        Player player = RenJava.PLAYER;
        RenScene currentScene = player.getCurrentScene();

        // Check to make sure they are in game and on a scene.
        StageType stageType = player.getCurrentStageType();
        boolean inGame = stageType == StageType.ANIMATION_SCENE || stageType == StageType.INTERACTABLE_SCENE || stageType == StageType.CHOICE_SCENE || stageType == StageType.IMAGE_SCENE || stageType == StageType.INPUT_SCENE;
        if (!inGame) return; // Do not handle if they are not on a scene.

        // Current scene may be null, especially if the player starts a new game. This function handles the rendering of the 'nextScene'
        // Check if the current scene is not null and if it has an end transition
        RenScene nextScene = null;
        Story story;

        // We need to pass some checks. First, if a transition is already playing stop it.
        Transitions transitions = player.getCurrentTransition();
        if (transitions != null && transitions.isPlaying()) {
            RenLogger.LOGGER.info("Skipping transition...");
            transitions.stop();
            return; // Don't process scene when stopping the transition.
        }

        // Next if the scene is an interactable or choice don't process next scene.
        if (currentScene instanceof InteractableScene || currentScene instanceof ChoiceScene) {
            return;
        }


        // Lastly, don't process if they click inside the text-field area.
        if (currentScene instanceof InputScene) {
            double textBoxY = RenJava.CONFIGURATION.getTextY();
            if (pressedY > 0) {
                if (pressedY > textBoxY - 200 && pressedY < textBoxY + 200) {
                    return;
                }
            }
        }

        if (currentScene != null) {
            story = currentScene.getStory();
            nextScene = story.getNextScene(currentScene.getId());

            // Check if the end transition exists.
            Transitions endTransition = currentScene.getEndTransition();
            // Check if the transition has played, if so don't play it again. The played tag can reset if the story is refreshed. Global tags could be applied but the same issue would persist with sessions instead stories.
            if (endTransition != null && !endTransition.isPlayed() && !endTransition.isPlaying()) {
                // Run the end transition
                window.handleSceneTransition(currentScene, endTransition);
                return; // Let the transition play.
            }
        } else {
            // If the current scene is null then play the first scene of the story
            story = player.getCurrentStory();
            if (story != null) {
                story.start();
                return;
            } else {
                RenLogger.LOGGER.error("No further route! Engine is unable to progress the game.");
            }
        }

        if (nextScene != null) {
            SceneEndEvent endEvent = new SceneEndEvent(currentScene);
            RenJava.callEvent(endEvent);

            if (endEvent.isAutoPlayNextScene()) {
                // First render the scene and play the starting transition if it exists.
                story.displayScene(nextScene); // This will render the scene and play the starting transition (if one exists).
            }
        } else {
            // Check if the current scene is the last scene in the story.
            if (currentScene != null) {
                SceneEndEvent endEvent = new SceneEndEvent(currentScene);

                if (currentScene.getEndInterface() != null) {
                    currentScene.getEndInterface().onEnd(endEvent);
                }

                RenJava.callEvent(endEvent);

                story = currentScene.getStory();
                if (currentScene.getIndex() == story.getLastIndex()) {
                    // Handle story end events...
                    StoryEndEvent event = new StoryEndEvent(story);
                    RenJava.callEvent(event);
                }
            }
        }

    }
}