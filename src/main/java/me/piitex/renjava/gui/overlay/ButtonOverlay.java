package me.piitex.renjava.gui.overlay;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

public class ButtonOverlay implements Overlay {
    private final Button button;
    private Transitions transitions;

    private final String id;
    private String text;
    private Font font;
    private Image image;
    private Color color;

    private double x = 1, y = 1;
    private final double xScale, yScale;

    public ButtonOverlay(String id, String text, Color color, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.xScale = x;
        this.yScale = y;
        this.button = build();
    }

    public ButtonOverlay(String id, String text, Color color, Font font, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.font = font;
        this.xScale = x;
        this.yScale = y;
        this.button = build();
    }

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
    public ButtonOverlay(String id, String text, Color color, double x, double y, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
        this.button = build();
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
    public ButtonOverlay(String id, String text, Font font, Color color, double x, double y, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
        this.button = build();
    }

    /**
     * Create a button with only text with a specific font.
     *
     * @param id     Identifier for the button.
     * @param text   Text that will be displayed inside the button.
     * @param font   Font to be used for the text.
     * @param color  Color of the text.
     * @param xScale X-Axis scale of the button.
     * @param yScale Y-Axis scale of the button.
     */
    public ButtonOverlay(String id, String text, Font font, Color color, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.color = color;
        this.xScale = xScale;
        this.yScale = yScale;
        this.button = build();
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
    public ButtonOverlay(String id, ImageLoader imageLoader, double x, double y, double xScale, double yScale) {
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
        this.button = build();
    }

    /**
     * Create a button with image and text
     *
     * @param id          Identifier for the button.
     * @param imageLoader Image to be displayed inside the button.
     * @param text        Text to be displayed inside the button.
     * @param x           X-Axis position of the button.
     * @param y           Y-Axis position of the button.
     * @param xScale      X-Axis scale of the button.
     * @param yScale      Y-Axis scale of the button.
     */
    public ButtonOverlay(String id, ImageLoader imageLoader, String text, double x, double y, double xScale, double yScale) {
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
        this.button = build();
    }

    public ButtonOverlay(Button button) {
        this.button = button;
        this.id = button.getId();
        this.xScale = button.getScaleX();
        this.yScale = button.getScaleY();
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

    @Override
    public double x() {
        return button.getTranslateX();
    }

    @Override
    public double y() {
        return button.getTranslateY();
    }

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }
    public void setX(double x) {
        this.x = x;
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
        if (image != null) {
            ImageView imageView = new ImageView(image);
            button.setGraphic(imageView);
        }
        if (text != null && !text.isEmpty()) {
            button.setText(text);
        }
        if (font != null) {
            button.setFont(font);
        } else {
            // Set default font
            button.setFont(RenJava.getInstance().getConfiguration().getUiFont().getFont());
        }
        if (color != null) {
            button.setTextFill(color);
        }
        if (x != 0 && y != 0) {
            button.setTranslateX(x);
            button.setTranslateY(y);
        }
        button.setScaleX(xScale);
        button.setScaleY(yScale);

        button.setOnAction(actionEvent -> {
            ButtonClickEvent event = new ButtonClickEvent(RenJava.getInstance().getPlayer().getCurrentScene(), button);
            RenJava.callEvent(event);
        });
        return button;
    }

    public static ButtonOverlay copyOf(String id, ButtonOverlay builder) {
        ButtonOverlay toReturn = new ButtonOverlay(id, builder.getText(), builder.getFont(), builder.getColor(), builder.x(), builder.y(), builder.getXScale(), builder.getYScale());
        toReturn.setImage(builder.getImage());
        return toReturn;
    }
}
