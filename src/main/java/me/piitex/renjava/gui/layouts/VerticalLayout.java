package me.piitex.renjava.gui.layouts;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.overlays.Overlay;

public class VerticalLayout extends Layout {
    private double spacing = 10;

    public VerticalLayout(double width, double height) {
        super(new VBox(), width, height);
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    @Override
    public Pane render(Container container) {
        // Clear
        VBox pane = (VBox) getPane();
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

        System.out.println("Layout size: " + getPane().getChildren().size());

        return getPane();
    }
}
