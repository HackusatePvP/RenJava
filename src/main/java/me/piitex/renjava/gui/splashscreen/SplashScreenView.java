package me.piitex.renjava.gui.splashscreen;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.APIChange;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.Overlay;
import me.piitex.renjava.gui.overlay.TextOverlay;

import java.util.HashSet;
import java.util.Set;

/**
 * Builder class for making a splash screen. A splash screen is a small box with an image.
 */
public class SplashScreenView {
    private final Image image;
    private int width = 600;
    private int height = 400;

    private int duration = 3;

    private final Set<Overlay> additionalOverlays = new HashSet<>();

    /**
     * Creates a splash screen which consists of a single image. This is displayed while parts of the framework are loading. By default, the size of the splash screen is 600x400.
     * @param image ImageLoader is used to load image assets.
     */
    public SplashScreenView(ImageLoader image) {
        try {
            this.image = image.build();
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param image ImageLoader is used to load image assets.
     * @param width Width of the window size.
     * @param height Height of the window size.
     */
    public SplashScreenView(ImageLoader image, int width, int height) {
        try {
            this.image = image.build();
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.width = width;
        this.height = height;
    }

    public void addAdditionalOverlay(Overlay overlay) {
        this.additionalOverlays.add(overlay);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImage() {
        return image;
    }

    public Set<Overlay> getAdditionalOverlays() {
        return additionalOverlays;
    }


    @APIChange(changedVersion = "0.0.153", description = "Duration for splash-screens is now configurable.")
    public int getDuration() {
        return duration;
    }

    @APIChange(changedVersion = "0.0.153", description = "Duration for splash-screens is now configurable.")
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void build(Stage stage) {
        Group group = new Group();
        Scene scene = new Scene(group, width, height);

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());
        imageView.setImage(image);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(imageView);
        stackPane.translateXProperty().bind(scene.widthProperty().subtract(stackPane.widthProperty()).divide(2));
        stackPane.translateYProperty().bind(scene.heightProperty().subtract(stackPane.heightProperty()).divide(2));
        group.getChildren().add(stackPane);

        // Add custom overlays
        for (Overlay overlay : additionalOverlays) {
            // Add the additional overlays to the scene
            if (overlay instanceof ImageOverlay imageOverlay) {
                ImageView imageView1 = new ImageView(imageOverlay.image());
                imageView1.setX(imageOverlay.x());
                imageView1.setY(imageView1.getY());
                group.getChildren().add(imageView1);
            } else if (overlay instanceof TextOverlay textOverlay) {
                Text text = new Text(textOverlay.text());
                text.setX(textOverlay.x());
                text.setY(textOverlay.y());
                group.getChildren().add(text);
            } else if (overlay instanceof ButtonOverlay buttonOverlay) {
                Button button = buttonOverlay.button();
                button.setTranslateX(buttonOverlay.x());
                button.setTranslateY(buttonOverlay.y());
                group.getChildren().add(button);
            }
        }

        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(RenJava.getInstance().getConfiguration().getGameIcon());
        stage.setScene(scene);
        stage.show();
    }
}
