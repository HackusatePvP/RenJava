package me.piitex.renjava.api.characters;

import javafx.scene.paint.Color;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.data.PersistentData;

/**
 * The `Character` class represents a character in the game within the RenJava framework. It is used for displaying text and managing character-related variables, such as relationship values.
 * <p>
 * A character is uniquely identified by its ID, which must be provided during instantiation. The ID system allows for multiple characters with the same name.
 * The character's name is used for display purposes and can be changed using the `setName()` method. The display name, which is used for coloring the character's name, can be set using the `setDisplayName()` method.
 * The character's color, specified using the `javafx.scene.paint.Color` class, is used to color the display name.
 * <p>
 * The `Character` class automatically registers itself with the RenJava framework upon instantiation using the `RenJava.getInstance().registerCharacter(this)` method.
 * This allows the character to be accessed and managed by other parts of the framework.
 * <p>
 * This class is abstract and serves as a base class for creating specific character implementations by extending it and providing additional functionality.
 *
 * @see javafx.scene.paint.Color
 * @see me.piitex.renjava.RenJava
 */
public abstract class Character {
    private final String id; // The ID must be unique. The ID system allows you to have multiple characters with the same name.
    private String name; // This is the name display for the character.
    private final Color color;
    private String displayName;

    /**
     * Creates a new character object with the specified ID, name, and color.
     * <p>
     * The Character constructor is used to create a new character object within the RenJava framework.
     * Each character is uniquely identified by its ID, which must be provided during instantiation.
     * The ID system allows for multiple characters with the same name.
     * <p>
     * The character's name is used for display purposes and can be changed using the `setName()` method.
     * The display name, which is used for coloring the character's name, can be set using the `setDisplayName()` method.
     * <p>
     * The character's color, specified using the `javafx.scene.paint.Color` class, is used to color the display name.
     * <p>
     * Upon instantiation, the Character object automatically registers itself with the RenJava framework using the `RenJava.getInstance().registerCharacter(this)` method.
     * This allows the character to be accessed and managed by other parts of the framework.
     * <p>
     * This class is abstract and serves as a base class for creating specific character implementations by extending it and providing additional functionality.
     *
     * @param id The ID must be unique. The ID system allows you to have multiple characters with the same name.
     * @param name This is the name display for the character.
     * @param color The color is used to color the display name.
     *
     * @see javafx.scene.paint.Color
     * @see me.piitex.renjava.RenJava
     */
    public Character(String id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.displayName = name;

        RenJava.getInstance().registerCharacter(this);

        if (this instanceof PersistentData) {
            RenJava.getInstance().registerData((PersistentData) this);
        }
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
