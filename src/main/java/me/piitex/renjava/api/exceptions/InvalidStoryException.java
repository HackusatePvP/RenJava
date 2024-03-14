package me.piitex.renjava.api.exceptions;

public class InvalidStoryException extends Exception {

    public InvalidStoryException(String id) {
        super("Story '" + id + "' does not exist and thus could not be started. Ensure that the story class is registered within the framework. `new MyStoryClass()`");
    }
}
