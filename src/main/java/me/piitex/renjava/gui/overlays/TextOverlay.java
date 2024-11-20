package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.piitex.renjava.api.loaders.FontLoader;

public class TextOverlay extends Overlay implements Region {
    private String text;
    private Color textFillColor;
    private FontLoader fontLoader;
    private double width, height;
    private double scaleWidth, scaleHeight;
    private boolean strikeout;

    public TextOverlay(String text) {
        this.text = text;
    }

    public TextOverlay(String text, FontLoader fontLoader) {
        this.text = text;
        this.fontLoader = fontLoader;
    }

    public TextOverlay(String text, Color textFillColor) {
        this.text = text;
        this.textFillColor = textFillColor;
    }

    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader) {
        this.text = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
    }

    public TextOverlay(String text, FontLoader fontLoader, double x, double y) {
        this.text = text;
        this.fontLoader = fontLoader;
        setX(x);
        setY(y);
    }

    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader, int x, int y) {
        this.text = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
        setX(x);
        setY(y);
    }

    @Override
    public Node render() {
        Text text = new Text(getText());
        if (fontLoader != null) {
            Font font = fontLoader.getFont();
            text.setFont(font);
        }
        text.setStrikethrough(strikeout);
        if (getTextFillColor() != null) {
            text.setFill(getTextFillColor());
        }
        text.setTranslateX(getX());
        text.setTranslateY(getY());
        setInputControls(text);
        renderTransitions(text);
        return text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isStrikeout() {
        return strikeout;
    }

    public void setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public void setFontLoader(FontLoader fontLoader) {
        this.fontLoader = fontLoader;
    }

    public Color getTextFillColor() {
        return textFillColor;
    }

    public void setTextFill(Color textFillColor) {
        this.textFillColor = textFillColor;
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
}
