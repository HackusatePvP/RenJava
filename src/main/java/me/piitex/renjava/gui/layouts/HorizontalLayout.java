package me.piitex.renjava.gui.layouts;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;

public class HorizontalLayout extends Layout {
    private int spacing;
    private final HBox pane;

    public HorizontalLayout(double width, double height) {
        super(new HBox(), width, height);
        this.pane = (HBox) getPane();
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public Node render() {
        // Clear
        VBox.setVgrow(pane, Priority.ALWAYS);
        pane.setSpacing(getSpacing());
        pane.setTranslateX(getX());
        pane.setTranslateY(getY());
        pane.getChildren().clear();
        if (getWidth() > 0) {
            pane.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            pane.setMinHeight(getHeight());
        }

        if (getPrefWidth() > 0) {
            pane.setPrefWidth(getPrefWidth());
        }
        if (getPrefHeight() > 0) {
            pane.setPrefHeight(getPrefHeight());
        }

        if (getMaxWidth() > 0) {
            pane.setMaxWidth(getMaxWidth());
        }
        if (getMaxHeight() > 0) {
            pane.setMaxHeight(getMaxHeight());
        }
        if (getAlignment() != null) {
            pane.setAlignment(getAlignment());
        }
        if (getBackgroundColor() != null) {
            pane.setBackground(new Background(new BackgroundFill(getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }


        setStyling(pane);
        return pane;
    }
}
