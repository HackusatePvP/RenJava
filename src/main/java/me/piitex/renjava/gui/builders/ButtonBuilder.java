package me.piitex.renjava.gui.builders;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

/**
 * Start button for the main menu. Simply create and set this class to override the default one.
 */
public class ButtonBuilder {
    private final String id;
    private String text;
    private Font font;
    private Image image;

    private Color color;

    private int x, y;
    private final double xScale, yScale;

    /**
     * Create a button with only text.
     *
     * @param id     Identifier for the button.
     * @param text   Text that will be displayed inside the button.
     * @param color  Color of the text.
     * @param x      X-Axis position of the button.
     * @param y      Y-Axis position of the button.
     * @param xScale X-Axis scale of the button.
     * @param yScale Y-Axis scale of the button.
     */
    public ButtonBuilder(String id, String text, Color color, int x, int y, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    /**
     * Create a button with only text with a specific font.
     *
     * @param id     Identifier for the button.
     * @param text   Text that will be displayed inside the button.
     * @param font   Font to be used for the text.
     * @param color  Color of the text.
     * @param x      X-Axis position of the button.
     * @param y      Y-Axis position of the button.
     * @param xScale X-Axis scale of the button.
     * @param yScale Y-Axis scale of the button.
     */
    public ButtonBuilder(String id, String text, Font font, Color color, int x, int y, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    /**
     * Create a button with only an image.
     *
     * @param id          Identifier for the button.
     * @param imageLoader Image to be displayed inside the button.
     * @param x           X-Axis position of the button.
     * @param y           Y-Axis position of the button.
     * @param xScale      X-Axis scale of the button.
     * @param yScale      Y-Axis scale of the button.
     */
    public ButtonBuilder(String id, ImageLoader imageLoader, int x, int y, double xScale, double yScale) {
        this.id = id;
        try {
            this.image = imageLoader.build();
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    /**
     * Create a button with only an image.
     *
     * @param id          Identifier for the button.
     * @param imageLoader Image to be displayed inside the button.
     * @param text        Text to be displayed inside the button.
     * @param x           X-Axis position of the button.
     * @param y           Y-Axis position of the button.
     * @param xScale      X-Axis scale of the button.
     * @param yScale      Y-Axis scale of the button.
     */
    public ButtonBuilder(String id, ImageLoader imageLoader, String text, int x, int y, double xScale, double yScale) {
        this.id = id;
        try {
            this.image = imageLoader.build();
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.text = text;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
    }


    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getXScale() {
        return xScale;
    }

    public double getYScale() {
        return yScale;
    }

    public Button build() {
        Button button = new Button();
        button.setId(id);
        if (text != null && !text.isEmpty()) {
            button.setText(text);
        }
        if (font != null) {
            button.setFont(font);
        }
        if (image != null) {
            ImageView imageView = new ImageView(image);
            button.setGraphic(imageView);
        }
        if (color != null) {
            button.setTextFill(color);
        }
        button.setTranslateX(x);
        button.setTranslateY(y);
        button.setScaleX(xScale);
        button.setScaleY(yScale);
        button.setOnAction(actionEvent -> {
            ButtonClickEvent event = new ButtonClickEvent(RenJava.getInstance().getPlayer().getCurrentScene(), button);
            RenJava.callEvent(event);
        });
        return button;
    }
}
