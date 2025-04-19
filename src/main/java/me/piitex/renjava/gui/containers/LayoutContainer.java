package me.piitex.renjava.gui.containers;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class LayoutContainer extends Container {
    private Node right, middle, left, top, bottom;

    public LayoutContainer(double width, double height) {
        super(0, 0, width, height);
    }

    public LayoutContainer(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public LayoutContainer(double x, double y, double width, double height, DisplayOrder order) {
        super(x, y, width, height, order);
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getMiddle() {
        return middle;
    }

    public void setMiddle(Node middle) {
        this.middle = middle;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getTop() {
        return top;
    }

    public void setTop(Node top) {
        this.top = top;
    }

    public Node getBottom() {
        return bottom;
    }

    public void setBottom(Node bottom) {
        this.bottom = bottom;
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        BorderPane borderPane = new BorderPane();
        if (left != null)
            borderPane.setLeft(left);
        if (middle != null)
            borderPane.setCenter(middle);
        if (right != null)
            borderPane.setRight(right);
        if (top != null)
            borderPane.setTop(top);
        if (bottom != null)
            borderPane.setTop(top);

        borderPane.setTranslateX(getX());
        borderPane.setTranslateY(getY());
        borderPane.setPrefSize(getWidth(), getHeight());

        LinkedList<Node> lowOrder = new LinkedList<>();
        LinkedList<Node> normalOrder = new LinkedList<>();
        LinkedList<Node> highOrder = new LinkedList<>();
        buildBase(lowOrder, normalOrder, highOrder);

        return new AbstractMap.SimpleEntry<>(borderPane, lowOrder);
    }
}
