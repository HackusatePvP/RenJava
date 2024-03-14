package me.piitex.renjava.gui.overlay;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.piitex.renjava.api.scenes.transitions.Transitions;

import java.util.LinkedList;

public class TextFlowOverlay implements Overlay {
    private double x;
    private double y;
    private Transitions transitions;
    private Font font;
    private Color textColor;

    private LinkedList<Text> texts = new LinkedList<>();

    private final int width, height;

    public TextFlowOverlay(String text, int width, int height) {
        this.width = width;
        this.height = height;
        texts.add(new Text(text));
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

    @Override
    public double x() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double y() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public LinkedList<Text> getTexts() {
        return texts;
    }

    public TextFlow build() {
        TextFlow textFlow = new TextFlow();
        for (Text text : texts) {
            if (font != null) {
                text.setFont(font);
            }
            text.setFill(textColor);
            textFlow.getChildren().add(text);
        }

        textFlow.setPrefSize(width, height);

        return textFlow;
    }
}
