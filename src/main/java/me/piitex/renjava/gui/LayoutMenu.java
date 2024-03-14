package me.piitex.renjava.gui;

import javafx.scene.Node;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlay.Overlay;

import java.util.LinkedHashSet;

public abstract class LayoutMenu {
    private final Menu menu;
    private final int width, height;
    private int x, y;

    private final LinkedHashSet<Node> nodes = new LinkedHashSet<>();
    private final LinkedHashSet<Layout> layouts = new LinkedHashSet<>();
    private final LinkedHashSet<Overlay> overlays = new LinkedHashSet<>();
    private final LinkedHashSet<Menu> children = new LinkedHashSet<>();

    public LayoutMenu(Menu menu, int width, int height) {
        this.menu = menu;
        this.width = width;
        this.height = height;
        menu.addParent(this);
    }

    public Menu getMenu() {
        return menu;
    }

    public abstract void render();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public LinkedHashSet<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public LinkedHashSet<Layout> getLayouts() {
        return layouts;
    }

    public LayoutMenu addLayout(Layout layout) {
        layouts.add(layout);
        return this;
    }

    public LinkedHashSet<Overlay> getOverlays() {
        return overlays;
    }

    public LayoutMenu addOverlay(Overlay overlay) {
        overlays.add(overlay);
        return this;
    }

    public LinkedHashSet<Menu> getChildren() {
        return children;
    }

    /* Rendering functions */

    public LayoutMenu addMenu(Menu menu) {
        this.children.add(menu);
        return this;
    }
}
