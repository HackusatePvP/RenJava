package me.piitex.renjava;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class RenLoggerFormat extends Formatter {

    @Override
    public String format(LogRecord record) {
        String buffer = record.getLevel() +
                ": " +
                record.getMessage() +
                "\n";
        return buffer;
    }

    @Override
    public String formatMessage(LogRecord record) {
        String buffer = record.getLevel() +
                ": " +
                record.getMessage() +
                "\n";
        return buffer;
    }
}
