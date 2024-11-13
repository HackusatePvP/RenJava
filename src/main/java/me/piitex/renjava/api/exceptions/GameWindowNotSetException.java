package me.piitex.renjava.api.exceptions;

public class GameWindowNotSetException extends Exception {
    public GameWindowNotSetException() {
        super("Game window operation called but the window is not set.");
    }
}
