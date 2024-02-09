package me.piitex.renjava.gui;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.transitions.TransitionType;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.api.scenes.transitions.types.FadingTransition;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.layouts.Layout;
import me.piitex.renjava.gui.layouts.impl.HorizontalLayout;
import me.piitex.renjava.gui.layouts.impl.VerticalLayout;
import me.piitex.renjava.gui.overlay.*;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.util.LinkedHashSet;
import java.util.logging.Logger;

public class Menu {
    private final Stage stage;
    private final double width, height;

    private int x, y;

    // Pre configured data.
    private String title = renJava.getName();

    private Image backgroundImage;

    private final LinkedHashSet<Node> nodes = new LinkedHashSet<>();

    private final LinkedHashSet<Layout> layouts = new LinkedHashSet<>();
    private final LinkedHashSet<Overlay> overlays = new LinkedHashSet<>();
    private final LinkedHashSet<Menu> children = new LinkedHashSet<>();

    // Constants
    private static final RenJava renJava = RenJava.getInstance();

    public Menu(double width, double height) {
        this.width = width;
        this.height = height;
        this.stage = renJava.getStage();
    }

    public Menu(ImageLoader imageLoader, double width, double height) {
        try {
            this.backgroundImage = imageLoader.build();
        } catch (ImageNotFoundException e) {
            e.printStackTrace();
        }
        this.width = width;
        this.height = height;
        this.stage = renJava.getStage();
    }

