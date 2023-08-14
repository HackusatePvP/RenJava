package me.piitex.renjava.api.scenes.types.choices;

import me.piitex.renjava.events.types.ChoiceSelectEvent;

/**
 * The ChoiceSelectInterface is a functional interface that allows you to define custom actions when a choice is selected in a ChoiceScene.
 * Implement this interface and pass it to the {@link ChoiceScene#onChoice(ChoiceSelectInterface)} method to handle the player's choice selection.
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * ChoiceScene scene = new ChoiceScene("myScene", backgroundImage);
 * scene.addChoice(new Choice("choice1", "Option 1"));
 * scene.addChoice(new Choice("choice2", "Option 2"));
 * scene.onChoice(event -> {
 *     if (event.getChoice().getId().equals("choice1")) {
 *         // Handle choice 1 selection
 *     } else if (event.getChoice().getId().equals("choice2")) {
 *         // Handle choice 2 selection
 *     }
 * });
 * }</pre>
 * </p>
 *
 * @see ChoiceScene
 * @see Choice
 */
public interface ChoiceSelectInterface {

    void onChoiceSelect(ChoiceSelectEvent event);
}
