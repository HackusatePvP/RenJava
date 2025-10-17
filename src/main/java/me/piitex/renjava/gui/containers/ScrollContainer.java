package me.piitex.renjava.gui.containers;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.layouts.Layout;

public class ScrollContainer extends Container {
    private final ScrollPane scrollPane;
    private final Layout layout;
    private double xOffset, yOffset;
    private boolean horizontalScroll = true;
    private boolean verticalScroll = true;
    private boolean scrollWhenNeeded = true;
    private boolean scrollToBottom = false;
    private boolean pannable = false;
    private double scrollPosition;

    public ScrollContainer(Layout layout, double x, double y, double width, double height) {
        super(new ScrollPane(), x, y, width, height);
        this.scrollPane = (ScrollPane) getNode();
        this.layout = layout;
    }

    public double getXOffset() {
        return xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public boolean isHorizontalScroll() {
        return horizontalScroll;
    }

    public void setHorizontalScroll(boolean horizontalScroll) {
        this.horizontalScroll = horizontalScroll;
    }

    public boolean isVerticalScroll() {
        return verticalScroll;
    }

    public void setVerticalScroll(boolean verticalScroll) {
        this.verticalScroll = verticalScroll;
    }

    public boolean isScrollWhenNeeded() {
        return scrollWhenNeeded;
    }

    public void setScrollWhenNeeded(boolean scrollWhenNeeded) {
        this.scrollWhenNeeded = scrollWhenNeeded;
    }

    public void setScrollPosition(double scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    public void setScrollToBottom(boolean scrollToBottom) {
        this.scrollToBottom = scrollToBottom;
    }

    public void setPannable(boolean pannable) {
        this.pannable = pannable;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public Layout getLayout() {
        return layout;
    }

    @Override
    public Node build() {
        scrollPane.setVvalue(scrollPosition);
        scrollPane.setTranslateX(getX());
        scrollPane.setTranslateY(getY());
        scrollPane.setPannable(pannable);
        if (getWidth() > 0) {
            scrollPane.setMinWidth(getWidth());
        }
        if (getHeight() > 0) {
            scrollPane.setMinHeight(getHeight());
        }

        if (getPrefWidth() > 0) {
            scrollPane.setPrefWidth(getPrefWidth());
        }
        if (getPrefHeight() > 0) {
            scrollPane.setPrefHeight(getPrefHeight());
        }

        if (getMaxWidth() > 0) {
            scrollPane.setMaxWidth(getMaxWidth());
        }
        if (getMaxHeight() > 0) {
            scrollPane.setMaxHeight(getMaxHeight());
        }
        if (verticalScroll) {
            scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
        } else if (scrollWhenNeeded) {
            scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        } else {
            scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        }
        if (horizontalScroll) {
            scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
        } else if (scrollWhenNeeded) {
            scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        } else {
            scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        }

        setStyling(scrollPane);

        // Build pane layout for the scroll content
        VBox pane = (VBox) layout.assemble();
        pane.setAlignment(layout.getAlignment());

        if (scrollToBottom) {
            pane.heightProperty().addListener(observable -> {
                scrollPane.setVvalue(1);
            });
        }

        scrollPane.setContent(pane);
        scrollPane.requestFocus();

        return scrollPane;
    }
}