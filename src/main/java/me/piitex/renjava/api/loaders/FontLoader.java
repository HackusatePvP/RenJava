package me.piitex.renjava.api.loaders;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import me.piitex.renjava.RenJava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * The FontLoader is used to load and translate fonts to JavaFX.
 * The fonts are cached for faster load times.
 * The size of a text can also be modified by the FontLoader.
 * The FontLoader can be attached to any overlay that has text.
 * <pre>
 *     {@code
 *       // Load the font
 *       FontLoader font = new FontLoader("font.ttf", 24);
 *
 *       // Apply the font to the text.
 *       TextOverlay overlay = new TextOverlay("Text");
 *       overlay.setFont(font);
 *     }
 * </pre>
 */
public class FontLoader {
    private final String name;
    private double size;
    private Font font;
    private FontWeight weight;
    private FontPosture posture;
    private static final Map<String, Font> cachedFonts = new HashMap<>();

    /**
     * Used to change the size of a provided font.
     *
     * <p>
     *     <pre>
     *         {@code
     *           FontLoader existingFont = new FontLoader("arial, 17"); // Font has a size of 17
     *           FontLoader newFont = new FontLoader(existingFont, 21); // Font has a size of 21.
     *         }
     *     </pre>
     * </p>
     *
     * @param font The existing FontLoader.
     * @param size The new size of the font.
     */
    public FontLoader(FontLoader font, double size) {
        this.name = font.getName();
        this.size = size;
        this.font = Font.font(font.getFont().getFamily(), size);
    }

    /**
     * Adjusts the size of a JavaFX font.
     *
     * @param font The JavaFX {@link Font}.
     * @param size The new size of the font.
     */
    public FontLoader(Font font, double size) {
        this.name = font.getName();
        this.size = size;
        this.font = Font.font(font.getFamily(), size);
    }

    /**
     * Adjusts the boldness of an existing JavaFX {@link Font}.
     *
     * <p>
     *     <pre>
     *         {@code
     *           Font font = new FontLoader("Arial", 17).getFont();
     *           FontLoader boldFont = new FontLoader(font, FontWeight.BOLD, 17);
     *         }
     *     </pre>
     * </p>
     *
     * @param font The JavaFX font.
     * @param weight The boldness of the font.
     * @param size The size of the font.
     */
    public FontLoader(Font font, FontWeight weight, double size) {
        this.name = font.getName();
        this.size = size;
        this.weight = weight;
        this.font = Font.font(font.getFamily(), weight, size);
    }

    /**
     * Adjusts the style of an existing JavaFX {@link Font}.
     *
     * <p>
     *     <pre>
     *         {@code
     *           Font font = new FontLoader("Arial", 17).getFont();
     *           FontLoader italicFont = new FontLoader(font, FontPosture.ITALIC, 17);
     *         }
     *     </pre>
     * </p>
     * @param font The JavaFX font.
     * @param posture The style of the font.
     * @param size The size of the font.
     */
    public FontLoader(Font font, FontPosture posture, double size) {
        this.name = font.getName();
        this.size = size;
        this.posture = posture;
        this.font = Font.font(font.getFamily(), posture, size);
    }

    /**
     * Adjusts both style and boldness of an existing JavaFX {@link Font}.
     *
     * <p>
     *     <pre>
     *         {@code
     *           Font font = new FontLoader("Arial", 17).getFont();
     *           FontLoader styleFont = new FontLoader(font, FontWeight.BOLD, FontPosture.ITALIC, 17);
     *         }
     *     </pre>
     * </p>
     * @param font The JavaFX font.
     * @param weight The boldness of the font.
     * @param posture The style of the font.
     * @param size The size of the font.
     */
    public FontLoader(Font font, FontWeight weight, FontPosture posture, double size) {
        this.name = font.getName();
        this.size = size;
        this.posture = posture;
        this.weight = weight;
        this.font = Font.font(font.getFamily(), weight, posture, size);
    }

    /**
     * Loads a system font or a file font. The file location must be inside '~game/fonts/~'.
     * The system must be installed onto the system for it too work.
     *
     * <p>
     *     <pre>
     *         {@code
     *           // System font
     *           FontLoader systemFont = new FontLoader("arial", 21);
     *           FontLoader fileFont = new FontLoader("Arial.ttf", 21)
     *         }
     *     </pre>
     * </p>
     *
     * @param name The system name or file name of the font.
     * @param size The size of the font.
     */
    public FontLoader(String name, double size) {
        this.name = name;
        File directory = new File(RenJava.getInstance().getBaseDirectory(), "game/fonts/");
        File file = new File(directory, name);
        this.size = size;
        try {
            this.font = Font.loadFont(new FileInputStream(file), size);
        } catch (FileNotFoundException e) {
            this.font = Font.font(name, size);
        }
    }

    /**
     * Loads a system font or a file font. The file location must be inside '~game/fonts/~'.
     * The system must be installed onto the system for it too work.
     *
     * <p>
     *     <pre>
     *         {@code
     *           // System font
     *           FontLoader systemFont = new FontLoader("arial", 21);
     *           FontLoader fileFont = new FontLoader("Arial.ttf")
     *         }
     *     </pre>
     * </p>
     *
     * @param name The system name or file name of the font.
     */
    public FontLoader(String name) {
        File directory = new File(RenJava.getInstance().getBaseDirectory(), "game/fonts/");
        File file = new File(directory, name);
        this.name = name;
        this.size = 24;
        try {
            if (!cachedFonts.containsKey(name)) {
                this.font = Font.loadFont(new FileInputStream(file), size);
                cachedFonts.put(name, font);
            }
        } catch (FileNotFoundException e) {
            this.font = Font.font(name, size);
        }
    }

    public String getName() {
        return name;
    }

    public double getSize() {
        return size;
    }

    public FontPosture getPosture() {
        return posture;
    }

    public FontWeight getWeight() {
        return weight;
    }

    public void setSize(double size) {
        this.size = size;
        this.font = Font.font(name, size);
    }

    public Font getFont() {
        return font;
    }
}
