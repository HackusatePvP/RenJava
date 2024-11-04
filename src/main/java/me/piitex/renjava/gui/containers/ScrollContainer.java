package me.piitex.renjava.gui.containers;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.gui.overlays.Overlay;
import me.piitex.renjava.loggers.RenLogger;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class ScrollContainer extends Container {
    private final Layout layout;
    private ScrollPane scrollPane;

    public ScrollContainer(Layout layout, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.layout = layout;
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        scrollPane = new ScrollPane();
        scrollPane.setTranslateX(getX());
        scrollPane.setTranslateY(getY());
        scrollPane.setPrefSize(getWidth(), getHeight());
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Build pane layout for the scroll content
        Pane pane = layout.getPane();
        scrollPane.setContent(pane);


        LinkedList<Node> lowOrder = new LinkedList<>();
        LinkedList<Node> normalOrder = new LinkedList<>();
        LinkedList<Node> highOrder = new LinkedList<>();

        lowOrder.add(layout.render(this));

        buildBase(lowOrder, normalOrder, highOrder);

        return new AbstractMap.SimpleEntry<>(scrollPane, lowOrder);
    }
}