    public Stage getStage() {
        return stage;
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

    /* Rendering functions */

    public Menu setTitle(String title) {
        this.title = title;
        return this;
    }

    public Menu addMenu(Menu menu) {
        this.children.add(menu);
        return this;
    }

    /**
     * Renders the menu on the screen.
     *
     * @param root The root Group to which the menu will be added. If null, a new Group will be created.
     * @param renScene The RenScene that is being used. If null, it will be assumed this is a main menu screen.
     */
    public void render(@Nullable Pane root, @Nullable RenScene renScene) {
        Logger logger = renJava.getLogger();

        if (root == null) {
            root = new Pane();
        }

        root.setTranslateX(x);
        root.setTranslateY(y);

        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, new CornerRadii(1), new Insets(0,0,0,0));
        root.setBackground(new Background(backgroundFill));

        if (backgroundImage != null) {
            if (renScene != null) {
                renJava.getLogger().info("Post render for " + renScene.getId());
                // First call animation
                if (renScene.getStartAnimation() != null) {
                    renJava.getLogger().info("Animation found...");
                    try {
                        Timeline timeline = renScene.getStartAnimation().build();

                        SceneAnimationStartEvent animationStartEvent = new SceneAnimationStartEvent(renScene);
                        renJava.getLogger().info("Calling animation start event...");
                        RenJava.callEvent(animationStartEvent);
                        renJava.getLogger().info("Playing TimeLine...");

                        // This is not working correctly...
                        timeline.setCycleCount(Animation.INDEFINITE);
                        timeline.play();
                    } catch (ImageNotFoundException e) {
                        renJava.getLogger().severe(e.getMessage());
                        return;

                    }
                } else if (renScene.getStartTransition() != null) {
                    logger.info("Starting transition...");
                    if (renScene.getStartTransition().getTransitionType() == TransitionType.BACKGROUND_IMAGE) {
                        ImageView view = new ImageView(backgroundImage);
                        root.getChildren().add(view); // Add to view
                        Transitions transitions = renScene.getStartTransition();
                        if (transitions instanceof FadingTransition fadingTransition) {
                            fadingTransition.play(view);
                        }
                    } else if (renScene.getStartTransition().getTransitionType() == TransitionType.SCENE) {
                        Transitions transitions = renScene.getStartTransition();
                        if (transitions instanceof FadingTransition fadingTransition) {
                            fadingTransition.play(root);
                        }
                    }
                } else {
                    logger.info("No animation or transition present...");
                    ImageView view = new ImageView(backgroundImage);
                    root.getChildren().add(view);
                }
            } else {
                logger.info("");
                ImageView view = new ImageView(backgroundImage);
                root.getChildren().add(view);
            }


            // TODO: 2/7/2024 Handle animations here
        } else {
            // TODO: 12/28/2023 Create a black background
        }


        // FIXME: 12/28/2023 Can optimize.
        logger.info("Rendering layouts...");
        for (Layout layout : layouts) {
            if (layout instanceof HorizontalLayout horizontalLayout) {
                logger.info("Rendering horizontal layout...");
                HBox hBox = new HBox();
                logger.info("Checking layout overlays...");
                for (Overlay overlay : horizontalLayout.getOverlays()) {
                    if (overlay instanceof ImageOverlay imageOverlay) {
                        logger.info("Adding image overlay...");
                        hBox.getChildren().add(new ImageView(imageOverlay.image()));
                    } else if (overlay instanceof TextOverlay textOverlay) {
                        logger.info("Adding text overlay...");
                        hBox.getChildren().add(textOverlay.text());
                    } else if (overlay instanceof ButtonOverlay buttonOverlay) {
                        logger.info("Adding button overlay...");
                        hBox.getChildren().add(buttonOverlay.button().build());
                    }  else if (overlay instanceof TextFlowOverlay textFlowOverlay) {
                        logger.info("Adding text flow overlay...");
                        hBox.getChildren().add(textFlowOverlay.textFlowBuilder().build());
                    }
                }

                hBox.setSpacing(horizontalLayout.getSpacing());
                hBox.setTranslateX(horizontalLayout.getXPosition());
                hBox.setTranslateY(horizontalLayout.getYPosition());
                hBox.setPrefWidth(horizontalLayout.getWidth());
                hBox.setPrefHeight(horizontalLayout.getHeight());
                hBox.setAlignment(Pos.CENTER);
                HBox.setHgrow(hBox, Priority.ALWAYS);

                root.getChildren().add(hBox);
            } else {
                logger.info("Rendering vertical layout...");
                VerticalLayout verticalLayout = (VerticalLayout) layout;
                VBox vBox = new VBox();

                logger.info("Checking layout overlays...");
                for (Overlay overlay : verticalLayout.getOverlays()) {
                    if (overlay instanceof ImageOverlay imageOverlay) {
                        logger.info("Adding image overlay...");
                        vBox.getChildren().add(new ImageView(imageOverlay.image()));
                    } else if (overlay instanceof TextOverlay textOverlay) {
                        logger.info("Adding text overlay...");
                        vBox.getChildren().add(textOverlay.text());
                    } else if (overlay instanceof ButtonOverlay buttonOverlay) {
                        logger.info("Adding button overlay...");
                        logger.info("Button Rotate: " + buttonOverlay.button().build().getRotate());
                        vBox.getChildren().add(buttonOverlay.button().build());
                        vBox.layout();
                    } else if (overlay instanceof TextFlowOverlay textFlowOverlay) {
                        logger.info("Adding text flow overlay...");
                        vBox.getChildren().add(textFlowOverlay.textFlowBuilder().build());
                    }
                }

                vBox.setSpacing(verticalLayout.getSpacing());
                vBox.setTranslateX(verticalLayout.getXPosition());
                vBox.setTranslateY(verticalLayout.getYPosition());
                vBox.setPrefWidth(verticalLayout.getWidth());
                vBox.setPrefHeight(verticalLayout.getHeight());
                vBox.setAlignment(Pos.CENTER);
                VBox.setVgrow(vBox, Priority.ALWAYS);

                root.getChildren().add(vBox);
            }
        }

        for (Overlay overlay : overlays) {
            if (overlay instanceof ImageOverlay imageOverlay) {
                Image image = imageOverlay.image();
                ImageView imageView = new ImageView(image);
                imageView.setTranslateX(imageOverlay.x());
                imageView.setTranslateY(imageOverlay.y());
                root.getChildren().add(imageView);
            } else if (overlay instanceof TextOverlay textOverlay) {
                Text text = textOverlay.text();
                text.setTranslateX(textOverlay.x());
                text.setTranslateY(textOverlay.y());
                root.getChildren().add(textOverlay.text());
            } else if (overlay instanceof ButtonOverlay buttonOverlay) {
                Button button = buttonOverlay.build();
                button.setTranslateX(buttonOverlay.x());
                button.setTranslateY(buttonOverlay.y());
                root.getChildren().add(buttonOverlay.button().build());
            } else if (overlay instanceof TextFlowOverlay textFlowOverlay) {
                logger.info("Adding text flow overlay...");
                TextFlow textFlow = textFlowOverlay.textFlowBuilder().build();
                textFlow.setTranslateX(textFlowOverlay.x());
                textFlow.setTranslateY(textFlowOverlay.y());
                root.getChildren().add(textFlow);
            }
        }

        for (Menu menu : children) {
            menu.render(root, renScene); // Renders menu on top of this menu.
        }

        Scene scene;
        if (stage.getScene() != null) {
            scene = stage.getScene();
            stage.getScene().setRoot(root);
        } else {
            stage.setScene(new Scene(root));
            scene = stage.getScene();
        }

        try {
            scene.getStylesheets().add(new File(System.getProperty("user.dir") + "/game/css/button.css").toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        setInputControls(scene);

        stage.show();
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