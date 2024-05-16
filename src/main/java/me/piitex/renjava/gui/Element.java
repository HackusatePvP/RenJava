package me.piitex.renjava.gui;

import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.OverlayClickEvent;
import me.piitex.renjava.events.types.OverlayExitEvent;
import me.piitex.renjava.events.types.OverlayHoverEvent;
import me.piitex.renjava.gui.overlay.*;
import me.piitex.renjava.loggers.RenLogger;

public class Element {
    private final Overlay overlay;
    private Node node;
    private Transitions transitions;

    public Element(Overlay overlay) {
        this.overlay = overlay;
        //FIXME: When scaling the height and width the x and y positions need to be scaled to match the modified width and height.
        double scaleX = overlay.scaleX();
        double scaleY = overlay.scaleY();
        if (scaleX > 0) {
            overlay.setWidth(overlay.width() * scaleX);
            overlay.setX(overlay.x() * scaleX);
        }
        if (scaleY > 0) {
            overlay.setHeight(overlay.height() * scaleY);
            overlay.setY(overlay.y() * scaleY);
        }
        if (overlay instanceof ImageOverlay imageOverlay) {
            RenLogger.LOGGER.debug("Processing " + imageOverlay.getFileName());
            Image image = imageOverlay.getImage();
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            if (imageOverlay.width() != 0) {
                imageView.setFitWidth(imageOverlay.width());
            }
            if (imageOverlay.width() != 0) {
                imageView.setFitHeight(imageOverlay.width());
            }
            imageView.setTranslateX(imageOverlay.x());
            imageView.setTranslateY(imageOverlay.y());
            this.node = imageView;
        } else if (overlay instanceof TextOverlay textOverlay) {
            Text text = textOverlay.getText();
            if (textOverlay.getFontLoader() != null) {
                Font font = textOverlay.getFontLoader().getFont();
                text.setFont(font);
            }
            text.setTranslateX(textOverlay.x());
            text.setTranslateY(textOverlay.y());
            this.node = text;
        } else if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.build();
            button.setTranslateX(buttonOverlay.x());
            button.setTranslateY(buttonOverlay.y());
            this.node = button;
        } else if (overlay instanceof TextFlowOverlay textFlowOverlay) {
            TextFlow textFlow = textFlowOverlay.build();
            textFlow.setTranslateX(textFlowOverlay.x());
            textFlow.setTranslateY(textFlowOverlay.y());
            this.node = textFlow;
        } else if (overlay instanceof SliderOverlay sliderOverlay) {
            Slider slider = new Slider(sliderOverlay.width(), sliderOverlay.height(), 4);
            slider.setPrefSize(sliderOverlay.width(), sliderOverlay.height());
            slider.setBlockIncrement(sliderOverlay.getBlockIncrement());
            this.node = slider;
        }

        // Handle specific input
        handleInput();

        // Render transition for specific overlay.
        if (overlay.getTransition() != null) {
            setTransition(overlay.getTransition());
        }
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public Element setTransition(Transitions transitions) {
        this.transitions = transitions;
        return this;
    }

    public void render(Pane root) {
        if (transitions != null) {
            transitions.play(node);
        }
        root.getChildren().add(node);
    }

    private void handleInput() {
        node.setOnMouseEntered(event -> {
            RenJava.callEvent(new OverlayHoverEvent(overlay, event));
        });
        node.setOnMouseClicked(event -> {
            RenJava.callEvent(new OverlayClickEvent(overlay, event));
        });
        node.setOnMouseExited(event -> {
            RenJava.callEvent(new OverlayExitEvent(overlay, event));
        });
    }

    public Node getNode() {
        return node;
    }
}
