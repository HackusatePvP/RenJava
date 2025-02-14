package me.piitex.renjava.gui.overlays;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.gui.overlays.events.IOverlayClick;
import me.piitex.renjava.loggers.RenLogger;

import java.io.File;
import java.net.MalformedURLException;
import java.util.LinkedList;

/**
 * The ButtonOverlay is a visual element that displays a button. A button can contain text or an image, and stylized borders and backgrounds.
 * To handle a button use the generic {@link #onClick(IOverlayClick)} method.
 * <p>
 * <pre>
 *     {@code
 *       ButtonOverlay button = new ButtonOverlay("button-id", "Button", Color.BLACK);
 *       button.onClick(event -> {
 *           // Handle click action.
 *           System.out.println("Clicked on 'button-id'");
 *       });
 *     }
 * </pre>
 * <p>
 * A button is also a {@link Region} which means it has a shape. The shape for an image is a box around the button. The specific can have a specified width and height. A bigger width and height will enlarge the button.
 * <pre>
 *     {@code
 *       ButtonOverlay button = new ButtonOverlay("button-id", "Button");
 *       button.setWidth(width);
 *       button.setHeight(height);
 *     }
 * </pre>
 * <p>
 * A button can display an {@link ImageOverlay} rather than text. The positioning and rendering of the image can also be modified. The {@link #isAlignGraphicToBox()} controls if the images should automatically align within the button.
 * The {@link #getTopImage()} will track the last displayed image on the button.
 * <pre>
 *     {@code
 *       ButtonOverlay button = new ButtonOverlay("button-id", new ImageOverlay("path/to/image.png");
 *
 *       // You can also add mulitiple images to a button
 *       button.addImage(new ImageOverlay("path/to/image.png");
 *     }
 * </pre>
 *
 * @see Overlay
 * @see Region
 * @see ImageOverlay
 */
public class ButtonOverlay extends Overlay implements Region {
    private Button button;
    private final String id;
    private String text;
    private double scaleX, scaleY;
    private FontLoader font;

    private final LinkedList<ImageOverlay> images = new LinkedList<>();
    // Needed for some engine fixes
    private ImageOverlay topImage;
    private boolean alignGraphicToBox = true;

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

    /**
     * Creates a text button with a specified text color.
     * @param id The id of the button.
     * @param text The text to be displayed.
     * @param textFill The color of the display text.
     * @see Color
     */
    public ButtonOverlay(String id, String text, Color textFill) {
        this.id = id;
        this.text = text;
        this.textFill = textFill;
    }

    /**
     * Creates a text button with specified text color and font.
     * @param id The id of the button.
     * @param text The text to be displayed.
     * @param textFill The color of the display text.
     * @param font The font of the text.
     */
    public ButtonOverlay(String id, String text, Color textFill, FontLoader font) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
    }

    /**
     * Creates a text button with specified text color and positioning.
     * @param id The id of the button.
     * @param text The text to be displayed.
     * @param textFill The color of the display text.
     * @param x The x position of the button.
     * @param y The y position of the button.
     */
    public ButtonOverlay(String id, String text, Color textFill, double x, double y) {
        this.id = id;
        this.text = text;
        this.textFill = textFill;
        setX(x);
        setY(y);
    }

    /**
     * Creates a text button with specified text color, text font, and positioning.
     * @param id The id of the button.
     * @param text The text to be displayed.
     * @param textFill The color of the display text.
     * @param font The font of the text.
     * @param x The x position of the button.
     * @param y The y position of the button.
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
     * Creates a text button with specified text color, text font, background color, and border color.
     * @param id The id of the button.
     * @param text The text to be displayed.
     * @param textFill The color of the display text.
     * @param font The font of the text.
     * @param backgroundColor The color of the border box.
     * @param borderColor The fill color of the box.
     */
    public ButtonOverlay(String id, String text, Color textFill, FontLoader font, Color backgroundColor, Color borderColor) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    /**
     * Creates a text button with specified text color, text font, background color, and border color.
     * @param id The id of the button.
     * @param text The text to be displayed.
     * @param textFill The color of the display text.
     * @param font The font of the text.
     * @param backgroundColor The color of the border box.
     * @param borderColor The fill color of the box.
     * @param hoverColor The color of the text when you hover over the button.
     */
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
     * Creates an image button with specified positioning.
     * @param id The id of the button.
     * @param imageLoader The background image of the button.
     * @param x The x position of the button.
     * @param y The y position of the button.
     */
    public ButtonOverlay(String id, ImageOverlay imageLoader, double x, double y) {
        this.id = id;
        this.images.add(imageLoader);
        setX(x);
        setY(y);
    }

    /**
     * Creates an image button with specified positioning.
     * @param id The id of the button.
     * @param imageLoader The background image of the button.
     * @param x The x position of the button.
     * @param y The y position of the button.
     * @param width The width of the button box.
     * @param height The height of the button box.
     * @see Region
     */
    public ButtonOverlay(String id, ImageOverlay imageLoader, double x, double y, int width, int height) {
        this.id = id;
        this.images.add(imageLoader);
        setX(x);
        setY(y);
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a button with both text and an image.
     * @param id The id of the button.
     * @param imageLoader The background image of the button.
     * @param text The text to be displayed.
     * @param x The x position of the button.
     * @param y The y position of the button.
     */
    public ButtonOverlay(String id, ImageOverlay imageLoader, String text, double x, double y) {
        this.id = id;
        this.images.add(imageLoader);
        this.text = text;
        setX(x);
        setY(y);
        this.button = build();
    }

    /**
     * Creates a button with a given JavaFX {@link Button}
     * @param button The JavaFX button.
     */
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

    public LinkedList<ImageOverlay> getImages() {
        return images;
    }

    public void addImage(ImageOverlay imageOverlay) {
        this.images.add(imageOverlay);
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

    public ImageOverlay getTopImage() {
        return topImage;
    }

    public void setTopImage(ImageOverlay topImage) {
        this.topImage = topImage;
    }

    public boolean isAlignGraphicToBox() {
        return alignGraphicToBox;
    }

    public void setAlignGraphicToBox(boolean alignGraphicToBox) {
        this.alignGraphicToBox = alignGraphicToBox;
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
        for (ImageOverlay image : images) {
            topImage = image;
            if (image != null) {
                ImageView imageView = new ImageView(image.getImage());
                if (alignGraphicToBox) {
                    if (width > 0) {
                        imageView.setFitWidth(width);
                    }
                    if (height > 0) {
                        imageView.setFitHeight(height);
                    }
                } else {
                    imageView.setFitWidth(image.getWidth());
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
        }
        if (text != null && !text.isEmpty()) {
            button.setText(text);
        }
        if (font != null) {
            button.setFont(font.getFont());
        } else {
            // Set default font
            button.setFont(RenJava.CONFIGURATION.getUiFont().getFont());
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
            RenJava.getEventHandler().callEvent(event);
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
