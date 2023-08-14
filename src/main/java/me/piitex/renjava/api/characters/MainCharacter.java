package me.piitex.renjava.api.characters;

import javafx.scene.paint.Color;

/**
 * Represents the main playable character.
 */
public abstract class MainCharacter extends Character {

    public MainCharacter(String id, String name, Color color) {
        super(id, name, color);
    }

}
