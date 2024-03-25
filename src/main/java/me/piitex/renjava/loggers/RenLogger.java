package me.piitex.renjava.loggers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenLogger {
    public static Logger LOGGER;

    public static void init() {
        LOGGER = LoggerFactory.getLogger("Ren");
    }

}
