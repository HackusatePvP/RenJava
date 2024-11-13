package me.piitex.renjava.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.Overlay;
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

/**
 * Window is the main GUI component which handle the rendering process for the engine. There are three components to windows which are {@link Container}, {@link Overlay}, {@link Layout}.
 * Window houses and manages these components.
 * <p>
 * You can create and render multiple windows at once. The title is used for the process name and label in the top left corner.
 * The stage style is used to control how the window is displayed. A decorated style will contain a "X", minimize, and maximize button.
 * An undecorated style will not contain any top bar similar to a full-screen game.
 * <pre>
 * {@code
 * Window window = new Window("Window Title", StageStyle.DECORATED, new ImageLoader("path/to/icon.png"));
 * }
 * </pre>
 * <p>
 * To display various elements to a window you must create a container first. Once the container is created you simply have to add it the window.
 * Note, you can add and position multiple containers to a single window.
 * <pre>
 * {@code
 *  Window window = application.getWindow();
 *  Container container = new EmptyContainer(x, y, width, height);
 *  window.addContainer(container);
 * }
 * </pre>
 * <p>
 * There is no game loop which handles rendering. All the rendering is handled by JavaFX which does it automatically when a node is modified.
 * To change or render a different container, you must remove the current containers and re-render the window.
 * <pre>
 * {@code
 *  Window window = application.getWindow();
 *  window.clearContainers(); // Clear existing containers
 *
 *  window.addContainer(newContainer);
 *
 *  window.render(); // Process newly added container
 * }
 * </pre>
 * <p>
 * RenJava framework handles the game window which you can access via the {@link RenJava} class.
 * It is recommended to have your own application instance but isn't necessarily required.
 * You can modify the game window at any point pass the initial loading stage.
 * <pre>
 * {@code
 *  Window gameWindow = RenJava.getInstance().getGameWindow();
 * }
 * </pre>
 * <p>
 * All GUI related functions must be called in the JavaFX thread. You can use the {@link Tasks} utility to switch between different threads.
 * <pre>
 * {@code
 *  Tasks.runAsync(() -> {
 *      // Some code to be ran asynchronously
 *
 *      // Handle JavaFX in async
 *      Tasks.runJavaFXThread(() -> {
 *          // Gui related code
 *          window.render();
 *      })
 *  })
 * }
 * </pre>
 *
 * @see Container
 * @see Overlay
 * @see Layout
 * @see Tasks
 * @see RenJava#getGameWindow()
 */
public class Window {
    private final String title;
    private final ImageLoader icon;
    private final StageStyle stageStyle;
    private int width, height;
    private boolean fullscreen = false, maximized = false;
    private Color backgroundColor = Color.BLACK;
    private Stage stage;
    private Scene scene;
    private Pane root;

    // Time tracking for thresholds
    private Instant lastRun;
    private Instant firstRun;
    private boolean captureInput = true;

    private final LinkedList<Container> containers = new LinkedList<>();

    public Window(String title, StageStyle stageStyle, ImageLoader icon) {
        this.width = RenJava.getInstance().getConfiguration().getWidth();
        this.height = RenJava.getInstance().getConfiguration().getHeight();
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        buildStage();
    }

    public Window(String title, StageStyle stageStyle, ImageLoader icon, boolean captureInput) {
        this.width = RenJava.getInstance().getConfiguration().getWidth();
        this.height = RenJava.getInstance().getConfiguration().getHeight();
        this.captureInput = captureInput;
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        buildStage();
    }

    public Window(String title, StageStyle stageStyle, boolean fullscreen, boolean maximized, ImageLoader icon) {
        this.width = RenJava.getInstance().getConfiguration().getWidth();
        this.height = RenJava.getInstance().getConfiguration().getHeight();
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.setFullscreen(fullscreen);
        this.setMaximized(maximized);
        buildStage();
    }

