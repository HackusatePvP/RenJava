package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.piitex.renjava.api.loaders.FontLoader;

/**
 * Represents a graphical overlay for displaying text within a GUI.
 * <p>
 * The {@code TextOverlay} class allows customization of text appearance and position, such as its font, color, size, and location.
 * It supports the use of external font loaders and integrates seamlessly with the GUI framework.
 * </p>
 */
public class TextOverlay extends Overlay implements Region {
    private String text;
    private Color textFillColor;
    private FontLoader fontLoader;
    private double width, height;
    private double scaleWidth, scaleHeight;
    private boolean strikeout;

    /**
     * Constructs a TextOverlay with the specified text content.
     *
     * @param text The text to display in this overlay.
     */
    public TextOverlay(String text) {
        this.text = text;
    }

    /**
     * Constructs a TextOverlay with the specified text and font loader.
     * @param text       The text to display in this overlay.
     * @param fontLoader The font loader used to set the font for the text.
     */
    public TextOverlay(String text, FontLoader fontLoader) {
        this.text = text;
        this.fontLoader = fontLoader;
    }

    /**
     * Constructs a TextOverlay with the specified text and color.
     * @param text          The text to display in this overlay.
     * @param textFillColor The color used to fill the text characters.
     */
    public TextOverlay(String text, Color textFillColor) {
        this.text = text;
        this.textFillColor = textFillColor;
    }

    /**
     * Constructs a TextOverlay with the specified text, color, and font loader.
     * @param text          The text to display in this overlay.
     * @param textFillColor The color used to fill the text characters.
     * @param fontLoader    The font loader used to set the font for the text.
     */
    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader) {
        this.text = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
    }

    /**
     * Constructs a TextOverlay with the specified text, font loader, and position.
     * @param text       The text to display in this overlay.
     * @param fontLoader The font loader used to set the font for the text.
     * @param x          The x-coordinate of the overlay's position.
     * @param y          The y-coordinate of the overlay's position.
     */
    public TextOverlay(String text, FontLoader fontLoader, double x, double y) {
        this.text = text;
        this.fontLoader = fontLoader;
        setX(x);
        setY(y);
    }

    /**
     * Constructs a TextOverlay with the specified text, color, font loader, and position.
     * @param text          The text to display in this overlay.
     * @param textFillColor The color used to fill the text characters.
     * @param fontLoader    The font loader used to set the font for the text.
     * @param x             The x-coordinate of the overlay's position.
     * @param y             The y-coordinate of the overlay's position.
     */
    public TextOverlay(String text, Color textFillColor, FontLoader fontLoader, int x, int y) {
        this.text = text;
        this.textFillColor = textFillColor;
        this.fontLoader = fontLoader;
        setX(x);
        setY(y);
    }

    /**
     * Renders the overlay as a JavaFX {@link Text} node.
     * <p>
     * The method applies all configured properties, such as font, color, position, and effects, to the text.
     * </p>
     *
     * @return A {@link Node} representing the rendered text.
     */
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
