package me.piitex.renjava.gui;

import javafx.scene.Node;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.Overlay;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Container {
    private double x, y;
    private final double width, height;
    private DisplayOrder order;

    // Might be better to index that way someone can modify the index and move overlays around.
    private final LinkedList<Overlay> overlays = new LinkedList<>();
    private final LinkedList<Layout> layouts = new LinkedList<>();
    private final LinkedList<Container> containers = new LinkedList<>();

    public Container(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.order = DisplayOrder.NORMAL;
    }

    public Container(double x, double y, double width, double height, DisplayOrder order) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.order = order;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public DisplayOrder getOrder() {
        return order;
    }

    public void setOrder(DisplayOrder order) {
        this.order = order;
    }

    public void addOverlay(Overlay overlay) {
        this.overlays.add(overlay);
    }

    public void addOverlays(Overlay... overlays) {
        this.overlays.addAll(List.of(overlays));
    }

    public void addOverlays(LinkedList<Overlay> overlays) {
        this.overlays.addAll(overlays);
    }

    public LinkedList<Overlay> getOverlays() {
        return overlays;
    }

    public LinkedList<Container> getContainers() {
        return containers;
    }

    public void addContainer(Container container) {
        this.containers.add(container);
    }

    public void addContainers(Container... containers) {
        this.containers.addAll(List.of(containers));
    }

    public LinkedList<Layout> getLayouts() {
        return layouts;
    }

    public void addLayout(Layout layout) {
        this.layouts.add(layout);
    }

    public void addLayouts(Layout... layouts) {
        this.layouts.addAll(List.of(layouts));
    }

    // Generalized methods
    public void buildBase(LinkedList<Node> lowOrder, LinkedList<Node> normalOrder, LinkedList<Node> highOrder) {
        for (Overlay overlay : getOverlays()) {
            Node node = overlay.render();
            if (node != null) {
                if (overlay.getOrder() == DisplayOrder.LOW) {
                    lowOrder.add(node);
                } else if (overlay.getOrder() == DisplayOrder.NORMAL) {
                    normalOrder.add(node);
                } else if (overlay.getOrder() == DisplayOrder.HIGH) {
                    highOrder.add(node);
                }
            }
        }

        for (Layout layout : getLayouts()) {
            Node node = layout.render(this);
            if (node != null) {
                if (layout.getOrder() == DisplayOrder.LOW) {
                    lowOrder.add(node);
                } else if (layout.getOrder() == DisplayOrder.NORMAL) {
                    normalOrder.add(node);
                } else if (layout.getOrder() == DisplayOrder.HIGH) {
                    highOrder.add(node);
                }
            }
        }

        lowOrder.addAll(normalOrder);
        lowOrder.addAll(highOrder);

        // Render sub containers
        for (Container container : getContainers()) {
            lowOrder.addAll(container.build().getValue()); // Might not work
        }
    }

    /**
     * This does not directly render the container to the screen. Add the container to the window and use the windows render function.
     * @return An entry set where the key is the pane as a node. The value is the collection of nodes which the pane contains.
     */
    public abstract Map.Entry<Node, LinkedList<Node>> build();
}
