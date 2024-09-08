package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.OverlayClickEvent;
import me.piitex.renjava.events.types.OverlayClickReleaseEvent;
import me.piitex.renjava.events.types.OverlayExitEvent;
import me.piitex.renjava.events.types.OverlayHoverEvent;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.overlays.events.IOverlayClick;
import me.piitex.renjava.gui.overlays.events.IOverlayClickRelease;
import me.piitex.renjava.gui.overlays.events.IOverlayHover;
import me.piitex.renjava.gui.overlays.events.IOverlayHoverExit;

import java.util.Collection;
import java.util.HashSet;

public abstract class Overlay {
    private double x,y;
    private double scaleX, scaleY;
    private DisplayOrder order = DisplayOrder.NORMAL;

    private IOverlayHover iOverlayHover;
    private IOverlayHoverExit iOverlayHoverExit;
    private IOverlayClick iOverlayClick;
    private IOverlayClickRelease iOverlayClickRelease;

    private Collection<Transitions> transitions = new HashSet<>();

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

    public void setOnclick(IOverlayClick iOverlayClick) {
        this.iOverlayClick = iOverlayClick;
    }

    public void setOnHover(IOverlayHover iOverlayHover) {
        this.iOverlayHover = iOverlayHover;
    }

    public void setOnClickRelease(IOverlayClickRelease iOverlayClickRelease) {
        this.iOverlayClickRelease = iOverlayClickRelease;
    }

    public void setOnHoverExit(IOverlayHoverExit iOverlayHoverExit) {
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

    public Overlay addTransition(Transitions transitions) {
        this.transitions.add(transitions);
        return this;
    }

    public abstract Node render();

    public void renderTransitions(Node node) {
        for (Transitions transitions1 : getTransitions()) {
            transitions1.play(node);
        }
    }

    public void setInputControls(Node node) {
        node.setOnMouseEntered(event -> {
            RenJava.callEvent(new OverlayHoverEvent(this, event));
        });
        node.setOnMouseClicked(event -> {
            RenJava.callEvent(new OverlayClickEvent(this, event));
        });
        node.setOnMouseExited(event -> {
            RenJava.callEvent(new OverlayExitEvent(this, event));
        });
        if (node.getOnMouseReleased() == null) {
            node.setOnMouseReleased(event -> {
                RenJava.callEvent(new OverlayClickReleaseEvent(this, event));
            });
        }
    }
}