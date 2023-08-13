package me.piitex.renjava.events.exceptions;

public class InvalidEventArgument extends Exception {

    public InvalidEventArgument(Class<?> c, String method) {
        super(c.getName() + ": " + method + " does not have event argument.");
    }
}
