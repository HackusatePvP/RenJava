package me.piitex.renjava.events.types;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.events.Event;

public class GameStartEvent extends Event {
    private final RenJava renJava;

    public GameStartEvent(RenJava renJava) {
        this.renJava = renJava;
    }

    public RenJava getRenJava() {
        return renJava;
    }
}
