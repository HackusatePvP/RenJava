package me.piitex.renjava.events.defaults;

import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.ImageScene;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.MouseClickEvent;
import me.piitex.renjava.events.types.SceneEndEvent;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.title.MainTitleScreenView;

import java.util.logging.Logger;

public class GameFlowEventListener implements EventListener {

    @Listener
    public void onMouseClick(MouseClickEvent event) {
        // RenJa keeps track of current Stages and other stuff
        Stage stage = RenJava.getInstance().getStage();
        StageType stageType = RenJava.getInstance().getStageType();
        RenScene scene = RenJava.getInstance().getPlayer().getCurrentScene();
        Player player = RenJava.getInstance().getPlayer();
        MouseButton button = event.getEvent().getButton();

        // Only do this if its not the title screen or any other menu screen

        switch (button) {
            case MIDDLE -> {
                if (stageType == StageType.GAME_WINDOW) {
                    // Roll back.

                    // Rollback will probably not work at this time. I have to figure out a way to play a previous story and the development of stories has just started. I need to implement more functionality before attempting this.
                    ImageScene previousScene = RenJava.getInstance().getStoryManager().getScene(scene.getIndex() - 1);
                    if (previousScene != null) {
                        previousScene.build(stage);
                    }
                }
            } case PRIMARY -> {
                // Go Forward
                if (stageType == StageType.GAME_WINDOW) {
                    // Go to the next scene map

                    // Play next scene in the story or call the scene end event. StoryHandlerEvent controls stories just call the end event (I think)
                    SceneEndEvent endEvent = new SceneEndEvent(scene);
                    RenJava.callEvent(endEvent);
                }
            }
            case SECONDARY -> {
                // FIXME: 8/5/2023 Uses old methods that have since been removed.

                // Open Main Menu
                Logger logger = RenJava.getInstance().getLogger();
                logger.info("Right click detected");
                if (!player.isRightClickMenu()) {
                    logger.info("Player is not in menu opening menu");
                    MainTitleScreenView screenView = RenJava.getInstance().buildTitleScreen();
                    screenView.build(stage);
                    player.setRightClickMenu(true);
                } else {
                    logger.info("Player is in right click menu return them to the previous scene.");
                    // Return to previous screen
                    stage.hide(); // Hide current scene.
                    //enScene renScene = player.getPreviousScene();
                    //renScene.build(stage);
                }
            }
        }
    }
}
