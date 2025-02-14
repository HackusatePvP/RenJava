package me.piitex.renjava.api.characters;

import javafx.scene.paint.Color;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.data.Data;

public abstract class Character {
    private final String id; // The ID must be unique. The ID system allows you to have multiple characters with the same name.
    @Data private String name; // This is the name display for the character.
    private final Color color;
    @Data private String displayName;

    public Character(String id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.displayName = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setDisplayName(name);
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
