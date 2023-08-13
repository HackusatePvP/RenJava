package me.piitex.renjava.api.characters;

import javafx.scene.paint.Color;
import me.piitex.renjava.RenJava;

/**
 * A Character represents a character in the game. This is used when displaying text and also has some variables attached to it. For example; relationship values.
 */
public abstract class Character {
    private final String id; // The ID must be unique. The ID system allows you to have multiple characters with the same name.
    private String name; // This is the name display for the character.
    private final Color color;
    private String displayName;

    /**
     *
     * @param id The ID must be unique. The ID system allows you to have multiple characters with the same name.
     * @param name This is the name display for the character.
     * @param color The color is used to color the displayname.
     */
    public Character(String id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.displayName = name;

        RenJava.getInstance().registerCharacter(this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
