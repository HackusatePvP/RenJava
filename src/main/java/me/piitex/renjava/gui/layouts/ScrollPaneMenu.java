package me.piitex.renjava.gui.layouts;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.LayoutMenu;

public class ScrollPaneMenu extends LayoutMenu {

    public ScrollPaneMenu(Menu menu, int width, int height) {
        super(menu, width, height);
    }

    @Override
    public void render() {
        Pane pane = getMenu().getPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(getWidth(), getHeight());
        scrollPane.setTranslateX(getX());
        scrollPane.setTranslateY(getY());

        pane.getChildren().add(scrollPane); // Adds to menu
    }
}
