package me.piitex.renjava.events.types;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.overlays.Overlay;

public class OverlayClickEvent extends Event {
    private final Overlay overlay;
    private final MouseEvent event;
    private boolean handleMouseEvent = true;

    public OverlayClickEvent(Overlay overlay, MouseEvent event) {
        this.overlay = overlay;
        this.event = event;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public MouseEvent getHandler() {
        return event;
    }

    public boolean isRightClicked() {
        return event.getButton() == MouseButton.SECONDARY;
    }

    public boolean isMiddleButton() {
        return event.getButton() == MouseButton.MIDDLE;
    }

    public boolean isHandleMouseEvent() {
        return handleMouseEvent;
    }

    public void setHandleMouseEvent(boolean handleMouseEvent) {
        this.handleMouseEvent = handleMouseEvent;
    }
}
