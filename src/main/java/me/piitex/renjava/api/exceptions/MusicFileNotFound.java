package me.piitex.renjava.api.exceptions;

public class MusicFileNotFound extends Exception {
    public MusicFileNotFound(String fileName) {
        super("Could not load track '" + fileName + "' as it does not exist. Please ensure the file is located within `/game/audio/" + fileName + "'");
    }
}
