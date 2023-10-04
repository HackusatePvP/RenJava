package me.piitex.renjava.api.saves.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Data annotation is used to mark fields that should be saved in the save file.
 * <p>
 * Fields annotated with Data will be included in the save file.
 * The annotated fields can be private or public, but they cannot be final.
 *
 * @apiNote Only use this annotation on fields. Fields cannot be final.
 */
@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Data {
}
