package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.piitex.renjava.api.loaders.FontLoader;

/**
 * The TextOverlay is a visual element which displays text. The overlay supports font, positioning, and color.
 * This overlay has a defined {@link Region}.
 * <p>
 * <pre>
 *     {@code
 *     TextOverlay overlay = new TextOverlay("Text");
 *     overlay.setFont(font);
 *     overlay.setTextFill(color);
 *     overlay.setX(x);
 *     overlay.setY(y);
 *     overlay.setWidth(width);
 *     overlay.setHeight(height);
 *     }
 * </pre>
 */
public class TextOverlay extends Overlay implements Region {
    private String text;
    private Color textFillColor;
    private FontLoader fontLoader;
    private double width, height;
    private double scaleWidth, scaleHeight;
    private boolean strikeout;

    /**
     * Creates an overlay of the text.
     * @param text The text for the overlay.
     */
    public TextOverlay(String text) {
        this.text = text;
    }

    /**
     * Creates a TextOverlay with a specific font.
     * @param text The text for the overlay.
     * @param fontLoader The font to be used.
     * @see FontLoader
     */
    public TextOverlay(String text, FontLoader fontLoader) {
        this.text = text;
        this.fontLoader = fontLoader;
    }

    /**
     * Creates a TextOverlay with a specific text color.
     * @param text The text for the overlay.
     * @param textFillColor The color of the text.
     * @see Color
     */
    public TextOverlay(String text, Color textFillColor) {
        this.text = text;
        this.textFillColor = textFillColor;
    }

    /**
     * Creates a TextOverlay with a specific color and font.
     * @param text The text for the overlay.
     * @param textFillColor The color of the text.
     * @param fontLoader The font to be used.
     * @see Color
     * @see FontLoader
     */
    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader) {
        this.text = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
    }

    /**
     * Creates a TextOverlay with a specific font and positioning.
     * @param text The text for the overlay.
     * @param fontLoader The font to be used.
     * @param x The x position of the overlay.
     * @param y The y position of the overlay.
     * @see FontLoader
     * @see Region
     */
    public TextOverlay(String text, FontLoader fontLoader, double x, double y) {
        this.text = text;
        this.fontLoader = fontLoader;
        setX(x);
        setY(y);
    }

    /**
     * Creates a TextOverlay with a specific color, font, and positioning.
     * @param text The text for the overlay.
     * @param textFillColor The color of the text.
     * @param fontLoader The font to be used.
     * @param x The x position of the overlay.
     * @param y The y position of the overlay.
     * @see Color
     * @see FontLoader
     * @see Region
     */
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

    public void setFont(FontLoader fontLoader) {
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
