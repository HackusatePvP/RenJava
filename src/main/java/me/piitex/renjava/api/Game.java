package me.piitex.renjava.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to set default information about the game.
 * Use this annotation to tag the ReJava class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Game {
    /**
     * The name of the game.
     */
    String name();

    /**
     * The author of the game.
     */
    String author();

    /**
     * The version of the game.
     */
    String version();
}