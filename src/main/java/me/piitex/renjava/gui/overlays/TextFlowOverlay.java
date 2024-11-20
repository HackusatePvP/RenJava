package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.loggers.RenLogger;

import java.util.LinkedList;

public class TextFlowOverlay extends Overlay implements Region {
    private LinkedList<Overlay> texts = new LinkedList<>();
    private Color textFillColor = Color.BLACK;
    private FontLoader font;
    private double width, height;
    private double scaleWidth, scaleHeight;

    public TextFlowOverlay(String text, int width, int height) {
        this.width = width;
        this.height = height;
        texts.add(new TextOverlay(text));
    }

    public TextFlowOverlay(String text, FontLoader fontLoader, int width, int height) {
        this.texts.add(new TextOverlay(text));
        this.font = fontLoader;
        this.width = width;
        this.height = height;
    }

    public TextFlowOverlay(TextOverlay text, int width, int height) {
        this.width = width;
        this.height = height;
        texts.add(text);
    }

    public TextFlowOverlay(LinkedList<Overlay> texts, int width, int height) {
        this.width = width;
        this.height = height;
        this.texts = texts;
    }

    public LinkedList<Overlay> getTexts() {
        return texts;
    }

    public void setTexts(LinkedList<Overlay> texts) {
        this.texts = texts;
    }

    public Color getTextFillColor() {
        return textFillColor;
    }

    public FontLoader getFontLoader() {
        return font;
    }

    public void setFont(FontLoader font) {
        this.font = font;
    }

    public void setTextFillColor(Color textFillColor) {
        this.textFillColor = textFillColor;
    }

    public void add(Overlay overlay) {
        texts.add(overlay);
    }

    @Override
    public Node render() {
        return build();
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

    public TextFlow build() {
        TextFlow textFlow = new TextFlow();
        for (Overlay overlay : texts) {
            // Check node type
            if (overlay instanceof TextOverlay text) {
                text.setText(text.getText().replace("\\n", System.lineSeparator()));
                if (font != null) {
                    text.setFont(font);
                }
                text.setTextFill(textFillColor);
            } else if (overlay instanceof HyperLinkOverlay hyperlink) {
                if (font != null) {
                    hyperlink.setFont(font);
                }
            } else if (overlay instanceof ButtonOverlay button) {
                if (font != null) {
                    button.setFont(font);
                }
                button.setTextFill(textFillColor);
            } else if (overlay instanceof InputFieldOverlay inputField) {
                if (font != null) {
                    inputField.setFont(font);
                }
            } else {
                RenLogger.LOGGER.warn("Unsupported overlay in TextFlow. {}", overlay.toString());
            }

            textFlow.getChildren().add(overlay.render());
        }

        textFlow.setPrefSize(width, height);
        textFlow.setTranslateX(getX());
        textFlow.setTranslateY(getY());
        setInputControls(textFlow);
        renderTransitions(textFlow);
        return textFlow;
    }
}
