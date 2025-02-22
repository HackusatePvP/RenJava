package me.piitex.renjava.gui.containers;

import javafx.scene.Node;
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

/**
 * The EmptyContainer is a default {@link Container} which houses {@link Overlay}s, {@link Layout}s, and sub-containers.
 * The container must be added to a {@link Window} to be rendered.
 * <pre>
 *     {@code
 *       EmptyContainer container = new EmptyContainer(x, y, width, height, displayOrder);
 *       window.addContainer(container);
 *       window.render();
 *     }
 * </pre>
 * @see Container
 * @see Layout
 * @see Overlay
 * @see Window
 */
public class EmptyContainer extends Container {

    public EmptyContainer(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public EmptyContainer(double x, double y, double width, double height, DisplayOrder order) {
        super(x, y, width, height, order);
    }

    public EmptyContainer(double width, double height) {
        super(0, 0, width, height);
    }

    public EmptyContainer(double width, double height, DisplayOrder order) {
        super(0, 0, width, height, order);
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> build() {
        Pane pane = new Pane();
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.setPrefSize(getWidth(), getHeight());

        LinkedList<Node> lowOrder = new LinkedList<>();
        LinkedList<Node> normalOrder = new LinkedList<>();
        LinkedList<Node> highOrder = new LinkedList<>();

        buildBase(lowOrder, normalOrder, highOrder);

        // Return loworder because the other orders are added onto the low order.
        return new AbstractMap.SimpleEntry<>(pane, lowOrder);
    }
}
