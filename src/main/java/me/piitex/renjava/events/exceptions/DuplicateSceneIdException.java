package me.piitex.renjava.events.exceptions;

public class DuplicateSceneIdException extends Exception {

    public DuplicateSceneIdException(String id) {
        super("Scene already exists with id: " + id);
    }

}
