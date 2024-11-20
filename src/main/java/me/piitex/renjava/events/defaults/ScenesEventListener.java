package me.piitex.renjava.events.defaults;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.api.scenes.transitions.types.FadingTransition;
import me.piitex.renjava.api.scenes.types.animation.VideoScene;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.overlays.InputFieldOverlay;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.AutoPlayScene;
import me.piitex.renjava.api.scenes.types.choices.Choice;
import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.Priority;
import me.piitex.renjava.events.types.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ScenesEventListener implements EventListener {
    private RenScene lastRenderedScene;

    @Listener
    public void onContainerRender(ContainerRenderEvent event) {
        // When the render is called the scene should be already set.
        RenScene scene = RenJava.getInstance().getPlayer().getCurrentScene();
        if (RenJava.getInstance().getPlayer().isRightClickMenu()) return;
        if (scene != null && scene != lastRenderedScene) {
            Window window = RenJava.getInstance().getGameWindow();
            Transitions transitions = scene.getStartTransition();
            if (transitions != null) {
                RenLogger.LOGGER.debug("Playing transition for scene '{}'", scene.getId());
                if (transitions instanceof FadingTransition fadingTransition) {
                    window.updateBackground(fadingTransition.getColor());
                }
                transitions.play(scene, event.getNode());
            }
        }
        lastRenderedScene = scene;
    }

    @Listener
    public void fixBackground(SceneEndTransitionFinishEvent event) {
        if (event.getTransitions() instanceof FadingTransition) {
            Window window = RenJava.getInstance().getGameWindow();
            window.updateBackground(Color.BLACK);
        }
    }

    @Listener(priority = Priority.HIGHEST)
    public void onSceneStart(SceneStartEvent event) {
        RenScene scene = event.getScene();
        if (scene instanceof AutoPlayScene autoPlayScene) {
            // Play the next scene after duration.

            int duration = autoPlayScene.getDuration();
            Story story = autoPlayScene.getStory();
            Timer timer = new Timer();
            RenScene renScene = story.getNextSceneFromCurrent();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (renScene != null) {
                        if (RenJava.getInstance().getPlayer().getCurrentScene().getId().equalsIgnoreCase(scene.getId())) {
                            Platform.runLater(story::displayNextScene);
                        }
                    } else {
                        Platform.runLater(() -> {
                            if (RenJava.getInstance().getPlayer().getCurrentStory().getId().equalsIgnoreCase(story.getId())) {
                                // Call story end
                                RenJava.callEvent(new StoryEndEvent(story));
                            }
                        });
                    }
                }
            }, TimeUnit.MILLISECONDS.toMillis(duration));
        }
    }

    @Listener(priority = Priority.HIGHEST)
    public void onSceneEnd(SceneEndEvent event) {
        // If the scene is playing media when it ends stop the media
        RenScene scene = event.getScene();
        if (scene instanceof VideoScene videoScene) {
            videoScene.stop();
        }
    }

    @Listener(priority = Priority.HIGHEST)
    public void onChoiceButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();
        RenScene scene = RenJava.getInstance().getPlayer().getCurrentScene();
        if (scene instanceof ChoiceScene choiceScene) {
            Choice choice = choiceScene.getChoice(button.getId());
            if (choice != null) {
                ChoiceSelectEvent selectEvent = new ChoiceSelectEvent(choice, scene.getStory(), scene);
                RenJava.callEvent(selectEvent);
                if (choiceScene.getSelectInterface() != null) {
                    choiceScene.getSelectInterface().onChoiceSelect(selectEvent);
                }
            }
        }
    }

    @Listener(priority = Priority.HIGHEST)
    public void onSceneBuild(SceneBuildEvent event) {
        RenScene scene = event.getScene();
        if (scene.getBuildInterface() != null) {
            scene.getBuildInterface().onBuild(event);
        }
    }

    @Listener(priority = Priority.HIGHEST)
    public void onSceneRender(SceneRenderEvent event) {
        // Event used to save the preview for the save file.
        RenLogger.LOGGER.info("Updating tracker for {}", event.getRenScene().getId());
        RenJava.getInstance().getPlayer().setLastRenderedScene(event.getScene()); // Update the player/tracker information
        RenJava.getInstance().getPlayer().setLastRenderedRenScene(event.getRenScene());

    }
}
