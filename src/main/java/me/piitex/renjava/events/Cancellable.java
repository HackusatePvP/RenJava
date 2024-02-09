package me.piitex.renjava.events;

public interface Cancellable {

    void setCancelled(boolean cancelled);
    boolean isCancelled();
}
