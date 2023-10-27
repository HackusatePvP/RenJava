package me.piitex.renjava.events.defaults;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.InteractableScene;
import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.title.MainTitleScreenView;
import me.piitex.renjava.tasks.KeyHeldTask;

import java.util.AbstractMap;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GameFlowEventListener implements EventListener {
    // Also experimental, task that runs while the ctrl key is held. Maybe I can change this to a do while or something... I'm not sure.
    private KeyHeldTask heldTask;

    private final Timer timer = new Timer();


    @Listener
    public void onMouseClick(MouseClickEvent event) {
        // RenJa keeps track of current Stages and other stuff
        Stage stage = RenJava.getInstance().getStage();
        StageType stageType = RenJava.getInstance().getStageType();
        RenScene scene = RenJava.getInstance().getPlayer().getCurrentScene();
        Player player = RenJava.getInstance().getPlayer();
        MouseButton button = event.getEvent().getButton();
        Logger logger = RenJava.getInstance().getLogger();

        // Only do this if it's not the title screen or any other menu screen

        boolean gameMenu = stageType == StageType.IMAGE_SCENE || stageType == StageType.INPUT_SCENE || stageType == StageType.CHOICE_SCENE || stageType == StageType.INTERACTABLE_SCENE || stageType == StageType.ANIMATION_SCENE;

        switch (button) {
            case MIDDLE -> {
                if (gameMenu) {
                    // Hide ui elements from scene
                    RenJava.getInstance().getLogger().info("Toggling UI!");
                    logger.info("Before Ui Toggled: " + player.isUiToggled());
                    player.setUiToggled(!player.isUiToggled());
                    logger.info("After Ui Toggled: " + player.isUiToggled());
                    scene.build(stage, player.isUiToggled());

                    // Roll back.

                    /*
                    // Rollback will probably not work at this time. I have to figure out a way to play a previous story and the development of stories has just started. I need to implement more functionality before attempting this.
                    RenScene previousScene = RenJava.getInstance().getPlayer().getCurrentStory().getPreviousSceneFromCurrent();
                    if (previousScene == null) {

                        // Scan for the previous story
                        Story previousStory = RenJava.getInstance().getPlayer().getPreviousStory(); // Gets the previous story by the index of the story.
                        if (previousStory != null) {
                            previousStory.getScene(previousStory.getLastIndex()).build(stage);
                        }
                        RenJava.getInstance().getLogger().warning("Could not find rollback scene.");
                    } else {
                        previousScene.build(stage);
                    }*/
                }
            } case PRIMARY -> {
                // Go Forward
                if (gameMenu) {

                    if (scene == null) {
                        logger.severe("The scene is null.");
                        return;
                    }
                    // Go to the next scene map
                    // Play next scene in the story or call the scene end event. StoryHandlerEvent controls stories just call the end event (I think)

                    // Some scenes you don't want to end with a click or space.
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
                        logger.info("Scene has no story returning.");
                        return;
                    }

                    // Add scene to view. Complicated mapping but it shooould work
                    player.getViewedScenes().put(new AbstractMap.SimpleEntry<>(story, scene.getId()), scene);
                    if (scene.getIndex() == story.getLastIndex()) {
                        logger.info("Calling story end event...");
                        StoryEndEvent storyEndEvent = new StoryEndEvent(story);
                        RenJava.callEvent(storyEndEvent);
                        return;
                    }

                    if (endEvent.isAutoPlayNextScene()) {
                        // Call next if the story did not end.
                        RenScene nextScene = story.getNextScene(scene.getId());
                        if (nextScene != null) {
                            nextScene.build(RenJava.getInstance().getStage(), true);
                        } else {
                            logger.info("Next scene not found. Ending story...");
                        }
                    }
                }
            }
            case SECONDARY -> {
                // FIXME: 8/5/2023 Uses old methods that have since been removed.

                // Open Main Menu
                logger.info("Right click detected");
                if (!player.isRightClickMenu()) {
                    logger.info("Player is not in menu, opening menu...");
                    MainTitleScreenView screenView = RenJava.getInstance().getMainTitleScreenView();
                    screenView.build(stage, true);
                    player.setRightClickMenu(true);

                } else {
                    logger.info("Player is in right click menu return them to the previous scene.");
                    // Return to previous screen
                    logger.info("Current scene: " + player.getCurrentScene());
                    RenScene renScene = player.getCurrentScene();
                    renScene.build(stage, true);
                    player.setRightClickMenu(false);
                }
            }
        }
    }

    @Listener
    public void onKeyPress(KeyPressEvent event) {
        // Handle actions when a player presses a key.

        RenScene scene = event.getScene();
        KeyCode code = event.getCode();
        // First do skip which is ctrl
        if (!(scene instanceof InteractableScene)) {
            if (code == KeyCode.CONTROL) {
                heldTask = new KeyHeldTask(event.getScene());
                //timer.scheduleAtFixedRate(heldTask, TimeUnit.MILLISECONDS.toMillis(500L), TimeUnit.MILLISECONDS.toMillis(500L));
            }
        }

        if (code == KeyCode.ENTER || code == KeyCode.SPACE) {
            SceneEndEvent endEvent = new SceneEndEvent(scene);
            RenJava.callEvent(endEvent);
        }
    }

    @Listener
    public void onKeyRelease(KeyReleaseEvent event) {
        RenScene scene = event.getScene();
        KeyCode code = event.getCode();
        // First do skip which is ctrl
        if (!(scene instanceof InteractableScene)) {
            if (code == KeyCode.CONTROL) {
                if (heldTask != null) {
                    timer.cancel();
                }
            }
        }
    }
}
