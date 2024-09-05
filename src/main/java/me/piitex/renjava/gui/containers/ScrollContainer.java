package me.piitex.renjava.gui.containers;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.Overlay;

import java.util.LinkedList;
import java.util.Map;

public class ScrollContainer extends Container {
    private final Layout layout;
    private ScrollPane scrollPane;

    public ScrollContainer(Layout layout, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.layout = layout;
    }

    @Override
    public Map.Entry<Node, LinkedList<Node>> render() {
        return null;
    }

    //TODO: Do content types for scrollpane. For now its just a layout

//    @Override
//    public Node render(Window window) {
//        scrollPane = new ScrollPane();
//        scrollPane.setTranslateX(getX());
//        scrollPane.setTranslateY(getY());
//        scrollPane.setPrefSize(getWidth(), getHeight());
//        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
//        layout.addOverlays(getOverlays());
//        Pane pane = layout.getPane();
//        scrollPane.setContent(pane);
//
//        // When doing a scroll container render the base layout first then add all of the elements to that layout.
//        for (Overlay overlay : getOverlays()) {
//            layout.addOverlay(overlay);
//        }
//
//        for (Layout layout1 : getLayouts()) {
//            layout.addChildLayout(layout1);
//        }
//
//        layout.render(this);
//
//        // Next add to window render pipeline
//        window.getRoot().getChildren().add(scrollPane);
//
//        return scrollPane;
//    }
}
