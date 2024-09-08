package me.piitex.renjava.utils;

import javafx.scene.input.KeyCode;

import java.util.List;

// List of modifier keys for key held events
// JavaFX does not handle modifier keys the same way they do other keys
public class ModifierKeyList {

    public static KeyCode[] modifier = new KeyCode[] {
      KeyCode.CONTROL,
      KeyCode.SHIFT,
      KeyCode.ALT,
      KeyCode.META
    };
}
