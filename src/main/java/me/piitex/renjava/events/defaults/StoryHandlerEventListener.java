package me.piitex.renjava.events.defaults;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.Priority;
import me.piitex.renjava.events.types.*;

/**
 * Handles all the default story events. This event listener is called automatically.
 */
public class StoryHandlerEventListener implements EventListener {

    @Listener(priority = Priority.HIGHEST)
    public void onSceneStartEvent(SceneStartEvent event) {
        RenScene scene = event.getScene();
        Story story = event.getScene().getStory();
        if (scene.getStartInterface() != null) {
            scene.getStartInterface().onStart(event);
        }
        if (story == null) return;
        // Check to see if this scene is the first scene in the story.
        if (story.getSceneIndex(scene) == 0) { // 0 means the first entry.
            // Update the story tracker
            //RenJava.getInstance().getPlayer().setCurrentStory(story);
            StoryStartEvent startEvent = new StoryStartEvent(story);
            RenJava.callEvent(startEvent);
        }
    }

    @Listener(priority = Priority.HIGHEST)
    public void onStoryStart(StoryStartEvent event) {
        Story story = event.getStory();
        story.refresh(); // Called first to update story before it starts.
        if (story.getStartInterface() != null) {
            story.getStartInterface().onStoryStart(event);
        }
    }

    @Listener(priority = Priority.HIGHEST)
    public void onStoryEndEvent(StoryEndEvent event) {
        Story story = event.getStory();
        if (story.getEndInterface() != null) {
            story.getEndInterface().onStoryEnd(event);
        }
        story.refresh(); // Called last to update story after it ends.
    }
}
