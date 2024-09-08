package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.piitex.renjava.api.loaders.FontLoader;

import java.util.LinkedList;

public class TextFlowOverlay extends Overlay implements Region {
    private LinkedList<Text> texts = new LinkedList<>();
    private InputFieldOverlay inputFieldOverlay;
    private Color textFillColor = Color.BLACK;
    private Font font;
    private double width, height;
    private double scaleWidth, scaleHeight;

    public TextFlowOverlay(String text, int width, int height) {
        this.width = width;
        this.height = height;
        texts.add(new Text(text));
    }

    public TextFlowOverlay(String text, FontLoader fontLoader, int width, int height) {
        this.texts.add(new Text(text));
        this.font = fontLoader.getFont();
        this.width = width;
        this.height = height;
    }

    public TextFlowOverlay(Text text, int width, int height) {
        this.width = width;
        this.height = height;
        texts.add(text);
    }

    public TextFlowOverlay(LinkedList<Text> texts, int width, int height) {
        this.width = width;
        this.height = height;
        this.texts = texts;
    }

    public LinkedList<Text> getTexts() {
        return texts;
    }

    public void setTexts(LinkedList<Text> texts) {
        this.texts = texts;
    }

    public Color getTextFillColor() {
        return textFillColor;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setTextFillColor(Color textFillColor) {
        this.textFillColor = textFillColor;
    }

    public InputFieldOverlay getInputFieldOverlay() {
        return inputFieldOverlay;
    }

    public void setInputFieldOverlay(InputFieldOverlay inputFieldOverlay) {
        this.inputFieldOverlay = inputFieldOverlay;
    }

    @Override
    public Node render() {
        return build();
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

    public TextFlow build() {
        TextFlow textFlow = new TextFlow();
        for (Text text : texts) {
            // Not the best way to go about this
            text = new Text(text.getText().replace("\\n", System.lineSeparator()));

            if (font != null) {
                text.setFont(font);
            }
            text.setFill(textFillColor);

            textFlow.getChildren().add(text);
        }

        if (inputFieldOverlay != null) {
            textFlow.getChildren().add(inputFieldOverlay.build());
        }

        textFlow.setPrefSize(width, height);
        textFlow.setTranslateX(getX());
        textFlow.setTranslateY(getY());
        setInputControls(textFlow);
        renderTransitions(textFlow);
        return textFlow;
    }
}
