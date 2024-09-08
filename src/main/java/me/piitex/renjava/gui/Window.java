package me.piitex.renjava.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.utils.KeyUtils;
import me.piitex.renjava.tasks.Tasks;
import me.piitex.renjava.utils.ModifierKeyList;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Window {
    private final String title;
    private final ImageLoader icon;
    private final StageStyle stageStyle;
    private int width, height;
    // Used for scaling the window when it resizes.
    private Color backgroundColor = Color.BLACK;
    private Stage stage;
    private Scene scene;
    private Pane root;

    // Time tracking for thresholds
    private Instant lastRun;
    private final LinkedList<Container> containers = new LinkedList<>();

    public Window(String title, StageStyle stageStyle, ImageLoader icon) {
        this.width = RenJava.getInstance().getConfiguration().getWidth();
        this.height = RenJava.getInstance().getConfiguration().getHeight();
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

    public Window(String title, Color backgroundColor, StageStyle stageStyle, ImageLoader icon) {
        this.width = RenJava.getInstance().getConfiguration().getWidth();
        this.height = RenJava.getInstance().getConfiguration().getHeight();
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.stageStyle = stageStyle;
        this.icon = icon;
        buildStage();
    }

    public Window(String title, Color backgroundColor, StageStyle stageStyle, ImageLoader icon, int width, int height) {
        this.title = title;
        this.backgroundColor = backgroundColor;
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
        stage.setWidth(width);
        stage.setHeight(height);


        root = new Pane();

        root.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(root);

        scene.setFill(Color.BLACK);

        stage.setScene(scene);
    }

    public void updateBackground(Color color) {
        this.backgroundColor = color;
        root.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        stage.getScene().setFill(color);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
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

    public void setMaximized(boolean maximized) {
        if (stage != null) {
            stage.setMaximized(maximized);
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

    public void buildAndRender() {
        buildStage();
        render();
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

        root.requestFocus();
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

    public void handleInput(Pane root) {
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

        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (Arrays.stream(ModifierKeyList.modifier).anyMatch(keyCode -> keyCode == event.getCode())) {

                // Slight issue with alt-tabbing. The tab key will never be recognized nor will the release event. This causes infinite looping.
                // To fix this, if they push down a different modifier set the last one to false to end the loop.
                // If you tab out for a long period of time your pc will probably slow down.
                // TODO: Create kill method if they hold longer than 2 or 3 minutes.
                KeyCode keyCode = KeyUtils.getCurrentKeyDown();
                if (keyCode != event.getCode()) {
                    KeyUtils.setModifierDown(keyCode, false);
                }

                if (keyCode == null) {
                    // Engine says key was not held before but it is now.
                    // Update engine
                    KeyUtils.setModifierDown(event.getCode(), true); // So far it is down.

                    // Start Sub-thread for continous event
                    Tasks.runAsync(() -> {
                        while (KeyUtils.getCurrentKeyDown() != null) {
                            // Add delay threshold
                            Instant current = Instant.now();
                            if (lastRun == null) {
                                Tasks.runJavaFXThread(() -> {
                                    KeyPressEvent event1 = new KeyPressEvent(event); // Might not pass
                                    RenJava.callEvent(event1);
                                });
                                lastRun = current;
                            } else {
                                long diff = Duration.between(lastRun, current).toMillis();
                                if (diff > 100) {
                                    Tasks.runJavaFXThread(() -> {
                                        KeyPressEvent event1 = new KeyPressEvent(event); // Might not pass
                                        RenJava.callEvent(event1);
                                    });
                                    lastRun = current;
                                }
                            }
                        }
                    });
                }
            } else {
                KeyPressEvent pressEvent = new KeyPressEvent(event);
                RenJava.callEvent(pressEvent);
            }
        });


        root.addEventFilter(KeyEvent.KEY_RELEASED,event -> {

            if (Arrays.stream(ModifierKeyList.modifier).anyMatch(keyCode -> keyCode == event.getCode())) {
                // If CTRL is being set to down set to false to stop the while thread
                if (KeyUtils.getCurrentKeyDown() != null) {
                    KeyUtils.setModifierDown(event.getCode(), false);
                    KeyReleaseEvent releaseEvent = new KeyReleaseEvent(event);
                    RenJava.callEvent(releaseEvent);
                }
            } else {
                KeyReleaseEvent releaseEvent = new KeyReleaseEvent(event);
                RenJava.callEvent(releaseEvent);
            }
        });
    }
}