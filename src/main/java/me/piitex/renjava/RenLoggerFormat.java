package me.piitex.renjava;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class RenLoggerFormat extends Formatter {

    @Override
    public String format(LogRecord record) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(record.getLevel());
        buffer.append(": ");
        buffer.append(record.getMessage());
        buffer.append("\n");
        return buffer.toString();
    }

    @Override
    public String formatMessage(LogRecord record) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(record.getLevel());
        buffer.append(": ");
        buffer.append(record.getMessage());
        buffer.append("\n");
        return buffer.toString();
    }
}
