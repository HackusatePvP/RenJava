package me.piitex.renjava.events.types;

import javafx.scene.input.MouseEvent;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.overlay.Overlay;

public class OverlayDragEvent extends Event {
    private final Overlay overlay;
    private final MouseEvent mouseEvent;

    public OverlayDragEvent(Overlay overlay, MouseEvent mouseEvent) {
        this.overlay = overlay;
        this.mouseEvent = mouseEvent;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }
}
