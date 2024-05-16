package me.piitex.renjava.gui.overlay;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.overlay.events.IOverlayClick;
import me.piitex.renjava.gui.overlay.events.IOverlayHover;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

import java.util.concurrent.atomic.AtomicReference;

public class ButtonOverlay implements Overlay {
    private Button button;
    private Transitions transitions;
    private Menu menu;
    private final String id;
    private String text;
    private double scaleX, scaleY;
    private Font font;
    private ImageOverlay image;
    private Color textFill;
    private Color backgroundColor;
    private Color borderColor;
    private Color hoverColor;
    private ImageLoader hoverImage;
    private boolean hover;
    private int borderWidth = 0;
    private int backgroundRadius = 0;

    private IOverlayHover iOverlayHover;
    private IOverlayClick iOverlayClick;

    private double x = 1, y = 1;
    private double maxHeight, maxWidth;
    private final double xScale, yScale;

    public ButtonOverlay(String id, String text, Color textFill, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.textFill = textFill;
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
    public ButtonOverlay(String id, String text, Color textFill, Font font, double x, double y, double xScale, double yScale) {
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
    public ButtonOverlay(String id, String text, Color textFill, Font font, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public ButtonOverlay(String id, String text, Color textFill, Font font, Color backgroundColor, Color borderColor, double xScale, double yScale) {
        this.id = id;
        this.text = text;
        this.font = font;
        this.textFill = textFill;
        this.xScale = xScale;
        this.yScale = yScale;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    public ButtonOverlay(String id, String text, Color textFill, Font font, Color backgroundColor, Color borderColor, boolean hover, double xScale, double yScale) {
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

    public ButtonOverlay(String id, String text, Color textFill, Font font, Color backgroundColor, Color borderColor, Color hoverColor, double xScale, double yScale) {
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
    public ButtonOverlay(String id, ImageOverlay imageLoader, double x, double y, double xScale, double yScale) {
        this.id = id;
        this.image = imageLoader;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public ButtonOverlay(String id, ImageOverlay imageLoader, double x, double y, int maxWidth, int maxHeight, double xScale, double yScale) {
        this.id = id;
        this.image = imageLoader;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
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
    public ButtonOverlay(String id, ImageOverlay imageLoader, String text, double x, double y, double xScale, double yScale) {
        this.id = id;
        this.image = imageLoader;
        this.text = text;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
        this.button = build();
    }

    public ButtonOverlay(String id, Menu menu, double x, double y, double xScale, double yScale) {
        this.id = id;
        this.menu = menu;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
        this.button = build();
    }

    public ButtonOverlay(String id, Menu menu, double x, double y, int maxWidth, int maxHeight) {
        this.id = id;
        this.menu = menu;
        this.x = x;
        this.y = y;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.xScale = 1;
        this.yScale = 1;
        this.button = build();
    }

    public ButtonOverlay(Button button) {
        this.button = button;
        this.id = button.getId();
        this.xScale = button.getScaleX();
        this.yScale = button.getScaleY();
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

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public ImageOverlay getImage() {
        return image;
    }

    public void setImage(ImageOverlay image) {
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
        return maxWidth;
    }

    @Override
    public double height() {
        return maxHeight;
    }

    @Override
    public void setWidth(double width) {
        this.maxWidth = width;
    }

    @Override
    public void setHeight(double height) {
        this.maxHeight  = height;
    }

    public void setY(double y) {
        this.y = y;
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
            ImageView imageView = new ImageView(image.getImage());
            imageView.setFitWidth(image.width());
            imageView.setFitHeight(image.height());
            if (image.x() > 0) {
                imageView.setX(image.x());
            }
            if (image.y() > 0) {
                imageView.setY(image.y());
            }
            button.setAlignment(Pos.TOP_CENTER);
            button.setGraphic(imageView);
            if (image.x() > 0) {
                button.getGraphic().setTranslateX(image.x());
            }
            if (image.y() > 0) {
                // This can move the image within the button box
                // The y and x values are separate from the main menu
                // Top left of the box is 0,0
                // Bottom right of the box is boxWidth, boxHeight
                button.getGraphic().setTranslateY(image.y());
            }
        }
        if (menu != null) {
            //TODO: This is so fucking stupid if it works.
            menu.setRenderFadeInFill(false);
            menu.setRenderFadeOutFill(false);

            // Resize elements
            menu.setScaleX(width() / menu.getWidth());
            menu.setScaleY(height() / menu.getHeight());

            Pane node = menu.render(false);

            node.setMaxSize(maxWidth, maxHeight);
            node.setTranslateX(button.getTranslateX() - 20);
            node.setTranslateY(button.getTranslateY() - 20);
            node.setPrefWidth(node.getMaxWidth() - 100);
            node.setPrefHeight(node.getMaxHeight() - 100);
            button.setGraphic(node);
//            button.setContentDisplay(ContentDisplay.LEFT);
//            button.setAlignment(Pos.CENTER_LEFT);
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
            button.setPrefHeight(maxHeight);
        }
        if (maxWidth > 0) {
            button.setMaxWidth(maxWidth);
            button.setPrefWidth(maxWidth);
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

        this.button = button;

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
