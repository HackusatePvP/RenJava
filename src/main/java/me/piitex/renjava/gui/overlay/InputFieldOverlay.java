package me.piitex.renjava.gui.overlay;

import javafx.scene.control.TextField;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class InputFieldOverlay implements Overlay {
    private final double x;
    private final double y;
    private final FontLoader fontLoader;
    private Transitions transitions;

    public InputFieldOverlay(double x, double y, FontLoader fontLoader) {
        this.x = x;
        this.y = y;
        this.fontLoader = fontLoader;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
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
