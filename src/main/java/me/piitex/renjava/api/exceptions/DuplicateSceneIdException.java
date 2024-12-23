package me.piitex.renjava.api.exceptions;

public class DuplicateSceneIdException extends Exception {

    public DuplicateSceneIdException(String id) {
        super("Scene already exists with id: " + id);
    }

}
