package me.piitex.renjava.gui;

import javafx.scene.Node;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.Overlay;
import me.piitex.renjava.loggers.RenLogger;

import java.io.File;
import java.util.*;

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
public abstract class Container extends Element {
    private double x, y;
    private final double width, height;

    // Set container to a debug state. This will output the rendering process.
    public boolean debug = false;

    private final LinkedHashMap<Integer, Element> elements = new LinkedHashMap<>();

    private final List<File> stylesheets = new ArrayList<>();


    public Container(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Container(double x, double y, double width, double height, int index) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setIndex(index);
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
     * Retrieves the current element at the specific index. If the element is not present this will return null.
     * @param index Position of the desired element.
     * @return The {@link Element}
     */
    public Element getElementAt(int index) {
        return elements.get(index);
    }

    /**
     * Adds an element to the container. The added element will be indexed to the front of the container.
     * @param element The {@link Element} to be added.
     */
    public void addElement(Element element) {
        int index = element.getIndex();
        if (index == 0) {
            index = elements.size();
        }

        addElement(element, index);
    }

    /**
     * Adds the element to the specific index. If there is an element already bound to that index it is shuffled forward.
     *
     * @param element The {@link Element} to add to the container.
     * @param index The index/order of the element.
     */
    public void addElement(Element element, int index) {
        Element current = elements.get(index);
        if (current != null) {
            int i = index + 1;
            addElement(getElementAt(index), i);
        }
        elements.put(index, element);
    }

    /**
     * Adds an array of elements to the container. The elements are positioned by the order of the array.
     * The added elements will be indexed to the front of the container.
     * @param elements The array of {@link Element}s to be added.
     */
    public void addElements(Element... elements) {
        for (Element element : elements) {
            addElement(element);
        }
    }

    /**
     * Adds a {@link LinkedList<Element>} of elements to the container. The elements are position by the order of the list.
     * @param elements The list of elements to be added.
     */
    public void addElements(LinkedList<Element> elements) {
        for (Element element : elements) {
            addElement(element);
        }
    }

    public void removeElement(int index) {
        elements.remove(index);
    }

    public void removeAllElement(Element element) {
        LinkedHashMap<Integer, Element> toRemove = new LinkedHashMap<>(elements);
        toRemove.forEach((integer, e) -> {
            if (e == element) {
                elements.remove(integer);
            }
        });
    }

    public void moveElement(int oldIndex, int newIndex) {
        Element element = elements.get(oldIndex);
        if (element != null) {
            elements.put(newIndex, element);
            elements.remove(oldIndex);
        }
    }

    /**
     * Adds an overlay to the container. The added overlay will be indexed to the front of the container.
     * @deprecated Use {@link #addElement(Element)} instead.
     * @param overlay The {@link Overlay} to be added.
     *
     */
    @Deprecated
    public void addOverlay(Overlay overlay) {
        addElement(overlay);
    }

    /**
     * Adds the overlay to the specific index. If there is an element already bound to that index it is shuffled forward.
     * @deprecated Use {@link #addElement(Element, int)} instead.
     * @param overlay The {@link Overlay} to add to the container.
     * @param index The index/order of the element.
     */
    @Deprecated
    public void addOverlay(Overlay overlay, int index) {
        addElement(overlay, index);
    }

    /**
     * Adds an array of {@link Overlay}s to the container. The overlays are positioned by the order of the array.
     * The first overlay of the array is the first to be added.
     * @deprecated Use {@link #addElements(Element...)} instead.
     * @param overlays An array of {@link Overlay}s to be added.
     */
    @Deprecated
    public void addOverlays(Overlay... overlays) {
        for (Overlay overlay : overlays) {
            addElement(overlay);
        }
    }

    /**
     * Adds {@link LinkedList<Overlay>} of overlays to the container.
     * @param overlays The list of {@link Overlay} to be added
     */
    public void addOverlays(LinkedList<Overlay> overlays) {
        for (Overlay overlay : overlays) {
            addElement(overlay, elements.size());
        }
    }


    /**
     * Gets all {@link Overlay}s added to the container.
     * @return The current linked list of {@link Overlay}s.
     */
    public LinkedList<Overlay> getOverlays() {
        LinkedList<Overlay> toReturn = new LinkedList<>();
        elements.values().stream().filter(element -> element instanceof Overlay).forEach(element -> {
            Overlay overlay = (Overlay) element;
            toReturn.add(overlay);
        });
        return toReturn;
    }

    /**
     * Gets all sub-containers for the container.
     * @return The current linked list of sub-containers.
     */
    public LinkedList<Container> getContainers() {
        LinkedList<Container> toReturn = new LinkedList<>();
        elements.values().stream().filter(element -> element instanceof Container).forEach(element -> {
            Container container = (Container) element;
            toReturn.add(container);
        });
        return toReturn;
    }

    /**
     * Adds the sub-container to the specific index. If there is an element already bound to that index it is shuffled forward.
     * @deprecated Use {@link #addElement(Element)} instead.
     * @param container The {@link Container} to add to the container.
     * @param index The index/order of the element.
     */
    @Deprecated
    public void addContainer(Container container, int index) {
        addElement(container, index);
    }

    /**
     * Adds a sub-container to the container. The added container will be indexed to the front of the view.
     * @deprecated Use {@link #addElement(Element)} instead.
     * @param container The {@link Container} to be added.
     */
    @Deprecated
    public void addContainer(Container container) {
        addElement(container);
    }

    /**
     * Adds an array of sub-containers to be added. The containers are positioned by the order of the array.
     * The first container of the array is the first to be added.
     * @deprecated Use {@link #addElements(Element...)} instead.
     * @param containers An array of containers to be added.
     */
    @Deprecated
    public void addContainers(Container... containers) {
        for (Container container : containers) {
            addElement(container);
        }
    }

    /**
     * Gets all {@link Layout}s for the container.
     * @return The current linked list of {@link Layout}s
     */
    public LinkedList<Layout> getLayouts() {
        LinkedList<Layout> toReturn = new LinkedList<>();
        elements.values().stream().filter(element -> element instanceof Layout).forEach(element -> {
            Layout layout = (Layout) element;
            toReturn.add(layout);
        });
        return toReturn;
    }

    /**
     * Adds a layout to the container. The added layout will be indexed to the front of the view.
     * @deprecated Use {@link #addElement(Element)} instead.
     * @param layout The {@link Layout} to be added.
     */
    @Deprecated
    public void addLayout(Layout layout) {
        addElement(layout);
    }

    /**
     * Adds the layout to the specific index. If there is an element already bound to that index it is shuffled forward.
     * @deprecated Use {@link #addElement(Element, int)} instead.
     * @param layout The {@link Layout} to add to the container.
     * @param index The index/order of the element.
     */
    @Deprecated
    public void addLayout(Layout layout, int index) {
        addElement(layout, index);
    }

    /**
     * Adds an array of {@link Layout}s to be added. The layouts are positioned by the order of the array.
     * The first layout of the array is the first to be added.
     * @deprecated Use {@link #addElements(Element...)} instead.
     * @param layouts An array {@link Layout}s to be added.
     */
    @Deprecated
    public void addLayouts(Layout... layouts) {
        for (Layout layout : layouts) {
            addElement(layout);
        }
    }

    public void addStyleSheet(File file) {
        this.stylesheets.add(file);
    }

    public List<File> getStylesheets() {
        return stylesheets;
    }

    /* Generic methods */
    public LinkedList<Node> buildBase() {
        LinkedList<Node> toReturn = new LinkedList<>();
        int i = 0;
        for (Element element : elements.values()) {
            if (debug)
                RenLogger.LOGGER.info("{}: {}", i, element.toString());
            Node node = null;
            if (element instanceof Overlay overlay) {
                node = overlay.render();
            }
            if (element instanceof Layout layout) {
                node = layout.render(this);
            }
            if (node != null) {
                toReturn.add(node);
            }

            // Render sub-containers last
            if (element instanceof Container container) {
                toReturn.addAll(container.build().getValue());
            }
            i++;
        }

        return toReturn;
    }

    /**
     * Builds and assembles the container. Converts RenJava API into JavaFX.
     * @return An entry set where the key is the pane as a node. The value is the collection of nodes which the pane contains.
     */
    public abstract Map.Entry<Node, LinkedList<Node>> build();
}
