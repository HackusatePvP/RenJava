package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;
import me.piitex.renjava.gui.overlays.Overlay;

public class OverlayRenderEvent extends Event {
    private final Overlay overlay;

    public OverlayRenderEvent(Overlay overlay) {
        this.overlay = overlay;
    }

    public Overlay getOverlay() {
        return overlay;
    }
}
