package me.piitex.renjava.gui.containers;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.layouts.Layout;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class ScrollContainer extends Container {
    private final Layout layout;
    private ScrollPane scrollPane;
    private double xOffset, yOffset;

    public ScrollContainer(Layout layout, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.layout = layout;
    }

    public double getXOffset() {
        return xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
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

        // Offset overlays by 10
        if (xOffset > 0 || yOffset > 0) {
            lowOrder.forEach(node -> {
                node.setTranslateX(node.getTranslateX() + xOffset);
                node.setTranslateY(node.getTranslateX() + yOffset);
            });
        }

        return new AbstractMap.SimpleEntry<>(scrollPane, lowOrder);
    }
}