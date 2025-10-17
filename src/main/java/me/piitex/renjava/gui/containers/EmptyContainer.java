package me.piitex.renjava.gui.containers;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.Overlay;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The EmptyContainer is a default {@link Container} which houses {@link Overlay}s, {@link Layout}s, and sub-containers.
 * The container must be added to a {@link Window} to be rendered.
 * <pre>
 *     {@code
 *       EmptyContainer container = new EmptyContainer(x, y, width, height, index);
 *       window.addContainer(container);
 *     }
 * </pre>
 * @see Container
 * @see Layout
 * @see Overlay
 * @see Window
 */
public class EmptyContainer extends Container {
    private final Pane pane;

    public EmptyContainer(double x, double y, double width, double height, int index) {
        super(new Pane(), x, y, width, height, index);
        this.pane = (Pane) getNode();
    }

    public EmptyContainer(double x, double y, double width, double height) {
        this(x, y, width, height, 0);
    }

    public EmptyContainer(double width, double height) {
        this(0, 0, width, height, 0);
    }

    public EmptyContainer(double width, double height, int index) {
        this(0, 0, width, height, index);
    }

    public Pane getPane() {
        return pane;
    }

    @Override
    public Node build() {
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        if (getWidth() > 0) {
            pane.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            pane.setMinHeight(getHeight());
        }

        if (getPrefWidth() > 0) {
            pane.setPrefWidth(getPrefWidth());
        }
        if (getPrefHeight() > 0) {
            pane.setPrefHeight(getPrefHeight());
        }

        if (getMaxWidth() > 0) {
            pane.setMaxWidth(getMaxWidth());
        }
        if (getMaxHeight() > 0) {
            pane.setMaxHeight(getMaxHeight());
        }

        setStyling(pane);

        return pane;
    }
}
