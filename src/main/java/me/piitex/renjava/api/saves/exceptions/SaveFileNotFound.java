package me.piitex.renjava.api.saves.exceptions;

public class SaveFileNotFound extends Exception {

    public SaveFileNotFound() {
        super("Could not find save file to load.");
    }
}
