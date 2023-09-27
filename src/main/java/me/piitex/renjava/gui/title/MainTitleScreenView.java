package me.piitex.renjava.gui.title;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.MouseClickEvent;
import me.piitex.renjava.gui.ScreenView;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.Overlay;
import me.piitex.renjava.gui.overlay.TextOverlay;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Logger;

/**
 * This has to be customizable.
 */
public class MainTitleScreenView extends ScreenView {
    private int width,height;
    private String titleDisplay;
    private ImageLoader image = new ImageLoader("/gui/main_menu.png");


    public MainTitleScreenView(RenJava renJava) {
        RenJavaConfiguration configuration = renJava.getConfiguration();
        this.width = configuration.getWidth();
        this.height = configuration.getHeight();
        this.titleDisplay = configuration.getGameTitle();
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

    public void build(Stage splashStage) {
        Logger logger = RenJava.getInstance().getLogger();
        logger.info("Building main menu screen...");
        splashStage.hide();

        Stage stage = new Stage();
        Group root = new Group();
        logger.info("Setting background image...");
        ImageView backgroundView;
        try {
            backgroundView = new ImageView(image.build());
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        root.getChildren().add(backgroundView);

        logger.info("Adding overlays...");
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
                RenJava.getInstance().getLogger().info("Adding button...");
                Button button = buttonOverlay.button();
                button.setTranslateX(buttonOverlay.x());
                button.setTranslateY(buttonOverlay.y());
                root.getChildren().add(button);
            }
        }

        logger.info("Creating buttons...");
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        if (getStartButton() != null) {
            Button button = getStartButton().build();
            vBox.getChildren().add(button);
        }
        if (getLoadButton() != null) {
            Button button = getLoadButton().build();
            vBox.getChildren().add(button);
        }
        if (getOptionsButton() != null) {
            Button button = getOptionsButton().build();
            vBox.getChildren().add(button);
        }
        if (getAboutButton() != null) {
            Button button = getAboutButton().build();
            vBox.getChildren().add(button);
        }
        if (getHelpButton() != null) {
            Button button = getHelpButton().build();
            vBox.getChildren().add(button);
        }
        if (getQuitButton() != null) {
            Button button = getQuitButton().build();
            vBox.getChildren().add(button);
        }
        root.getChildren().add(vBox);

        // Buttons have to go on top of everything.
        /*if (getStartButton() != null) {
            Button button = getStartButton().build();
            button.setTranslateY(getStartButton().getX());
            button.setTranslateY(getStartButton().getY());
            root.getChildren().add(button);
        }*/

        logger.info("Creating scene...");
        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(new File(System.getProperty("user.dir") + "/game/css/button.css").toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            MouseClickEvent clickEvent = new MouseClickEvent(event);
            RenJava.callEvent(clickEvent);
        });
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.getIcons().add(RenJava.getInstance().getConfiguration().getGameIcon());
        stage.setTitle(RenJava.getInstance().getConfiguration().getGameTitle());
        RenJava.getInstance().setStage(stage, StageType.MAIN_MENU);
        stage.show();
        logger.info("Dispatched main menu.");
    }
}
