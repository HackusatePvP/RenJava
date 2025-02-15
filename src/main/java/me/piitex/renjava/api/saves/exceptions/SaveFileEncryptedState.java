package me.piitex.renjava.api.saves.exceptions;

import java.io.File;

public class SaveFileEncryptedState extends Exception {
    private final File file;

    public SaveFileEncryptedState(File file) {
        super("Could not modify '" + file.getName() + "'! The file is encrypted.");
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
