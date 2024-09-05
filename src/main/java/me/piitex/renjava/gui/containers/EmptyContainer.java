package me.piitex.renjava.gui.containers;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.gui.overlays.Overlay;
import me.piitex.renjava.loggers.RenLogger;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

// A default container which contains no special layout. Completely normal container.
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
    public Map.Entry<Node, LinkedList<Node>> render() {
        RenLogger.LOGGER.info("Rendering container...");
        Pane pane = new Pane();
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.setPrefSize(getWidth(), getHeight());

//        //TODO: Add support for specific container order. This was done for the general window but not achieved for sub containers.
//        for (Container container : getContainers()) {
//            container.render(window);
//        }
//
//        for (Overlay overlay : getOverlays()) {
//            Node node = overlay.render();
//            pane.getChildren().add(node);
//        }
//
//        for (Layout layout : getLayouts()) {
//            pane.getChildren().add(layout.render(this));
//        }

        LinkedList<Node> lowOrder = new LinkedList<>();
        LinkedList<Node> normalOrder = new LinkedList<>();
        LinkedList<Node> highOrder = new LinkedList<>();


        RenLogger.LOGGER.info("Rendering overlays...");
        for (Overlay overlay : getOverlays()) {

            // Debugging
            if (overlay instanceof ImageOverlay imageOverlay) {
                RenLogger.LOGGER.info("Rendering image {}", imageOverlay.getFileName());
            }

            if (overlay.getOrder() == DisplayOrder.LOW) {
                lowOrder.add(overlay.render());
            } else if (overlay.getOrder() == DisplayOrder.NORMAL) {
                normalOrder.add(overlay.render());
            } else if (overlay.getOrder() == DisplayOrder.HIGH) {
                highOrder.add(overlay.render());
            }
        }

        RenLogger.LOGGER.info("Rendering layouts " + getLayouts().size());
        for (Layout layout : getLayouts()) {
            if (layout.getOrder() == DisplayOrder.LOW) {
                lowOrder.add(layout.render(this));
            } else if (layout.getOrder() == DisplayOrder.NORMAL) {
                normalOrder.add(layout.render(this));
            } else if (layout.getOrder() == DisplayOrder.HIGH) {
                highOrder.add(layout.render(this));
            }
        }



        lowOrder.addAll(normalOrder);
        lowOrder.addAll(highOrder);

        // Render sub containers
        for (Container container : getContainers()) {
            lowOrder.addAll(container.render().getValue()); // Might not work
        }


        //                                    base   ordered list of all nodes
        return new AbstractMap.SimpleEntry<>(pane, lowOrder);
    }
}
