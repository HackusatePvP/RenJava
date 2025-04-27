package me.piitex.renjava.gui;

import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.Overlay;

/**
 * An element is a class which can be rendered to the {@link Window} or {@link Container}
 * @see Container
 * @see Overlay
 * @see Layout
 */
public abstract class Element {
    private int index = 0;

    public int getIndex() {
        return index;
    }

    /**
     * Sets the rendering layer of the element. An index of 0 will make the element be rendered first.
     * @param index The index layer of the element.
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