    public Window(String title, StageStyle stageStyle, boolean fullscreen, boolean maximized, boolean captureInput, ImageLoader icon) {
        this.width = RenJava.getInstance().getConfiguration().getWidth();
        this.height = RenJava.getInstance().getConfiguration().getHeight();
        this.captureInput = captureInput;
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.setFullscreen(fullscreen);
        this.setMaximized(maximized);
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

    public Window(String title, StageStyle stageStyle, ImageLoader icon, int width, int height, boolean captureInput) {
        this.title = title;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.captureInput = captureInput;
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

    public Window(String title, Color backgroundColor, StageStyle stageStyle, ImageLoader icon, boolean captureInput) {
        this.width = RenJava.getInstance().getConfiguration().getWidth();
        this.height = RenJava.getInstance().getConfiguration().getHeight();
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.captureInput = captureInput;
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

    public Window(String title, Color backgroundColor, StageStyle stageStyle, ImageLoader icon, int width, int height, boolean captureInput) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.stageStyle = stageStyle;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.captureInput = captureInput;
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
        stage.setMaximized(maximized);
        stage.setFullScreen(fullscreen);


        root = new Pane();

        root.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(root);

        scene.setFill(Color.BLACK);

        stage.setScene(scene);
        if (captureInput) {
            handleStageInput(stage);
        }
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

    public boolean hasCaptureInput() {
        return captureInput;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        if (stage != null) {
            stage.setFullScreen(fullscreen);
            if (!fullscreen) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        }
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
        if (stage != null) {
            stage.setMaximized(maximized);
            if (!maximized) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
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
        System.gc();
    }

    public void clearContainer(int index) {
        containers.remove(index);

        // Re-render
        render();
    }

    public LinkedList<Container> getContainers() {
        return containers;
    }

    // Clears and resets current window.
    public void clear() {
        clearContainers();
        this.root = new Pane();
        this.scene = new Scene(root);
        this.stage.setScene(scene);
    }

    public void close() {
        if (stage != null) {
            stage.close();
            System.gc(); // Force garbage collection once the window is closed.
        }
    }

    public void buildAndRender() {
        buildStage();
        render();
    }

    // Builds and renders all containers
    public void render() {
        // Clear and reset before rendering (this will prevent elements being stacked)
        if (containers.isEmpty()) {
            RenLogger.LOGGER.error("You must add containers to the window before every render call.");
        }

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

        // Not sure if this will cause issues but to reduce resource usage the mappings need to be cleared
        lowOrder.clear();
        normalOrder.clear();
        highOrder.clear();

        stage.show();

        // Force clear resources that are unused.
        // To those who feel like GC is bad practice or indicates broken code allow me to explain.
        // Garbage is automatically collected and deleted by the JVM which is good enough for most cases.
        // HOWEVER, when you are rendering and loading multiple 10mb+ images within a 5 minute time period auto GC is far too slow.
        // This call may not do anything at all at times. It tells the JVM that I want to clear any unused references pronto not when it wants to.
        // There are multiple gc calls within the framework and when testing on my own machine they dramatically decrease resource usage by 300mb+
        // I will admit that there may be in a memory leak somewhere in the framework, but this is not the solution to that.
        //
        // TL;DR I ain't waiting for your slow ass jvm to clear resources.
        System.gc();

    }

    // Renders container on top of current window
    public void render(Container container) {
        renderContainer(container);
        stage.show();
    }

    private void renderContainer(Container container) {
        Map.Entry<Node, LinkedList<Node>> entry = container.build();
        Node node = entry.getKey();

        node.prefHeight(container.getHeight());
        node.prefWidth(container.getWidth());
        node.setTranslateX(container.getX());
        node.setTranslateY(container.getY());


        for (Node n : entry.getValue()) {
            if (node instanceof Pane pane) {
                if (!pane.getChildren().contains(n)) {
                    pane.getChildren().add(n);
                }
            }
            // Different pane types
        }

        if (!getRoot().getChildren().contains(node)) {
            getRoot().getChildren().add(node);
        }

        ContainerRenderEvent renderEvent = new ContainerRenderEvent(container, node);
        RenJava.callEvent(renderEvent);

    }

    private void handleStageInput(Stage stage) {
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            MouseClickEvent clickEvent = new MouseClickEvent(event);
            RenJava.callEvent(clickEvent);
        });
        stage.addEventFilter(ScrollEvent.SCROLL, event -> {
            double y = event.getDeltaY();
            if (y > 0) {
                // Scroll up
                ScrollUpEvent scrollUpEvent = new ScrollUpEvent();
                RenJava.callEvent(scrollUpEvent);
            } else {
                ScrollDownEvent downEvent = new ScrollDownEvent();
                RenJava.callEvent(downEvent);
            }
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (Arrays.stream(ModifierKeyList.modifier).anyMatch(keyCode -> keyCode == event.getCode())) {
                KeyCode keyCode = KeyUtils.getCurrentKeyDown();
                if (keyCode != event.getCode()) {
                    KeyUtils.setModifierDown(keyCode, false);
                }

                if (keyCode == null) {
                    // Engine says key was not held before but it is now.
                    // Update engine
                    KeyUtils.setModifierDown(event.getCode(), true); // So far it is down.

                    // Start Sub-thread for continuous event
                    Tasks.runAsync(() -> {
                        firstRun = Instant.now();
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
                                long firstDiff = Duration.between(firstRun, current).toMinutes();
                                if (firstDiff > 20) { // This broke randomly???
                                    RenLogger.LOGGER.warn("Modifier key was held for 2 minutes. Killing task...");
                                    KeyUtils.setModifierDown(event.getCode(), false);
                                    return; // Kill after 2min
                                }
                                if (diff > 75) {
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


        stage.addEventFilter(KeyEvent.KEY_RELEASED,event -> {

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