package me.piitex.renjava.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.events.types.ContainerRenderEvent;
import me.piitex.renjava.events.types.MouseClickEvent;
import me.piitex.renjava.events.types.ScrollDownEvent;
import me.piitex.renjava.events.types.ScrollUpEvent;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.loggers.RenLogger;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Window {
    private final String title;
    private final ImageLoader icon;
    private final StageStyle stageStyle;
    private int width, height;
    private Stage stage;
    private Scene scene;
    private Pane root;
    private final LinkedList<Container> containers = new LinkedList<>();

    public Window(String title, StageStyle stageStyle, ImageLoader icon) {
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        buildStage();
    }

    public Window(String title, StageStyle stageStyle, ImageLoader icon, int width, int height) {
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.width = width;
        this.height = height;
        buildStage();
    }

    protected void buildStage() {
        stage = new Stage();

        if (icon != null) {
            Image windowIcon = null;
            try {
                windowIcon = icon.buildRaw();
            } catch (ImageNotFoundException e) {
                RenLogger.LOGGER.error(e.getMessage(), e);
                RenJava.writeStackTrace(e);
            }
            if (windowIcon != null) {
                stage.getIcons().add(windowIcon);
            }
        }

        stage.setTitle(title);
        stage.initStyle(stageStyle);
        if (width == 0 && height == 0) {
            stage.setMaximized(true);
        } else {
            stage.setWidth(width);
            stage.setHeight(height);
        }

        root = new Pane();

        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(root);

        scene.setFill(Color.BLACK);

        stage.setScene(scene);
    }

    public void updateBackground(Color color) {
        root.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        stage.getScene().setFill(color);
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public Pane getRoot() {
        return root;
    }

    public void setFullscreen(boolean fullscreen) {
        if (stage != null) {
            stage.setFullScreen(fullscreen);
        }
    }

    public void addContainer(Container container) {
        this.containers.add(container);
    }

    public void addContainers(Container... containers) {
        this.containers.addAll(List.of(containers));
    }

    public void clearContainers() {
        containers.clear();
    }

    public void close() {
        if (stage != null) {
            stage.close();
        }
    }


    // Builds and renders all containers
    public void render() {
        // Clear and reset before rendering (this will prevent elements being stacked)
        RenLogger.LOGGER.debug("Rendering window...");

        root.getChildren().clear();

        // Gather orders
        LinkedList<Container> lowOrder = new LinkedList<>();
        LinkedList<Container> normalOrder = new LinkedList<>();
        LinkedList<Container> highOrder = new LinkedList<>();

        for (Container container : containers) {
            if (container.getOrder() == DisplayOrder.LOW) {
                lowOrder.add(container);
            } else if (container.getOrder() == DisplayOrder.NORMAL) {
                normalOrder.add(container);
            } else if (container.getOrder() == DisplayOrder.HIGH) {
                highOrder.add(container);
            }
        }


        lowOrder.forEach(this::renderContainer);
        normalOrder.forEach(this::renderContainer);
        highOrder.forEach(this::renderContainer);

        handleInput(root);

        stage.show();
    }

    private void renderContainer(Container container) {
        Map.Entry<Node, LinkedList<Node>> entry = container.render();
        Node node = entry.getKey();
        node.prefHeight(container.getHeight());
        node.prefWidth(container.getWidth());
        node.setTranslateX(container.getX());
        node.setTranslateY(container.getY());

        for (Node n : entry.getValue()) {
            if (node instanceof Pane pane) {
                pane.getChildren().add(n);
            }
        }

        getRoot().getChildren().add(node);

        ContainerRenderEvent renderEvent = new ContainerRenderEvent(container, node);
        RenJava.callEvent(renderEvent);
    }

    protected void handleInput(Pane root) {
        // Handle inputs
        root.setOnMouseClicked(mouseEvent -> {
            MouseClickEvent clickEvent = new MouseClickEvent(mouseEvent);
            RenJava.callEvent(clickEvent);
        });

        root.setOnScroll(scrollEvent -> {
            double y = scrollEvent.getDeltaY();
            if (y > 0) {
                // Scroll up
                ScrollUpEvent scrollUpEvent = new ScrollUpEvent();
                RenJava.callEvent(scrollUpEvent);
            } else {
                ScrollDownEvent downEvent = new ScrollDownEvent();
                RenJava.callEvent(downEvent);
            }
        });
    }

}
