package me.piitex.renjava.api.saves.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation processor for storing generic types into a save file.
 * @apiNote Only use on fields. Fields cannot be final.
 */
@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Data {
}
