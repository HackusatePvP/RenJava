package me.piitex.renjava.gui.builders;

import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FontLoader {
    private final String name;
    private int size;
    private Font font;

    public FontLoader(String name, int size) {
        this.name = name;
        File directory = new File(System.getProperty("user.dir") + "/game/fonts/");
        File file = new File(directory, name);
        this.size = size;
        try {
            this.font = Font.loadFont(new FileInputStream(file), size);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public FontLoader(String name) {
        this.name = name;
        this.font = Font.font("Arial", 24);
        this.size = 24;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.font = Font.font(name, size);
    }

    public Font getFont() {
        return font;
    }
}
