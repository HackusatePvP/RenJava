package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.overlays.events.IOverlayClick;
import me.piitex.renjava.gui.overlays.events.IOverlayClickRelease;
import me.piitex.renjava.gui.overlays.events.IOverlayHover;
import me.piitex.renjava.gui.overlays.events.IOverlayHoverExit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * An overlay is a visual element which can be rendered. The overlay class is the JavaFX equivalent of a {@link Node}.
 * All overlays have generic events that are fired. For example, the {@link OverlayClickEvent} is fired if the overlay is clicked.
 *
 * <p>
 * To render an overlay you first need to add it to a {@link Container}. The container will have to be managed to a {@link Window}.
 * The window is used to render the screen.
 * <pre>
 *     {@code
 *       // Create the overlay
 *       TextOverlay overlay = new TextOverlay("Text");
 *
 *       // Create or fetch the container.
 *       Container container = new EmptyContainer(x, y, width, height, displayOrder);
 *
 *       // Add the overlay to the container.
 *       container.addOverlay(overlay);
 *
 *       // Add the container to the window if needed.
 *       window.addContainer(container);
 *
 *       // Render the screen
 *       window.render();
 *     }
 * </pre>
 *
 * Handling overlay events are key to creation a functional game. During the rendering process, logical programming must be executed with events.
 * <pre>
 *     {@code
 *       // Create the overlay
 *       TextOverlay overlay = new TextOverlay("Text");
 *
 *       // Handle code when the overlay is clicked.
 *       overlay.onClick(event -> {
 *          // Handle logic
 *          System.out.println("The overlay was clicked!");
 *       });
 *     }
 * </pre>
 */
public abstract class Overlay {
    private double x,y;
    private double scaleX, scaleY;
    private DisplayOrder order = DisplayOrder.NORMAL;

    private IOverlayHover iOverlayHover;
    private IOverlayHoverExit iOverlayHoverExit;
    private IOverlayClick iOverlayClick;
    private IOverlayClickRelease iOverlayClickRelease;

    private final Collection<Transitions> transitions = new HashSet<>();

    private final List<File> styleSheets = new ArrayList<>();

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

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public DisplayOrder getOrder() {
        return order;
    }

    public void setOrder(DisplayOrder order) {
        this.order = order;
    }

    public void onClick(IOverlayClick iOverlayClick) {
        this.iOverlayClick = iOverlayClick;
    }

    public void onHover(IOverlayHover iOverlayHover) {
        this.iOverlayHover = iOverlayHover;
    }

    public void onClickRelease(IOverlayClickRelease iOverlayClickRelease) {
        this.iOverlayClickRelease = iOverlayClickRelease;
    }

    public void onHoverExit(IOverlayHoverExit iOverlayHoverExit) {
        this.iOverlayHoverExit = iOverlayHoverExit;
    }

    public IOverlayClick getOnClick() {
        return iOverlayClick;
    }

    public IOverlayHover getOnHover() {
        return iOverlayHover;
    }

    public IOverlayHoverExit getOnHoverExit() {
        return iOverlayHoverExit;
    }

    public IOverlayClickRelease getOnRelease() {
        return iOverlayClickRelease;
    }

    public Collection<Transitions> getTransitions() {
        return transitions;
    }

    public List<File> getStyleSheets() {
        return styleSheets;
    }

    public void addStyleSheet(File file) {
        this.styleSheets.add(file);
    }

    public Overlay addTransition(Transitions transitions) {
        this.transitions.add(transitions);
        return this;
    }

    /**
     * Converts the overlay into a {@link Node} which is used for the JavaFX API.
     * @return The converted {@link Node} for the overlay.
     */
    public abstract Node render();

    public void renderTransitions(Node node) {
        for (Transitions transitions1 : getTransitions()) {
            transitions1.play(node);
        }
    }

    public void setInputControls(Node node) {
        if (node.getOnDragEntered() == null) {
            node.setOnMouseEntered(event -> {
                RenJava.getEventHandler().callEvent(new OverlayHoverEvent(this, event));
            });
        }
        if (node.getOnMouseClicked() == null) {
            node.setOnMouseClicked(event -> {
                MouseClickEvent clickEvent = new MouseClickEvent(event);
                OverlayClickEvent overlayClickEvent = new OverlayClickEvent(this, event);
                RenJava.getEventHandler().callEventChain(clickEvent, overlayClickEvent);
            });
        }
        if (node.getOnMouseExited() == null) {
            node.setOnMouseExited(event -> {
                RenJava.getEventHandler().callEvent(new OverlayExitEvent(this, event));
            });
        }
        if (node.getOnMouseReleased() == null) {
            node.setOnMouseReleased(event -> {
                RenJava.getEventHandler().callEvent(new OverlayClickReleaseEvent(this, event));
            });
        }
    }
}
