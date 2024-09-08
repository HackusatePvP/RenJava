package me.piitex.renjava.utils;

import javafx.scene.input.KeyCode;

public class KeyUtils {
    public static boolean ctrlDown = false;
    public static boolean shiftDown = false;
    public static boolean altDown = false;
    public static boolean metaDown = false;

    public static void setModifierDown(KeyCode keyCode, boolean pressed) {
        if (keyCode == KeyCode.CONTROL) {
            ctrlDown = pressed;
        }
        if (keyCode == KeyCode.SHIFT) {
            shiftDown = pressed;
        }
        if (keyCode == KeyCode.ALT) {
            altDown = pressed;
        }
        if (keyCode == KeyCode.META) {
            metaDown = pressed;
        }
    }

    public static KeyCode getCurrentKeyDown() {
        if (ctrlDown) {
            return KeyCode.CONTROL;
        }
        if (shiftDown) {
            return KeyCode.SHIFT;
        }
        if (altDown) {
            return KeyCode.ALT;
        }
        if (metaDown) {
            return KeyCode.META;
        }
        return null;
    }
}