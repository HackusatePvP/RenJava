package me.piitex.renjava.configuration;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.loggers.RenLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Custom file that works similar to Properties
// key=value
// All string base
public class InfoFile {
    private final File file;

    private final Map<String, String> entryMap = new HashMap<>();

    public InfoFile(File file, boolean create) {
        this.file = file;
        if (!file.exists()) {
            try {
                if (create) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                RenLogger.LOGGER.error("Could not create file '{}'", file.getName(), e);
                RenJava.writeStackTrace(e);
            }
        }

        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("=")) {
                        String[] entry = line.split("=");
                        if (entry.length == 1) {
                            entryMap.put(entry[0], "");
                        } else {
                            entryMap.put(entry[0], entry[1].replace("\"", "").trim());
                        }
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                RenLogger.LOGGER.error("Could not process {}", file.getName(), e);
                RenJava.writeStackTrace(e);
            }
        }
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean containsKey(String key) {
        return entryMap.containsKey(key);
    }

    public Object get(String key) {
        return entryMap.get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    public Integer getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public Double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    public Float getFloat(String key) {
        return Float.parseFloat(key);
    }

    public Long getLong(String key) {
        return Long.getLong(getString(key));
    }

    public Short getShort(String key) {
        return Short.parseShort(getString(key));
    }

    public Byte getByte(String key) {
        return Byte.parseByte(getString(key));
    }

    public void write(String key, String value) {
        entryMap.put(key, value);

        try {
            FileWriter writer = new FileWriter(file, false);
            entryMap.forEach((s, s2) -> {
                try {
                    writer.write(s + "=" + s2 + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
