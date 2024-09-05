package me.piitex.renjava.events.types;

import javafx.scene.input.MouseEvent;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.overlays.Overlay;

public class OverlayClickEvent extends Event {
    private final Overlay overlay;
    private final MouseEvent event;


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
}
