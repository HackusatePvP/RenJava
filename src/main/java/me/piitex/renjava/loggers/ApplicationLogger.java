package me.piitex.renjava.loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationLogger {
    public Logger LOGGER;

    public ApplicationLogger(String name) {
        LOGGER = LoggerFactory.getLogger(name);
    }

    public ApplicationLogger(Class<?> clazz) {
        String[] splitClassName = clazz.getName().split("\\.");
        int index = splitClassName.length;
        String name = splitClassName[index - 1];

        LOGGER = LoggerFactory.getLogger(name);
    }
}