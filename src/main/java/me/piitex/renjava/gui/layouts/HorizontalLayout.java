package me.piitex.renjava.gui.layouts;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.overlays.Overlay;

public class HorizontalLayout extends Layout {
    private int spacing;

    public HorizontalLayout(double width, double height) {
        super(new HBox(), width, height);
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public Pane render(Container container) {
        // Clear
        HBox pane = (HBox) getPane();
        pane.setSpacing(getSpacing());
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.getChildren().clear();

        for (Overlay overlay : getOverlays()) {
            Node node = overlay.render();
            pane.getChildren().add(node);
        }

        for (Layout layout : getChildLayouts()) {
            pane.getChildren().add(layout.render(container));
        }

        return getPane();
    }
}
