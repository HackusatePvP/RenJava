package me.piitex.renjava.api.builders;

import javafx.scene.control.TextField;

import java.io.File;

public class InputFieldBuilder {
    private final double x;
    private final double y;
    private final FontLoader fontLoader;

    public InputFieldBuilder(double x, double y, FontLoader fontLoader) {
        this.x = x;
        this.y = y;
        this.fontLoader = fontLoader;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public TextField build() {
        TextField textField = new TextField();
        textField.setTranslateX(x);
        textField.setTranslateY(y);
        textField.setStyle("");
        textField.setStyle("-fx-control-inner-background: transparent; -fx-background-color transparent;");
        return textField;
    }
}
