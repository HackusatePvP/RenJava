package me.piitex.renjava.events.types;

import javafx.scene.input.MouseEvent;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.overlay.Overlay;

public class OverlayExitEvent extends Event {
    private final Overlay overlay;
    private final MouseEvent event;

    public OverlayExitEvent(Overlay overlay, MouseEvent event) {
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
