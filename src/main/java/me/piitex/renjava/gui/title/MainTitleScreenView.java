package me.piitex.renjava.gui.title;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.ButtonBuilder;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.ScreenView;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.Overlay;
import me.piitex.renjava.gui.overlay.TextOverlay;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * This has to be customizable.
 */
public class MainTitleScreenView extends ScreenView {
    private int width,height;
    private String titleDisplay;
    private ImageLoader image = new ImageLoader("/gui/main_menu.png");

    private final Stage stage;

    private final RenJava renJava;

    public MainTitleScreenView(RenJava renJava) {
        this.renJava = renJava;
        RenJavaConfiguration configuration = renJava.getConfiguration();
        this.width = configuration.getWidth();
        this.height = configuration.getHeight();
        this.titleDisplay = configuration.getGameTitle();
        this.stage = new Stage();
        if (configuration.getGameIcon() != null) {
            renJava.getLogger().info("Setting game icon...");
            try {
                stage.getIcons().add(configuration.getGameIcon().buildRaw());
            } catch (ImageNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (renJava.getSettings().isFullscreen()) {
            stage.setFullScreen(true);
        }
        stage.setTitle(titleDisplay);
        stage.setOnHiding(windowEvent -> {
            // FIXME: 10/10/2023 Shutdown all threads
            Platform.exit();
            System.exit(0);
        });
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitleDisplay() {
        return titleDisplay;
    }

    public void setTitleDisplay(String titleDisplay) {
        this.titleDisplay = titleDisplay;
    }

    public ImageLoader getImage() {
        return image;
    }

    public void setImage(ImageLoader image) {
        this.image = image;
    }

    public void build(Stage splashStage, boolean rightClicked) {
        Logger logger = RenJava.getInstance().getLogger();
        Group root = new Group();
        ImageView backgroundView;
        try {
            backgroundView = new ImageView(image.build());
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        root.getChildren().add(backgroundView);
        for (Overlay overlay : getOverlays()) {
            // Add the additional overlays to the scene
            if (overlay instanceof ImageOverlay imageOverlay) {
                ImageView imageView = new ImageView(imageOverlay.image());
                imageView.setX(imageOverlay.x());
                imageView.setY(imageOverlay.y());
                root.getChildren().add(imageView);
            } else if (overlay instanceof TextOverlay textOverlay) {
                Text text = new Text(textOverlay.text());
                text.setX(textOverlay.x());
                text.setY(textOverlay.y());
                text.setScaleX(textOverlay.xScale());
                text.setScaleY(textOverlay.yScale());
                root.getChildren().add(text);
            } else if (overlay instanceof ButtonOverlay buttonOverlay) {
                Button button = buttonOverlay.button();
                button.setTranslateX(buttonOverlay.x());
                button.setTranslateY(buttonOverlay.y());
                root.getChildren().add(button);
            }
        }

        logger.info("Creating buttons...");

        VBox vBox = getButtonVbox();
        if (rightClicked) {
            vBox.getChildren().clear();
        }
        List<ButtonBuilder> list = new ArrayList<>(getButtons());
        Collections.reverse(list);
        for (ButtonBuilder builder : list) {
            if (rightClicked) {
                if (builder.getId().toLowerCase().contains("load")) {
                    ButtonBuilder saveBuilder = ButtonBuilder.copyOf("menu-save-button", builder);
                    saveBuilder.setText("Save");
                    vBox.getChildren().add(saveBuilder.build());
                    continue;
                }
            }
            vBox.getChildren().add(builder.build());
        }
        root.getChildren().add(vBox);

        logger.info("Creating scene...");
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

        scene.setOnKeyPressed(event -> {
            KeyPressEvent event1 = new KeyPressEvent(event.getCode());
            RenJava.callEvent(event1);
        });
        SettingsProperties settingsProperties = RenJava.getInstance().getSettings();
        if (!settingsProperties.isFullscreen()) {
            stage.setMaximized(true);
        } else {
            stage.setFullScreen(true);
        }
        stage.setWidth(width);
        stage.setHeight(height);

        // Call dispatch event
        MainMenDispatchEvent event = new MainMenDispatchEvent(stage, scene);
        RenJava.callEvent(event);
        stage.show();
        renJava.setStage(stage, StageType.MAIN_MENU);
        logger.info("Dispatched main menu.");
    }

    public Stage getStage() {
        return stage;
    }

    private void setInputControls(Scene scene) {
        // FIXME: 10/29/2023 This seems to cause issue #1
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
