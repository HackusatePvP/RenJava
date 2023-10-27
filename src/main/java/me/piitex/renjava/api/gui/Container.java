package me.piitex.renjava.api.gui;

import javafx.stage.Stage;
import me.piitex.renjava.api.gui.layouts.Layout;
import java.util.Collection;
import java.util.HashSet;

/**
 * Main default layout for guis and scenes.
 */
public abstract class Container {
   private final Collection<Layout> layouts = new HashSet<>();

    public void addLayout(Layout layout) {
        layouts.add(layout);
    }

    public Collection<Layout> getLayouts() {
        return layouts;
    }

    public abstract void build(Stage stage, boolean ui);
}
