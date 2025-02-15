package me.piitex.renjava.gui;

import javafx.scene.Node;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.Overlay;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The container houses all the elements that render onto the {@link Window}. The class can be extended to support different containers that can handle rendering differently.
 * The default base container is the {@link EmptyContainer}. It does not have any special rendering properties.
 * <pre>
 * {@code
 *      EmptyContainer container = new EmptyContainer(double width, double height);
 *      // Add elements to the container.
 * }
 * </pre>
 * <p>
 *     The {@link Window} will have to render and handle the container. The two components work in unison.
 * <p>
 * <pre>
 * {@code
 *      Container container = new EmptyContainer(1920, 1080);
 *
 *      window.addContainer(container);
 *
 *      window.render();
 * }
 * </pre>
 */
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

    /**
     * @return The x position of the container in correlation to the window.
     */
    public double getX() {
        return x;
    }

    /**
     * Set the x position of the container.
     * @param x The x position.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return The y position of the container in correlation to the window.
     */
    public double getY() {
        return y;
    }

    /**
     * Set the y position of the container.
     * @param y The y position.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return The width of the container.
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return The height of the container.
     */
    public double getHeight() {
        return height;
    }

    /**
     * {@link DisplayOrder} is used to order the stacking of elements or containers.
     * @return The display order of the container.
     */
    public DisplayOrder getOrder() {
        return order;
    }

    /**
     * Sets the {@link DisplayOrder} for the container.
     * @param order The {@link DisplayOrder}.
     */
    public void setOrder(DisplayOrder order) {
        this.order = order;
    }

    /**
     * Adds a singular {@link Overlay} to the container.
     * @param overlay The {@link Overlay} to be added.
     */
    public void addOverlay(Overlay overlay) {
        this.overlays.add(overlay);
    }

    /**
     * Adds an array of {@link Overlay}s to the container. The overlays are positioned by the order of the array.
     * The first overlay of the array is the first to be added.
     * @param overlays An array of {@link Overlay}s to be added.
     */
    public void addOverlays(Overlay... overlays) {
        this.overlays.addAll(List.of(overlays));
    }

    /**
     * Adds a linked list of {@link Overlay}s to the container.
     * @param overlays The list of {@link Overlay}s to be added.
     */
    public void addOverlays(LinkedList<Overlay> overlays) {
        this.overlays.addAll(overlays);
    }

    /**
     * Gets all {@link Overlay}s added to the container.
     * @return The current linked list of {@link Overlay}s.
     */
    public LinkedList<Overlay> getOverlays() {
        return overlays;
    }

    /**
     * Gets all sub-containers for the container.
     * @return The current linked list of sub-containers.
     */
    public LinkedList<Container> getContainers() {
        return containers;
    }

    /**
     * Adds a container as a sub-container to this container.
     * @param container The sub-container to be added.
     */
    public void addContainer(Container container) {
        this.containers.add(container);
    }

    /**
     * Adds an array of sub-containers to be added. The containers are positioned by the order of the array.
     * The first container of the array is the first to be added.
     * @param containers An array of containers to be added.
     */
    public void addContainers(Container... containers) {
        this.containers.addAll(List.of(containers));
    }

    /**
     * Gets all {@link Layout}s for the container.
     * @return The current linked list of {@link Layout}s
     */
    public LinkedList<Layout> getLayouts() {
        return layouts;
    }

    /**
     * Adds a {@link Layout} to be added to the container.
     * @param layout The {@link Layout} to be added.
     */
    public void addLayout(Layout layout) {
        this.layouts.add(layout);
    }

    /**
     * Adds an array of {@link Layout}s to be added. The layouts are positioned by the order of the array.
     * The first layout of the array is the first to be added.
     * @param layouts An array {@link Layout}s to be added.
     */
    public void addLayouts(Layout... layouts) {
        this.layouts.addAll(List.of(layouts));
    }

    /**
     * This should only be used by the engine. This builds the overlays, layouts, and containers.
     * It translates the RenJava API into JavaFX by converting the gui components into {@link Node}s.
     * @param lowOrder The list of low order nodes.
     * @param normalOrder The list of normal order nodes.
     * @param highOrder The list of high order nodes.
     */
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
     * Builds and assembles the container.
     * @return An entry set where the key is the pane as a node. The value is the collection of nodes which the pane contains.
     */
    public abstract Map.Entry<Node, LinkedList<Node>> build();
}
