package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.events.types.InputSetEvent;
import me.piitex.renjava.gui.overlays.events.IInputSetEvent;

public class InputFieldOverlay extends Overlay implements Region {
    private double width, height;
    private double scaleWidth, scaleHeight;
    private final String defaultInput;
    private FontLoader fontLoader;
    private String hintText = "";
    private String currentText;

    private IInputSetEvent iInputSetEvent;


    public InputFieldOverlay(String defaultInput, double x, double y, double width, double height) {
        this.defaultInput = defaultInput;
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    public InputFieldOverlay(String defaultInput, String hintText, double x, double y, double width, double height) {
        this.defaultInput = defaultInput;
        this.hintText = hintText;
        this.width = width;
        this.height = height;
        setX(x);
        setY(y);
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public void setFontLoader(FontLoader fontLoader) {
        this.fontLoader = fontLoader;
    }

    public String getCurrentText() {
        return currentText;
    }

    @Override
    public Node render() {
        TextField textField = build();
        textField.setPromptText(hintText);
        textField.setText(defaultInput);
        if (!defaultInput.isEmpty()) {
            // If there is content manually call set event
            InputSetEvent event = new InputSetEvent(this, defaultInput);
            getiInputSetEvent().onInputSet(event);
            RenJava.callEvent(event);
        }

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            InputSetEvent event = new InputSetEvent(this, newValue);
            if (getiInputSetEvent() != null) {
                getiInputSetEvent().onInputSet(event);
            }
            RenJava.callEvent(event);
            currentText = newValue;
        });
        setInputControls(textField);
        renderTransitions(textField);
        return textField;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setWidth(double w) {
        this.width = w;
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
    }

    @Override
    public double getScaleWidth() {
        return scaleWidth;
    }

    @Override
    public void setScaleWidth(double w) {
        this.scaleWidth = w;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }

    public IInputSetEvent getiInputSetEvent() {
        return iInputSetEvent;
    }

    public void onInputSetEvent(IInputSetEvent iInputSetEvent) {
        this.iInputSetEvent = iInputSetEvent;
    }

    public TextField build() {
        TextField textField = new TextField();
        textField.setTranslateX(getX());
        textField.setTranslateY(getY());
//        textField.setStyle("");
        if (fontLoader != null) {
            textField.setFont(getFontLoader().getFont());
        }
        textField.setStyle("-fx-control-inner-background: transparent; -fx-background-color: transparent;");
        textField.setPrefSize(width, height);
        return textField;
    }
}
