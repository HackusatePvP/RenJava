package me.piitex.renjava.events.defaults;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.*;

import java.util.logging.Logger;

/**
 * Handles all the default story events. This event listener is called automatically.
 */
public class StoryHandlerEventListener implements EventListener {

    @Listener
    public void onSceneStartEvent(SceneStartEvent event) {
        RenScene scene = event.getScene();
        Story story = event.getScene().getStory();
        Player player = RenJava.getInstance().getPlayer();
        if (scene.getStartInterface() != null) {
            scene.getStartInterface().onStart(event);
        }
        if (story == null) return; // Why this would return null? No clue but this is built in java so...
        player.getViewedStories().put(story.getId(), story);
        // Check to see if this scene is the first scene in the story.
        if (story.getSceneIndex(scene) == 0) { // 0 means the first entry.
            // Update the story tracker
            RenJava.getInstance().getStoryManager().setCurrentStory(story);
            StoryStartEvent startEvent = new StoryStartEvent(story);
            RenJava.callEvent(startEvent);
        }
    }

    @Listener
    public void onSceneEndEvent(SceneEndEvent event) {
        Logger logger = RenJava.getInstance().getLogger();
        logger.info("Handling story scene end event...");
        RenScene scene = event.getScene();
        if (scene.getEndInterface() != null) {
            scene.getEndInterface().onEnd(event);
        }

        Story story = event.getScene().getStory();
        if (story == null) {
            logger.info("Scene has no story returning.");
            return;
        }
        if (scene.getIndex() == story.getLastIndex()) {
            logger.info("Calling story end event...");
            StoryEndEvent endEvent = new StoryEndEvent(story);
            RenJava.callEvent(endEvent);
            return;
        }

        // Call next if the story did not end.

        RenScene nextScene = story.getNextScene(scene.getId());
        nextScene.build(RenJava.getInstance().getStage());
    }

    @Listener
    public void onStoryStart(StoryStartEvent event) {
        Story story = event.getStory();
        if (story.getStartInterface() != null) {
            story.getStartInterface().onStoryStart(event);
        }
    }

    @Listener
    public void onStoryEndEvent(StoryEndEvent event) {
        Logger logger = RenJava.getInstance().getLogger();
        logger.info("Handling story end event");
        Story story = event.getStory();

        logger.info("Calling interface method");
        if (story.getEndInterface() != null) {
            story.getEndInterface().onStoryEnd(event);
        }
        story.refresh();
    }
}
