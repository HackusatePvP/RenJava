package me.piitex.renjava.gui.overlay;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

import java.util.concurrent.atomic.AtomicReference;

public class ButtonOverlay implements Overlay {
    private Button button;
    private Transitions transitions;

    private final String id;
    private String text;
    private Font font;
    private Image image;
    private Color textFill;
    private Color backgroundColor;
    private Color borderColor;
    private Color hoverColor;
    private ImageLoader hoverImage;
    private boolean hover;
    private int borderWidth = 0;
    private int backgroundRadius = 0;

    private double x = 1, y = 1;
    private int maxHeight = 0, maxWidth = 0;
    private final double xScale, yScale;

    public ButtonOverlay(String id, String text, Color textFill, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.textFill = textFill;
        this.xScale = x;
        this.yScale = y;
    }

    public ButtonOverlay(String id, String text, Color textFill, Font font, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.textFill = textFill;
        this.font = font;
        this.xScale = x;
        this.yScale = y;
    }

    /**
     * Create a button with only text.
     *
     * @param id     Identifier for the button.
     * @param text   Text that will be displayed inside the button.
     * @param textFill  Color of the text.
     * @param x      X-Axis position of the button.
     * @param y      Y-Axis position of the button.
     * @param xScale X-Axis scale of the button.
     * @param yScale Y-Axis scale of the button.
     */
    public ButtonOverlay(String id, String text, Color textFill, double x, double y, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.textFill = textFill;
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
     * @param textFill  Color of the text.
     * @param x      X-Axis position of the button.
     * @param y      Y-Axis position of the button.
     * @param xScale X-Axis scale of the button.
     * @param yScale Y-Axis scale of the button.
     */
    public ButtonOverlay(String id, String text, Font font, Color textFill, double x, double y, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
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
     * @param textFill  Color of the text.
     * @param xScale X-Axis scale of the button.
     * @param yScale Y-Axis scale of the button.
     */
    public ButtonOverlay(String id, String text, Font font, Color textFill, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public ButtonOverlay(String id, String text, Font font, Color textFill, Color backgroundColor, Color borderColor, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.xScale = xScale;
        this.yScale = yScale;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    public ButtonOverlay(String id, String text, Font font, Color textFill, Color backgroundColor, Color borderColor, boolean hover, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.xScale = xScale;
        this.yScale = yScale;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.hover = hover;
    }

    public ButtonOverlay(String id, String text, Font font, Color textFill, Color backgroundColor, Color borderColor, Color hoverColor, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.xScale = xScale;
        this.yScale = yScale;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.hover = true;
        this.hoverColor = hoverColor;
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

    public Color getTextFill() {
        return textFill;
    }

    public void setTextFill(Color color) {
        this.textFill = color;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
        this.hover = true;
    }

    public ImageLoader getHoverImage() {
        return hoverImage;
    }

    public void setHoverImage(ImageLoader hoverImage) {
        this.hoverImage = hoverImage;
        this.hover = true;
    }

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getBackgroundRadius() {
        return backgroundRadius;
    }

    public void setBackgroundRadius(int backgroundRadius) {
        this.backgroundRadius = backgroundRadius;
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

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }

    public double getXScale() {
        return xScale;
    }

    public double getYScale() {
        return yScale;
    }

    public Button build() {
        if (button != null) {
            return button;
        }
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
        if (textFill != null) {
            button.setTextFill(textFill);
        }
        if (maxHeight > 0) {
            button.setMaxHeight(maxHeight);
        }
        if (maxWidth > 0) {
            button.setMaxWidth(maxWidth);
        }
        String inLine = "";
        if (backgroundColor != null) {
            inLine += "-fx-background-color: " + cssColor(backgroundColor) + "; ";
        }
        if (borderColor != null) {
            inLine += "-fx-border-color: " + cssColor(borderColor) + "; ";
        }
        inLine += "-fx-border-width: " + borderWidth + "; ";
        inLine += "-fx-background-radius: " + backgroundRadius + ";";

        // https://stackoverflow.com/questions/30680570/javafx-button-border-and-hover
        if (hover) {
            AtomicReference<String> attomicInLine = new AtomicReference<>(inLine);
            button.setOnMouseEntered(mouseEvent -> {
                if (hoverColor != null) {
                    button.setTextFill(hoverColor);
                    button.setStyle(attomicInLine.get());
                }
                if (hoverImage != null) {
                    try {
                        button.setGraphic(new ImageView(hoverImage.build()));
                    } catch (ImageNotFoundException e) {
                        RenJava.getInstance().getLogger().severe(e.getMessage());
                    }
                }
            });
            button.setOnMouseExited(mouseEvent -> {
                button.setTextFill(textFill);
                button.setStyle(attomicInLine.get());
                if (image != null) {
                    button.setGraphic(new ImageView(image));
                }
            });
        }

        button.setStyle(inLine);

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

    // Helper css function
    private String cssColor(Color color) {
        return String.format("rgba(%d, %d, %d, %f)",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()),
                color.getOpacity());
    }

    public static ButtonOverlay copyOf(String id, ButtonOverlay builder) {
        ButtonOverlay toReturn = new ButtonOverlay(id, builder.getText(), builder.getFont(), builder.getTextFill(), builder.x(), builder.y(), builder.getXScale(), builder.getYScale());
        toReturn.setImage(builder.getImage());
        return toReturn;
    }
}
