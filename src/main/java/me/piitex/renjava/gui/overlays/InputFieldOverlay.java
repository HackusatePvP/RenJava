package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.events.types.InputSetEvent;
import me.piitex.renjava.gui.overlays.events.IInputSetEvent;

public class InputFieldOverlay extends Overlay implements Region {
    private double width, height;
    private double scaleWidth, scaleHeight;
    private final String defaultInput;
    private String hintText = "";

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

    @Override
    public Node render() {
        TextField textField = build();
        textField.setPromptText(hintText);
        textField.setText(defaultInput);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (getiInputSetEvent() != null) {
                getiInputSetEvent().onInputSet(new InputSetEvent(this, newValue));
            }
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

    public void setInputSetEvent(IInputSetEvent iInputSetEvent) {
        this.iInputSetEvent = iInputSetEvent;
    }

    public TextField build() {
        TextField textField = new TextField();
        textField.setTranslateX(getX());
        textField.setTranslateY(getY());
        textField.setStyle("");
        textField.setStyle("-fx-control-inner-background: grey; -fx-background-color: grey;");
        textField.setPrefSize(width, height);
        return textField;
    }
}
