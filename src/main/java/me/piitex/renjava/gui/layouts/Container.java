package me.piitex.renjava.gui.layouts;

import javafx.stage.Stage;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.layouts.Layout;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

/**
 * Main default layout for guis and scenes.
 */
public abstract class Container {
    private Stage stage;
    private boolean ui;

    private final Collection<Layout> layouts = new HashSet<>();

    public void addLayout(Layout layout) {
        layouts.add(layout);
    }

    public Collection<Layout> getLayouts() {
        return layouts;
    }

    public abstract Menu build(boolean ui);

    public abstract void render(Menu menu);

    public Stage getStage() {
        return stage;
    }
}
