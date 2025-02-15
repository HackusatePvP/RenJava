package me.piitex.renjava.api.exceptions;

public class InvalidEventArgument extends Exception {

    public InvalidEventArgument(Class<?> c, String method) {
        super(c.getName() + ": " + method + " does not have event argument.");
    }
}
