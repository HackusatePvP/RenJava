package me.piitex.renjava.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlay.*;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;

import static javafx.scene.paint.Color.BLACK;

public class Menu {
    private final Stage stage;
    private Pane pane;
    private static Menu rootMenu;
    private final double width, height;

    private int x, y;

    // Pre configured data.
    private String title = renJava.getName();

    private Image backgroundImage;

    private final LinkedHashSet<Node> nodes = new LinkedHashSet<>();
    private final LinkedHashSet<Layout> layouts = new LinkedHashSet<>();
    private final LinkedHashSet<Overlay> overlays = new LinkedHashSet<>();
    private final LinkedHashSet<Menu> children = new LinkedHashSet<>();
    private final LinkedHashSet<Pane> subPanes = new LinkedHashSet<>();

    // Constants
    private static final RenJava renJava = RenJava.getInstance();

    public Menu(double width, double height) {
        this.width = width;
        this.height = height;
        this.stage = renJava.getStage();
    }

    public Menu(double width, double height, ImageLoader imageLoader) {
        if (imageLoader != null) {
            try {
                this.backgroundImage = imageLoader.build();
            } catch (ImageNotFoundException e) {
                RenLogger.LOGGER.error(e.getMessage());
            }
        }
        this.width = width;
        this.height = height;
        this.stage = renJava.getStage();
    }

    public Stage getStage() {
        return stage;
    }

    public Pane getPane() {
        return pane;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getTitle() {
        return title;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public Menu setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        return this;
    }

    public LinkedHashSet<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public LinkedHashSet<Layout> getLayouts() {
        return layouts;
    }

    public Menu addLayout(Layout layout) {
        layouts.add(layout);
        return this;
    }

    public LinkedHashSet<Overlay> getOverlays() {
        return overlays;
    }

    public Menu addOverlay(Overlay overlay) {
        overlays.add(overlay);
        return this;
    }

    public LinkedHashSet<Menu> getChildren() {
        return children;
    }

    public LinkedHashSet<Pane> getSubPanes() {
        return subPanes;
    }

    public Menu addSubPane(Pane pane) {
        subPanes.add(pane);
        return this;
    }

    /* Rendering functions */

    public Menu setTitle(String title) {
        this.title = title;
        return this;
    }

    public Menu addMenu(Menu menu) {
        this.children.add(menu);
        return this;
    }

    public Pane render() {
        return render(null, null);
    }

    /**
     * Renders the menu on the screen.
     *
     * @param root The root Group to which the menu will be added. If null, a new Group will be created.
     * @param renScene The RenScene that is being used. If null, it will be assumed this is a main menu screen.
     */
    public Pane render(@Nullable Pane root, @Nullable RenScene renScene) {
        RenLogger.LOGGER.info("Rendering menu...");
        if (root == null) {
            root = new Pane();
        }

        root.setTranslateX(x);
        root.setTranslateY(y);
        root.setPrefSize(width, height);

        // Background fill is used for fade ins.
        BackgroundFill backgroundFill = new BackgroundFill(BLACK, new CornerRadii(1), new Insets(0,0,0,0));
        root.setBackground(new Background(backgroundFill));

        if (backgroundImage != null) {
            Element backgroundImgElement = new Element(new ImageOverlay(backgroundImage, 0, 0));
            backgroundImgElement.render(root);
        }

        RenLogger.LOGGER.info("Rendering layouts...");
        for (Layout layout : layouts) {
            layout.render(root);
        }

        RenLogger.LOGGER.info("Rendering overlays...");
        for (Overlay overlay : overlays) {
            new Element(overlay).render(root);
        }

        RenLogger.LOGGER.info("Rendering sub-panes...");
        for (Pane sub : subPanes) {
            root.getChildren().add(sub);
        }

        for (Menu menu : children) {
            if (menu != null) {
                menu.render(root, renScene); // Renders menu on top of this menu.
            }
        }

        rootMenu = this;

        Scene scene;
        if (stage.getScene() != null) {
            scene = stage.getScene();
            stage.getScene().setRoot(root);
        } else {
            stage.setScene(new Scene(root));
            scene = stage.getScene();
        }

        setInputControls(scene);

        this.pane = root;

        scene.setFill(BLACK); // Scene fill is used for fade outs.

        // Apply transition to root
        if (renScene != null && renScene.getStartTransition() != null) {
            Transitions transitions = renScene.getStartTransition();
            transitions.play(root);
        }

        stage.show();

        return root;
    }

    public static Menu getRootMenu() {
        return rootMenu;
    }

    private void setInputControls(Scene scene) {
        scene.setOnMouseClicked(event -> {
            MouseClickEvent event1 = new MouseClickEvent(event);
            RenJava.callEvent(event1);
        });
        scene.setOnKeyPressed(keyEvent -> {
            // TODO: 9/28/2023 Call a repeatable task that ends when the key is released
            KeyPressEvent event1 = new KeyPressEvent(keyEvent.getCode());
            RenJava.callEvent(event1);
        });
        scene.setOnKeyReleased(keyEvent -> {
            KeyReleaseEvent event1 = new KeyReleaseEvent(keyEvent.getCode());
            RenJava.callEvent(event1);
        });
        scene.setOnScroll(scrollEvent -> {
            ScrollInputEvent event = new ScrollInputEvent(scrollEvent);
            RenJava.callEvent(event);
        });
    }
}