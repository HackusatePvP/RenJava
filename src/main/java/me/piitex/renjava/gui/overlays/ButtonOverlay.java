package me.piitex.renjava.gui.overlays;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.ButtonClickEvent;

public class ButtonOverlay extends Overlay implements Region {
    private Button button;
    private final String id;
    private String text;
    private double scaleX, scaleY;
    private FontLoader font;
    private ImageOverlay image;
    private Paint textFill;
    private Color backgroundColor;
    private Color borderColor;
    private Color hoverColor;
    private ImageOverlay hoverImage;
    private boolean hover = true;
    private int borderWidth = 0;
    private int backgroundRadius = 0;

    private double width, height;
    private double scaleWidth, scaleHeight;

    public ButtonOverlay(String id, String text, Color textFill) {
        this.id = id;
        this.text = text;
        this.textFill = textFill;
    }

    /**
     * Create a button with only text.
     *
     * @param id     Identifier for the button.
     * @param text   Text that will be displayed inside the button.
     * @param textFill  Color of the text.
     * @param x      X-Axis position of the button.
     * @param y      Y-Axis position of the button.
     */
    public ButtonOverlay(String id, String text, Color textFill, double x, double y) {
        this.id = id;
        this.text = text;
        this.textFill = textFill;
        setX(x);
        setY(y);
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
     */
    public ButtonOverlay(String id, String text, Color textFill, FontLoader font, double x, double y) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        setX(x);
        setY(y);
    }

    /**
     * Create a button with only text with a specific font.
     *
     * @param id     Identifier for the button.
     * @param text   Text that will be displayed inside the button.
     * @param font   Font to be used for the text.
     * @param textFill  Color of the text.
     */
    public ButtonOverlay(String id, String text, Color textFill, FontLoader font) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
    }

    public ButtonOverlay(String id, String text, Color textFill, FontLoader font, Color backgroundColor, Color borderColor) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    public ButtonOverlay(String id, String text, Color textFill, FontLoader font, Color backgroundColor, Color borderColor, boolean hover) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.hover = hover;
    }

    public ButtonOverlay(String id, String text, Color textFill, FontLoader font, Color backgroundColor, Color borderColor, Color hoverColor) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.hoverColor = hoverColor;
    }

    /**
     * Create a button with only an image.
     *
     * @param id          Identifier for the button.
     * @param imageLoader Image to be displayed inside the button.
     * @param x           X-Axis position of the button.
     * @param y           Y-Axis position of the button.
     */
    public ButtonOverlay(String id, ImageOverlay imageLoader, double x, double y) {
        this.id = id;
        this.image = imageLoader;
        setX(x);
        setY(y);
    }

    public ButtonOverlay(String id, ImageOverlay imageLoader, double x, double y, int width, int height) {
        this.id = id;
        this.image = imageLoader;
        setX(x);
        setY(y);
        this.width = width;
        this.height = height;
    }

    /**
     * Create a button with image and text
     *
     * @param id          Identifier for the button.
     * @param imageLoader Image to be displayed inside the button.
     * @param text        Text to be displayed inside the button.
     * @param x           X-Axis position of the button.
     * @param y           Y-Axis position of the button.
     */
    public ButtonOverlay(String id, ImageOverlay imageLoader, String text, double x, double y) {
        this.id = id;
        this.image = imageLoader;
        this.text = text;
        setX(x);
        setY(y);
        this.button = build();
    }

    public ButtonOverlay(Button button) {
        this.button = button;
        this.id = button.getId();
    }

    public Button getButton() {
        return button;
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

    public FontLoader getFontLoader() {
        return font;
    }

    public void setFont(FontLoader font) {
        this.font = font;
    }

    @Override
    public Node render() {
        Button button = build();
        button.setTranslateX(getX());
        button.setTranslateY(getY());
        renderTransitions(button);
        return button;
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

    public ImageOverlay getImage() {
        return image;
    }

    public void setImage(ImageOverlay image) {
        this.image = image;
    }

    public Paint getTextFill() {
        return textFill;
    }

    public void setTextFill(Paint color) {
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

    public ImageOverlay getHoverImage() {
        return hoverImage;
    }

    public void setHoverImage(ImageOverlay hoverImage) {
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

    public Button build() {
        if (button != null) {
            return button;
        }
        Button button = new Button();
        button.setId(id);
        if (image != null) {
            ImageView imageView = new ImageView(image.getImage());
            if (width> 0) {
                imageView.setFitWidth(width);
            } else {
                imageView.setFitWidth(image.getWidth());
            }
            if (height > 0) {
                imageView.setFitHeight(height);
            } else {
                imageView.setFitHeight(image.getHeight());
            }
            if (image.getX() > 0) {
                imageView.setX(image.getX());
            }
            if (image.getY() > 0) {
                imageView.setY(image.getY());
            }
            button.setAlignment(Pos.TOP_CENTER);
            button.setGraphic(imageView);
            if (image.getX() > 0) {
                button.getGraphic().setTranslateX(image.getX());
            }
            if (image.getY() > 0) {
                // This can move the image within the button box
                // The y and x values are separate from the main menu
                // Top left of the box is 0,0
                // Bottom right of the box is boxWidth, boxHeight
                button.getGraphic().setTranslateY(image.getY());
            }
        }
        if (text != null && !text.isEmpty()) {
            button.setText(text);
        }
        if (font != null) {
            button.setFont(font.getFont());
        } else {
            // Set default font


            button.setFont(RenJava.getInstance().getConfiguration().getUiFont().getFont());
        }
        if (textFill != null) {
            button.setTextFill(textFill);
        }
        if (height > 0) {
            button.setMaxHeight(height);
            button.setPrefHeight(height);
        }
        if (width > 0) {
            button.setMinWidth(width);
            button.setMaxWidth(width);
            button.setPrefWidth(width);
        }
        String inLine = "";
        if (backgroundColor != null) {
            inLine += "-fx-background-color: " + cssColor(backgroundColor) + "; ";
        } else {
            inLine += "-fx-background-color: transparent; ";
        }
        if (borderColor != null) {
            inLine += "-fx-border-color: " + cssColor(borderColor) + "; ";
        } else {
            inLine += "-fx-border-color: transparent; ";
        }
        inLine += "-fx-border-width: " + borderWidth + "; ";
        inLine += "-fx-background-radius: " + backgroundRadius + ";";

        button.setStyle(inLine);

        if (getX() != 0 && getY() != 0) {
            button.setTranslateX(getX());
            button.setTranslateY(getY());
        }

        button.setOnAction(actionEvent -> {
            ButtonClickEvent event = new ButtonClickEvent(button);
            RenJava.callEvent(event);
        });

        this.button = button;

        setInputControls(button);

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

}
