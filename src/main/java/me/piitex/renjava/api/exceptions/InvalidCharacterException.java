package me.piitex.renjava.api.exceptions;

public class InvalidCharacterException extends Exception {

    public InvalidCharacterException(String characterID) {
        super("Character '" + characterID + "' does not exist. Make sure the character is registered and created within the framework.");
    }
}
