package me.piitex.renjava.gui.layouts;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.piitex.renjava.gui.Element;
import me.piitex.renjava.gui.overlay.Overlay;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class Layout {
    private final Pane pane; // Get's the pane type for javafx
    private double x;
    private double y;
    private int width, height;
    private double spacing;
    private boolean scrollbar = false;
    private final LinkedHashSet<Overlay> overlays = new LinkedHashSet<>();
    private final LinkedHashSet<Layout> childLayouts = new LinkedHashSet<>();
    private final LinkedHashSet<Pane> subPanes = new LinkedHashSet<>();

    protected Layout(Pane pane) {
        this.pane = pane;
    }
    
    public void render(Pane root) {
        for (Overlay overlay : getOverlays()) {
            // Check if the layout should be a scroll pane.
            if (isScrollbar()) {
                ScrollPane scrollPane = new ScrollPane(pane);
                BorderPane subRoot = new BorderPane(scrollPane);
                root.getChildren().add(subRoot); // Adds as border (could be wrong)
            }
            new Element(overlay).render(pane);
        }
        for (Pane sub : subPanes) {
            pane.getChildren().add(sub);
        }
        pane.setTranslateX(x);
        pane.setTranslateY(y);
        pane.setPrefSize(width, height);
        if (pane instanceof HBox hBox) {
            hBox.setSpacing(spacing);
        } else if (pane instanceof VBox vBox) {
            vBox.setSpacing(spacing);
        }

        root.getChildren().add(pane);

        for (Layout child : childLayouts) {
            child.render(pane);
        }
    }

    public Pane getPane() {
        return pane;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    public boolean isScrollbar() {
        return scrollbar;
    }

    public void setScrollbar(boolean scrollbar) {
        this.scrollbar = scrollbar;
    }

    public Collection<Overlay> getOverlays() {
        return overlays;
    }

    public void addOverlays(Overlay... overlays) {
        this.overlays.addAll(List.of(overlays));
    }

    public void addChildLayout(Layout layout) {
        this.childLayouts.add(layout);
    }

    public LinkedHashSet<Layout> getChildLayouts() {
        return childLayouts;
    }

    public LinkedHashSet<Pane> getSubPanes() {
        return subPanes;
    }

    public void addSubPane(Pane pane) {
        subPanes.add(pane);
    }
}
