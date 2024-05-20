package me.piitex.renjava.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.gui.overlay.Region;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.overlay.*;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;

import static javafx.scene.paint.Color.BLACK;

public class Menu {
    private final Stage stage;
    private Pane pane;
    private static Menu rootMenu;
    private double width, height;
    private boolean renderFadeInFill = true;
    private boolean renderFadeOutFill = true;
    private double x, y;
    private double scaleX;
    private double scaleY;

    // Pre configured data.
    private String title = renJava.getName();

    private ImageOverlay backgroundImage;

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

    public Menu(double width, double height, ImageOverlay backgroundImage) {
        this.backgroundImage = backgroundImage;
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

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public void setRenderFadeInFill(boolean renderFadeInFill) {
        this.renderFadeInFill = renderFadeInFill;
    }

    public void setRenderFadeOutFill(boolean renderFadeOutFill) {
        this.renderFadeOutFill = renderFadeOutFill;
    }

    public String getTitle() {
        return title;
    }

    public ImageOverlay getBackgroundImage() {
        return backgroundImage;
    }

    public Menu setBackgroundImage(ImageOverlay backgroundImage) {
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
        return render(null, null, true);
    }

    public Pane render(boolean render) {
        return render(null, null, render);
    }

    public Pane render(Pane pane) {
        return render(pane, null, true);
    }

    public Pane render(RenScene renScene) {
        return render(null, renScene, true);
    }

    /**
     * Renders the menu on the screen.
     *
     * @param root The root Group to which the menu will be added. If null, a new Group will be created.
     * @param renScene The RenScene that is being used. If null, it will be assumed this is a main menu screen.
     */
    public Pane render(@Nullable Pane root, @Nullable RenScene renScene, boolean render) {
        RenLogger.LOGGER.info("Rendering menu...");
        if (root == null) {
            RenLogger.LOGGER.debug("Root pane is null.");
            root = new Pane();
        }

        root.setTranslateX(x);
        root.setTranslateY(y);
        if (scaleX > 0) {
            root.setPrefWidth(width * scaleX);
            root.setMaxWidth(width * scaleX);
            root.setMinWidth(width * scaleX);
        }
        if (scaleY > 0) {
            root.setPrefHeight(height * scaleY);
            root.setMaxHeight(height * scaleY);
            root.setMinHeight(height * scaleY);
        }

        // Background fill is used for fade ins.
        if (renderFadeInFill) {
            BackgroundFill backgroundFill = new BackgroundFill(BLACK, new CornerRadii(1), new Insets(0, 0, 0, 0));
            root.setBackground(new Background(backgroundFill));
        }

        if (backgroundImage != null) {
            ImageOverlay backGroundImageOverlay = backgroundImage;

            backGroundImageOverlay.setScaleX(this.scaleX);
            backGroundImageOverlay.setScaleY(this.scaleY);
            Element backgroundImgElement = new Element(backGroundImageOverlay);
            backgroundImgElement.render(root);
        }

        RenLogger.LOGGER.info("Rendering layouts...");
        for (Layout layout : layouts) {
            layout.render(root);
        }

        RenLogger.LOGGER.info("Rendering overlays...");
        for (Overlay overlay : overlays) {
            if (overlay instanceof Region region) {
                region.setScaleX(scaleX);
                region.setScaleY(scaleY);
            }

            new Element(overlay).render(root);
        }

        RenLogger.LOGGER.info("Rendering sub-panes...");
        for (Pane sub : subPanes) {
            root.getChildren().add(sub);
        }

        for (Menu menu : children) {
            RenLogger.LOGGER.info("Rendering child menus...");
            if (menu != null) {
                menu.setRenderFadeOutFill(renderFadeOutFill);
                menu.setRenderFadeInFill(renderFadeInFill);
                menu.setScaleX(scaleX);
                menu.setScaleY(scaleY);
                menu.render(root, renScene, render); // Renders menu on top of this menu.
            }
        }

        rootMenu = this;

        if (render) {
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

            if (renderFadeOutFill) {
                scene.setFill(BLACK); // Scene fill is used for fade outs.
            }

            // Apply transition to root
            if (renScene != null && renScene.getStartTransition() != null) {
                Transitions transitions = renScene.getStartTransition();
                transitions.play(root);
            }
            if (renScene != null) {
                renScene.setStage(stage);
                RenJava.getInstance().getPlayer().setLastRenderedRenScene(renScene);
            }
            stage.show();
        }

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