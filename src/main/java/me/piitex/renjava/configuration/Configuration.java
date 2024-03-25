package me.piitex.renjava.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to define configuration settings for the application window.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {
    /**
     * The title of the application window.
     */
    String title();

    /**
     * The width of the base resolution.
     */
    int width();

    /**
     * The height of the base resolution.
     */
    int height();

    /**
     * The file path for the window icon. Default value is "gui/window_icon.png".
     */
    String windowIconPath() default "gui/window_icon.png";
}