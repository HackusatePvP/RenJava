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

    private Transitions transitions;

    public Element(Overlay overlay) {
        this.overlay = overlay;
    }

    public Element setTransition(Transitions transitions) {
        this.transitions = transitions;
        return this;
    }

    public void render(Pane root) {
        if (overlay instanceof ImageOverlay imageOverlay) {
            Image image = imageOverlay.image();
            ImageView imageView = new ImageView(image);
            imageView.setTranslateX(imageOverlay.x());
            imageView.setTranslateY(imageOverlay.y());
            postToRoot(root, imageView);
        } else if (overlay instanceof TextOverlay textOverlay) {
            Text text = textOverlay.text();
            text.setTranslateX(textOverlay.x());
            text.setTranslateY(textOverlay.y());
            postToRoot(root, textOverlay.text());
        } else if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.build();
            button.setTranslateX(buttonOverlay.x());
            button.setTranslateY(buttonOverlay.y());
            postToRoot(root, button);
        } else if (overlay instanceof TextFlowOverlay textFlowOverlay) {
            TextFlow textFlow = textFlowOverlay.textFlowBuilder().build();
            textFlow.setTranslateX(textFlowOverlay.x());
            textFlow.setTranslateY(textFlowOverlay.y());
            postToRoot(root, textFlow);
        }
    }

    private void postToRoot(Pane root, Node node) {
        root.getChildren().add(node);
        if (transitions != null) {
            transitions.play(node);
        }
    }
}
