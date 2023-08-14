package me.piitex.renjava.events;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Listener annotation is used to mark methods that handle game events.
 * <p>
 * To handle events, annotate a method with the {@link Listener} annotation and provide the event type as the parameter.
 * The annotated method will be invoked when an event of the specified type is triggered.
 * <p>
 * Example:
 * <pre>{@code
 *     @Listener
 *     public void onButtonClick(ButtonClickEvent event) {
 *         // Code to handle the button click event
 *     }
 * }</pre>
 *
 * @see Event
 * @see EventListener
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Listener {
    Priority priority() default Priority.NORMAL;
}
