package me.piitex.renjava.events.types;

import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.choices.Choice;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.Event;

/**
 * Fired when a player choices a choice in a choice scene.
 */
public class ChoiceSelectEvent extends Event {
    private final Story story;
    private final RenScene scene;
    private final Choice choice;

    public ChoiceSelectEvent(Choice choice, Story story, RenScene scene) {
        this.choice = choice;
        this.story = story;
        this.scene = scene;
    }

    public Choice getChoice() {
        return choice;
    }

    public Story getStory() {
        return story;
    }

    public RenScene getScene() {
        return scene;
    }
}
