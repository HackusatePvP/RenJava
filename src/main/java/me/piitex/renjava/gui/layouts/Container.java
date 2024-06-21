package me.piitex.renjava.gui.layouts;

import javafx.stage.Stage;
import me.piitex.renjava.gui.Menu;

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

    public void render(Menu menu) {
        render(menu, true);
    }

    /**
     * Renders the specific {@link Menu} to the screen.
     * @param menu Menu to be rendered.
     * @param update If true, the stage will be updated.
     */
    public abstract void render(Menu menu, boolean update);

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
