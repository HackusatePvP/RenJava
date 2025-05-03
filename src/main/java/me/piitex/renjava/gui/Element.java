package me.piitex.renjava.gui;

import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.Overlay;

/**
 * Represents a graphical element that can be rendered to the {@link Window} or a {@link Container}.
 * <p>
 * This is an abstract base class for all renderable elements in the GUI framework.
 * Elements are organized by their rendering index, which determines the order in which
 * they are drawn. A lower index means the element will be rendered earlier (underneath others).
 * </p>
 *
 * @see Container
 * @see Overlay
 * @see Layout
 */
public abstract class Element {
    private int index = 0;


    /**
     * Retrieves the rendering index of this element.
     *
     * @return The rendering index of the element. A lower value means the element is rendered earlier.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the rendering index of this element.
     * <p>
     * An index of 0 will cause the element to be rendered first (at the bottom layer).
     * Higher index values will render the element on top of those with lower indices.
     * </p>
     *
     * @param index The new rendering index for the element.
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
