package me.piitex.renjava.gui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.gui.overlays.Overlay;

import java.util.*;

/**
 * An element which handles rendering of {@link Container}, {@link Layout}, and {@link Overlay}
 *
 * @see Layout
 * @see Container
 */
public class Renderer extends Element {
    private final TreeMap<Integer, Element> elements = new TreeMap<>();
    private double width, height;
    private double prefWidth, prefHeight;
    private double maxWidth, maxHeight;
    private double xOffset = 0, yOffset = 0;
    private Color backgroundColor;
    private Color borderColor;
    private double borderWidth = 1;
    private final List<String> styles = new ArrayList<>();


    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        if (getNode() instanceof Region region) {
            region.setMinWidth(width);
        }
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;

        if (getNode() instanceof Region region) {
            region.setMinHeight(height);
        }
    }

    public double getPrefWidth() {
        return prefWidth;
    }

    public double getPrefHeight() {
        return prefHeight;
    }

    public void setPrefWidth(double prefWidth) {
        this.prefWidth = prefWidth;

        if (getNode() instanceof Region region) {
            region.setPrefWidth(height);
        }
    }

    public void setPrefHeight(double prefHeight) {
        this.prefHeight = prefHeight;

        if (getNode() instanceof Region region) {
            region.setPrefHeight(height);
        }
    }

    public void setPrefSize(double width, double height) {
        this.prefWidth = width;
        this.prefHeight = height;

        if (getNode() instanceof Region region) {
            region.setPrefSize(width, height);
        }
    }

    public double getMaxWidth() {
        return maxWidth;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxSize(double width, double height) {
        this.maxWidth = width;
        this.maxHeight = height;

        if (getNode() instanceof Region region) {
            region.setMaxSize(width, height);
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(double borderWidth) {
        this.borderWidth = borderWidth;
    }

    public List<String> getStyles() {
        return styles;
    }

    public void addStyle(String style) {
        styles.add(style);
    }

    public TreeMap<Integer, Element> getElements() {
        return elements;
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
            element.setIndex(index);
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
        element.setIndex(index);
        elements.put(index, element);

        Node node = element.assemble();
        element.setNode(node);

        if (element instanceof ImageOverlay imageOverlay) {
            System.out.println("Adding image: " + imageOverlay.getPath());
        }

        // Ensure JavaFX operations are being executed on the FXThread.
        addToView(node, index);
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
     * Adds a {@link LinkedList <Element>} of elements to the container. The elements are position by the order of the list.
     * @param elements The list of elements to be added.
     */
    public void addElements(LinkedList<Element> elements) {
        for (Element element : elements) {
            addElement(element);
        }
    }

    public void removeElement(int index) {
        removeElement(getElementAt(index));
    }

    public void removeElement(Element element) {
        elements.remove(element.getIndex());
        if (element instanceof Overlay overlay) {
            removeFromView(overlay.getNode());
        }
        if (element instanceof Renderer renderer) {
            removeFromView(renderer.getNode());
        }
    }

    public void removeAllElement(Element element) {
        LinkedHashMap<Integer, Element> toRemove = new LinkedHashMap<>(elements);
        toRemove.forEach((integer, e) -> {
            if (e == element) {
                removeElement(e);
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

    public void removeAllElements() {
        elements.clear();
        if (getNode() instanceof Pane pane) {
            pane.getChildren().clear();
        }
    }

    public void replaceElement(int index, Element element) {
        if (getNode() instanceof Pane pane) {
            pane.getChildren().remove(index);
            pane.getChildren().add(index, element.assemble());
            elements.replace(index, element);
        }
    }

    public void addToView(Node node, int index) {
        System.out.println("Adding '" + node.getClass().getName() + "' to '" + getNode().getClass().getName() + "' at slot '" + index + "'");
        if (getNode() instanceof Pane pane) {
            if (!pane.getChildren().contains(node)) {
                pane.getChildren().add(node);
            }
        } else {
            Throwable throwable = new Throwable();
            throwable.printStackTrace();
        }
    }

    public void removeFromView(Node node) {
        if (getNode() instanceof Pane pane) {
            pane.getChildren().remove(node);
        }
    }

    public double getOffsetX() {
        return xOffset;
    }

    public void setOffsetX(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getOffsetY() {
        return yOffset;
    }

    public void setOffsetY(double yOffset) {
        this.yOffset = yOffset;
    }

    public void setStyling(Node node) {
        node.getStyleClass().addAll(styles);
        if (node instanceof Region region) {
            StringBuilder inLineCss = new StringBuilder();
            if (backgroundColor != null) {
                inLineCss.append("-fx-background-color: ").append(cssColor(backgroundColor)).append("; ");
            }

            if (borderColor != null) {
                inLineCss.append("-fx-border-color: ").append(cssColor(borderColor)).append("; ");
                inLineCss.append("-fx-border-width: ").append(borderWidth).append(" ").append(borderWidth).append(" ").append(borderWidth).append(" ").append(borderWidth).append("; ");
                inLineCss.append("-fx-border-style: ").append("solid").append("; ");
            }

            region.setStyle(inLineCss.toString());
        }
    }

    private void updateOffsets(Node node) {
        if (getOffsetX() > 0 || getOffsetY() > 0) {
            node.setTranslateX(node.getTranslateX() + getOffsetX());
            node.setTranslateY(node.getTranslateY() + getOffsetY());
        }
    }

    // Helper css function
    private String cssColor(Color color) {
        return String.format("rgba(%d, %d, %d, %f)",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()),
                color.getOpacity());
    }


    @Override
    public Node assemble() {
        Node node = null;
        if (this instanceof Container container) {
            node = container.build();
        }
        if (this instanceof Layout layout) {
            node = layout.render();
        }

        // Assemble existing elements.
        if (node instanceof Pane pane) {
            for (Element element : getElements().values()) {
                Node child = element.assemble();
                if (element instanceof Overlay overlay) {
                    overlay.setNode(child);
                }
                if (!pane.getChildren().contains(child)) {
                    pane.getChildren().add(child);
                }
            }
        }
        return node;
    }
}
