package me.piitex.renjava.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlays.Overlay;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Window serves as the primary GUI component, managing the rendering process for the engine.
 * It houses and manages three core components: {@link Container}, {@link Overlay}, and {@link Layout}.
 *
 * <p>Multiple windows can be created and rendered simultaneously. The window's title serves as its process name and label.
 * The stage style dictates the window's appearance, with {@link StageStyle#DECORATED} providing standard
 * window controls (close, minimize, maximize) and {@link StageStyle#UNDECORATED} removing the title bar
 * for a borderless experience, often used in full-screen applications.</p>
 *
 * <pre>{@code
 * Window window = new WindowBuilder("My Application Window")
 * .setStageStyle(StageStyle.UNDECORATED)
 * .setDimensions(800, 600)
 * .setBackgroundColor(Color.BLACK)
 * .build();
 * }</pre>
 *
 * <p>
 * To display elements within a window, a {@link Container} must first be created and added to the window.
 * Multiple containers can be added and positioned within a single window.</p>
 * <pre>{@code
 * Window window = application.getWindow();
 * Container container = new EmptyContainer(x, y, width, height);
 * window.addContainer(container);
 * }</pre>
 *
 * <p>
 * All GUI-related functions, especially those involving scene graph modifications,
 * must be executed on the JavaFX Application Thread.</p>
 * <pre>{@code
 *     new Thread( () -> {
 *         // Code to be ran asynchronously
 *         loadBackend();
 *
 *         Platform.runLater( () -> {
 *             // Any gui related code.
 *             initializeProgressIndicator();
 *         })
 *     })
 * }</pre>
 *
 * @see Container
 * @see Overlay
 * @see Layout
 * @see Platform#runLater(Runnable)
 */
public class Window {
    private final String title;
    private final ImageLoader icon;
    private final StageStyle stageStyle;
    private double initialWidth, initialHeight;
    private double width, height;
    private boolean fullscreen, maximized ;
    private Color backgroundColor;
    private Stage stage;
    private Scene scene;
    private Pane root;

    private final boolean scale;
    private final boolean focused;

    private TreeMap<Integer, Container> containers = new TreeMap<>();
    private Container currentPopup = null;

    /**
     * Constructs a Window instance using properties defined in a {@link WindowBuilder}.
     * This allows for a flexible and readable way to configure window properties.
     *
     * <p>Common styles include {@link StageStyle#DECORATED}, which provides standard
     * window controls, and {@link StageStyle#UNDECORATED} for a borderless window.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * WindowBuilder builder = new WindowBuilder()
     * .setStageStyle(StageStyle.UNDECORATED)
     * .setDimensions(800, 600)
     * .setBackgroundColor(Color.BLACK)
     * Window window = new Window(builder);
     *
     * // Add containers
     * window.addContainer(someContainer);
     * }</pre>
     * @param builder The {@link WindowBuilder} instance containing window configuration.
     */
    public Window(WindowBuilder builder) {
        this.title = builder.getTitle();
        this.stageStyle = builder.getStageStyle();
        this.root = builder.getRoot();
        this.icon = builder.getIcon();
        this.width = builder.getWidth();
        this.height = builder.getHeight();
        this.initialWidth = builder.getWidth();
        this.initialHeight = builder.getHeight();
        this.backgroundColor = builder.getBackgroundColor();
        this.fullscreen = builder.isFullscreen();
        this.maximized = builder.isMaximized();
        this.focused = builder.isFocused();
        this.scale = builder.isScale();
        buildStage();

        // Display stage.
        render();
    }

    /**
     * Initializes the JavaFX Stage with the configured properties from the `WindowBuilder`.
     * This method sets up the title, style, dimensions, icon, and initial scene.
     */
    protected void buildStage() {
        stage = new Stage();

        if (icon != null) {
            Image windowIcon = icon.build();
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

        root.setPrefSize(width, height);

        if (scale) {
            Scale scale = new Scale(getWidthScale(), getHeightScale(), 0, 0);
            root.getTransforms().setAll(scale);
        }

        handleWindowScaling(stage);

        scene = new Scene(root);

        stage.setScene(scene);
    }

    /**
     * Updates the background color of the window's root pane and scene.
     * @param color The new background color.
     */
    public void updateBackground(Color color) {
        this.backgroundColor = color;
        root.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        stage.getScene().setFill(color);
    }

    /**
     * Retrieves the current background color of the window.
     * @return The current background color.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Retrieves the JavaFX Stage associated with this window.
     * @return The current Stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Retrieves the JavaFX Scene associated with this window.
     * @return The current Scene.
     */
    public Scene getScene() {
        return scene;
    }
    /**
     * Retrieves the root Pane of the window's scene graph.
     * @return The root Pane.
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * Retrieves the configured width of the window.
     * @return The window width.
     */
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        this.initialWidth = width;
        stage.setWidth(width);
        root.getTransforms().clear();
    }

    public void setHeight(double height) {
        this.height = height;
        this.initialHeight = height;
        stage.setHeight(height);
        root.getTransforms().clear();
    }

    /**
     * Retrieves the configured height of the window.
     * @return The window height.
     */
    public double getHeight() {
        return height;
    }

    public double getWidthScale() {
        return width / initialWidth;
    }

    public double getHeightScale() {
        return height / initialHeight;
    }

    /**
     * Toggles the window between full-screen and windowed modes.
     * @param fullscreen True to set to full-screen, false for windowed.
     */
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

    /**
     * Toggles the window between maximized and normal states.
     * @param maximized True to maximize the window, false for normal size.
     */
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

    /**
     * Adds a {@link Container} to the window using its intrinsic index.
     * @param container The container to add.
     */
    public void addContainer(Container container) {
        addContainer(container, container.getIndex());
    }

    public void addContainer(Container container, Node node) {
        addContainer(container, node, container.getIndex());
    }


    /**
     * Adds a {@link Container} to the window at a specific index. If a container already exists at the given index,
     * it attempts to shift existing containers to accommodate the new one.
     * @param container The container to add.
     * @param index The desired rendering index for the container.
     */
    public void addContainer(Container container, int index) {
        Container current = containers.get(index);
        if (current != null) {
            int i = index + 1;
            removeContainer(current);
            addContainer(current, i);
        }
        containers.put(index, container);

        Node assemble = container.assemble();

        if (index > 0) {
            if (root.getChildren().size() < index) {
                root.getChildren().addLast(assemble);
            } else {
                root.getChildren().add(index, assemble);
            }
        } else {
            root.getChildren().add(assemble);
        }
    }

    /**
     * Adds a pre-compiled {@link Container} to the window. Use {@link Container#assemble()} to build the {@link Node}.
     * @param container The container to add.
     * @param node The pre-compiled node to add.
     * @param index The desired rendering index for the container.
     */
    public void addContainer(Container container, Node node, int index) {
        Container current = containers.get(index);
        if (current != null) {
            int i = index + 1;
            removeContainer(current);
            addContainer(current, i);
        }
        containers.put(index, container);
        root.getChildren().add(node);
    }

    /**
     * Adds all containers from the given TreeMap to this window's container collection.
     * Existing containers with matching indices will be overwritten.
     * @param con The TreeMap of containers to add.
     */
    public void addContainers(TreeMap<Integer, Container> con) {
        this.containers.putAll(con);
    }

    /**
     * Replaces the entire set of containers in the window with a new TreeMap of containers.
     * @param containers The new TreeMap of containers.
     */
    public void setContainers(TreeMap<Integer, Container> containers) {
        this.containers = containers;
    }

    /**
     * Replaces an old container instance with a new container instance, preserving its original index.
     * The old container must already exist in the window's collection.
     * @param oldContainer The container instance to be replaced.
     * @param newContainer The new container instance to take its place.
     */
    public void replaceContainer(Container oldContainer, Container newContainer) {
        if (containers.containsValue(oldContainer)) {
            containers.replace(oldContainer.getIndex(), newContainer);
        }
    }

    /**
     * Replaces the container at a specific index with a new container.
     * The window is then re-rendered to reflect this change.
     * @param index The index at which to replace the container.
     * @param container The new container to place at the specified index.
     */
    public void replaceContainer(int index, Container container) {
        containers.remove(index);
        containers.replace(index, container);
        render();
    }

    /**
     * Removes a specific {@link Container} instance from the window's collection.
     * Note: This only removes the container from the internal map,
     * it does not automatically remove its corresponding JavaFX Node from the scene graph.
     * A subsequent `render()` call would be needed to update the display.
     * @param container The container instance to remove.
     */
    public void removeContainer(Container container) {
        int toRemove = -1;
        for (Map.Entry<Integer, Container> entry : containers.entrySet()) {
            if (entry.getValue() == container) {
                toRemove = entry.getKey();
                root.getChildren().remove(container.getNode());
                break;
            }
        }
        containers.remove(toRemove);

        if (currentPopup == container) {
            currentPopup = null;
        }
    }

    /**
     * Clears all containers from the window.
     * A garbage collection hint is provided to the JVM.
     */
    public void clearContainers() {
        new LinkedList<>(containers.values()).forEach(this::removeContainer);
        containers.clear();
    }

    /**
     * Removes the container at a specific index from the window and re-renders the display.
     * @param index The index of the container to remove.
     */
    public void clearContainer(int index) {
        containers.remove(index);
        render();
    }

    /**
     * Retrieves the TreeMap of all containers currently managed by the window.
     * @return A TreeMap mapping container indices to Container objects.
     */
    public TreeMap<Integer, Container> getContainers() {
        return containers;
    }

    /**
     * Clears all child nodes from the root pane and re-sets the scene's root.
     * The stage is then shown.
     */
    public void clean() {
        root.getChildren().clear();
        scene.setRoot(root);
        stage.show();
    }

    /**
     * Clears all containers and resets the window's root pane and scene.
     * The stage is not automatically shown after this operation.
     */
    public void clear() {
        clear(false);
    }

    /**
     * Clears all containers and resets the window's root pane and scene.
     * Optionally shows the stage after clearing.
     * @param render True to show the stage after clearing, false otherwise.
     */
    public void clear(boolean render) {
        clearContainers();
        this.root = new Pane();
        root.setPrefSize(width, height);
        this.scene = new Scene(root);
        this.stage.setScene(scene);
        if (render) {
            stage.show();
        }
    }

    /**
     * Closes the JavaFX Stage associated with this window.
     * A garbage collection hint is provided to the JVM.
     */
    public void close() {
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Clears the root pane's children, creates a new Stage, and hides it.
     * This method essentially resets the visual state of the window without closing it.
     */
    public void resetStage() {
        buildStage();
    }

    /**
     * Shows the window's stage.
     * Note: This method is named "hide" but performs "show". This might be a naming inconsistency.
     */
    public void hide() {
        stage.show();
    }

    /**
     * Builds the JavaFX Stage and then renders all active nodes on the screen.
     */
    public void buildAndRender() {
        buildStage();
        render();
    }

    /**
     * Builds and displays all active nodes on the screen. This function translates the engine's API into JavaFX and updates the stage and scene.
     * <p>
     * Calling this excessively can cause visual flicker. It must be called after adding,
     * modifying, or removing {@link Overlay} or {@link Container} to update the display.
     * </p>
     */
    public void render() {
        build();
        if (focused) {
            stage.requestFocus();
        }
        stage.show();
    }

    /**
     * Builds the engine's API onto the JavaFX framework without displaying the built nodes on the screen.
     * For most use cases, {@link #render()} is recommended as it also shows the updated display.
     */
    public void build() {
        if (!containers.isEmpty()) {
            build(false);
        }
    }

    /**
     * Builds the engine's API onto the JavaFX framework, optionally resetting the scene.
     * This method processes and prepares containers for display but does not automatically show them.
     * @param reset True to reset the scene (clears and initialises the root pane and scene), false to only clear children.
     */
    public void build(boolean reset) {
        root.getChildren().clear();
        root.getStylesheets().clear();
        if (reset) {
            // Resets the scene. This can cause flickering but might be needed in specific capture scenarios.
            this.root = new Pane();
            root.setPrefSize(width, height);
            this.scene = new Scene(root);
            this.stage.setScene(scene);
        }

        containers.values().forEach(this::renderContainer);
    }

    /**
     * Renders a specific container by adding it on top of the current window's content.
     * The container is automatically assigned an index that places it at the highest layer.
     * @param container The container to render.
     */
    public void render(Container container) {
        int index = containers.isEmpty() ? 1 : containers.lastKey() + 1;
        container.setIndex(index);
        containers.put(index, container);
        renderContainer(container);
    }

    /**
     * Renders a single {@link Container} instance by building its corresponding JavaFX Node
     * and adding it to the window's root pane.
     * @param container The container to render.
     */
    private void renderContainer(Container container) {
        if (container.getNode() != null) {
            root.getChildren().remove(container.getNode());
        }

        root.getChildren().add(container.assemble());
    }

    /**
     * Renders a popup container, ensuring that only one popup is active at a time.
     * If a previous popup exists, its Node is removed from the scene graph before the new one is added.
     * This method does not apply translation (X, Y) from the container's properties directly to the node,
     * assuming these are handled by the calling `renderPopup` method.
     * @param container The container to render as a popup.
     */
    private void renderPopupContainer(Container container) {
        if (currentPopup != null) {
            removeContainer(currentPopup);
        }
        currentPopup = container;

        addContainer(container);
    }

    public Container getCurrentPopup() {
        return currentPopup;
    }

    private void handleWindowScaling(Stage stage) {
        // This scales the application to the desired width and height that it is running at.
        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.height = newValue.doubleValue();

            double scaleWidth = getWidthScale();
            double scaleHeight = getHeightScale();

            if (scale) {
                Scale scale = new Scale(scaleWidth, scaleHeight, 0, 0);
                root.getTransforms().setAll(scale);
            }
        });
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.width = newValue.doubleValue();

            double scaleWidth = getWidthScale();
            double scaleHeight = getHeightScale();

            if (scale) {
                Scale scale = new Scale(scaleWidth, scaleHeight, 0, 0);
                root.getTransforms().setAll(scale);
            }
        });
    }

    public void handleSceneTransition(RenScene scene, Transitions transitions) {
        // Play transition on the current root
        transitions.play(scene);
    }
}