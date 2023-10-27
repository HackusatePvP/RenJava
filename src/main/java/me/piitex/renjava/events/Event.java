package me.piitex.renjava.events;

import me.piitex.renjava.RenJava;

/**
 * Events are an extension of "actions" during the game. For example, if a player clicks on a button or closes the game, those are actions.
 * <p>
 * Custom events should extend this class and define their own event-specific properties and methods.
 * <p>
 * The event system in RenJava follows a listener-based architecture, where event listeners can register to listen for specific events and perform actions in response to those events.
 * To handle events, you can implement the {@link me.piitex.renjava.events.EventListener} interface and register your listener with the RenJava framework.
 * Event listeners can listen for specific events and perform actions in response to those events.
 * <p>
 * The event system supports different event priorities, allowing you to control the order in which listeners are invoked for a particular event.
 * <p>
 * To trigger an event, you can use the {@link RenJava#callEvent(Event)} method, which will invoke all registered listeners for that event.
 * <p>
 * Custom events can be created by extending this class and adding event-specific logic. Here's an example of how to create a custom event:
 * <pre>{@code
 * public class CustomEvent extends Event {
 *     private String eventData;
 *
 *     public CustomEvent(String eventData) {
 *         this.eventData = eventData;
 *     }
 *
 *     public String getEventData() {
 *         return eventData;
 *     }
 * }
 * }</pre>
 * In the example above, we create a custom event called `CustomEvent` that extends the `Event` class. We add a `String` property called `eventData` and provide a constructor to set its value.
 * We also provide a getter method to retrieve the `eventData` property.
 * <p>
 * Once the custom event is created, you can add your own logic and call the event using the {@link RenJava#callEvent(Event)} method. For example:
 * <pre>{@code
 * CustomEvent customEvent = new CustomEvent("Some event data");
 * RenJava.callEvent(customEvent);
 * }</pre>
 * In the example above, we create an instance of the `CustomEvent` class and pass it to the `RenJava.callEvent()` method to trigger the event. This will invoke all registered listeners for the `CustomEvent`.
 * <p>
 * You can pass your custom events in a listener class to handle the data.
 * <pre {@code
 * public class MyListenerClass implements EventListener {
 *     @Listener
 *     public void onEvent(CustomEvent event) {
 *         // Gather and process event data.
 *     }
 * }
 * }</pre>
 * <p>
 * Overall, events provide a way to handle and respond to various game actions and events in a structured and organized manner.
 */
public abstract class Event { }
