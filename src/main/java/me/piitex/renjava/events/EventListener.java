package me.piitex.renjava.events;

/**
 * The EventListener interface serves as a marker interface for classes that handle game events.
 * <p>
 * To handle events, implement this interface and define the event handling methods in your class.
 * The event handling methods should be annotated with the {@link Listener} annotation and specify the event type as the parameter.
 * <p>
 * Example:
 * <pre>{@code
 *     public class MyEventListener implements EventListener {
 *         @Listener
 *         public void onButtonClick(ButtonClickEvent event) {
 *             // Code to handle the button click event
 *         }
 *
 *         @Listener
 *         public void onStoryStart(StoryStartEvent event) {
 *             // Code to handle when a new story starts.
 *         }
 *     }
 * }</pre>
 *
 * @see Event
 * @see Listener
 */
public interface EventListener {
}
