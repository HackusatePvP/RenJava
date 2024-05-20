package me.piitex.renjava.gui.overlay;

import javafx.scene.SubScene;
import javafx.scene.control.Hyperlink;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.overlay.events.IOverlayClick;
import me.piitex.renjava.gui.overlay.events.IOverlayHover;

import java.util.LinkedList;

public class TextFlowOverlay implements Overlay, Region {
    private double x;
    private double y;
    private double scaleX, scaleY;
    private Transitions transitions;
    private Font font;
    private Color textColor = Color.BLACK;

    private IOverlayClick iOverlayClick;
    private IOverlayHover iOverlayHover;

    private LinkedList<Text> texts = new LinkedList<>();

    private double width, height;

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

    @Override
    public double x() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double y() {
        return y;
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

    @Override
    public void setOnclick(IOverlayClick iOverlayClick) {
        this.iOverlayClick = iOverlayClick;
    }

    @Override
    public void setOnHover(IOverlayHover iOverlayHover) {
        this.iOverlayHover = iOverlayHover;
    }

    @Override
    public IOverlayClick getOnClick() {
        return iOverlayClick;
    }

    @Override
    public IOverlayHover getOnHover() {
        return iOverlayHover;
    }


    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }


    public LinkedList<Text> getTexts() {
        return texts;
    }

    public TextFlow build() {
        TextFlow textFlow = new TextFlow();
        for (Text text : texts) {
            // Not the best way to go about this
            text = new Text(text.getText().replace("\\n", System.lineSeparator()));

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
