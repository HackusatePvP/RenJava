package me.piitex.renjava.gui.overlay;

import javafx.scene.control.TextField;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class InputFieldOverlay implements Overlay {
    private double x;
    private double y;
    private double scaleX, scaleY;
    private double height, width;
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
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double scaleX() {
        return scaleX;
    }

    @Override
    public double scaleY() {
        return scaleY;
    }

    @Override
    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    @Override
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    @Override
    public double width() {
        return width;
    }

    @Override
    public double height() {
        return height;
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
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
        textField.setPrefSize(width, height);
        return textField;
    }
}
