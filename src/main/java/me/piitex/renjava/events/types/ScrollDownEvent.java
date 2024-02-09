package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Cancellable;
import me.piitex.renjava.events.Event;

public class ScrollDownEvent extends Event implements Cancellable {
    private boolean cancel = false;
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancel = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }
}
