package me.piitex.renjava.gui;

import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.overlay.*;

public class Element {
    private final Overlay overlay;
    private Node node;
    private Transitions transitions;

    public Element(Overlay overlay) {
        this.overlay = overlay;
        if (overlay instanceof ImageOverlay imageOverlay) {
            Image image = imageOverlay.image();
            ImageView imageView = new ImageView(image);
            imageView.setTranslateX(imageOverlay.x());
            imageView.setTranslateY(imageOverlay.y());
            this.node = imageView;
        } else if (overlay instanceof TextOverlay textOverlay) {
            Text text = textOverlay.text();
            text.setTranslateX(textOverlay.x());
            text.setTranslateY(textOverlay.y());
            this.node = text;
        } else if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.build();
            button.setTranslateX(buttonOverlay.x());
            button.setTranslateY(buttonOverlay.y());
            this.node = button;
        } else if (overlay instanceof TextFlowOverlay textFlowOverlay) {
            TextFlow textFlow = textFlowOverlay.textFlowBuilder().build();
            textFlow.setTranslateX(textFlowOverlay.x());
            textFlow.setTranslateY(textFlowOverlay.y());
            this.node = textFlow;
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

    public Node getNode() {
        return node;
    }
}
