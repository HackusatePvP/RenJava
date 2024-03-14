package me.piitex.renjava;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to set default information about the game. Use this annotation to tag the ReJava class constructor.
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Game {
    String name() default ("Name");
    String author() default("Author");
    String version() default("1.0");
}
