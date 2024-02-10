package me.piitex.renjava.api.builders;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class FontLoader {
    private final String name;
    private double size;
    private Font font;
    private FontWeight weight;
    private FontPosture posture;

    private static final Map<String, Font> cachedFonts = new HashMap<>();

    public FontLoader(Font font, double size) {
        this.name = font.getName();
        this.size = size;
        this.font = Font.font(font.getFamily(), size);
    }

    public FontLoader(Font font, FontWeight weight, double size) {
        this.name = font.getName();
        this.size = size;
        this.weight = weight;
        this.font = Font.font(font.getFamily(), weight, size);
    }

    public FontLoader(Font font, FontPosture posture, double size) {
        this.name = font.getName();
        this.size = size;
        this.posture = posture;
        this.font = Font.font(font.getFamily(), posture, size);
    }

    public FontLoader(String name, int size) {
        this.name = name;
        File directory = new File(System.getProperty("user.dir") + "/game/fonts/");
        File file = new File(directory, name);
        this.size = size;
        try {
            this.font = Font.loadFont(new FileInputStream(file), size);
        } catch (FileNotFoundException e) {
            this.font = Font.font(name, size);
        }
    }

    public FontLoader(String name) {
        File directory = new File(System.getProperty("user.dir") + "/game/fonts/");
        File file = new File(directory, name);
        this.name = name;
        try {
            if (!cachedFonts.containsKey(name)) {
                this.font = Font.loadFont(new FileInputStream(file), 24);
            }
        } catch (FileNotFoundException e) {
            this.font = Font.font(name, size);
        }
        this.size = 24;
    }

    public String getName() {
        return name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
        this.font = Font.font(name, size);
    }

    public Font getFont() {
        return font;
    }
}
