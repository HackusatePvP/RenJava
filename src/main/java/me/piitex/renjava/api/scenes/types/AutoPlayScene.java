package me.piitex.renjava.api.scenes.types;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

import java.util.logging.Logger;

public class AutoPlayScene extends RenScene {
    private final ImageLoader loader;

    private final int duration;

    public AutoPlayScene(String id, ImageLoader backgroundImage, int duration) {
        super(id, backgroundImage);
        this.loader = backgroundImage;
        this.duration = duration;
    }

    @Override
    public void build(Stage stage, boolean ui) {
        Logger logger = RenJava.getInstance().getLogger();
        Group root = new Group();

        Image background = null;
        try {
            background = loader.build();
        } catch (ImageNotFoundException e) {
            logger.severe(e.getMessage());
        } finally {
            if (background != null) {
                ImageView imageView = new ImageView(background);
                root.getChildren().add(imageView);
            }
        }
        hookOverlays(root);
        setStage(stage, root, StageType.IMAGE_SCENE, false);
    }

    /**
     * Duration is in milliseconds.
     * @return
     */
    public int getDuration() {
        return duration;
    }

    @Override
    public StageType getStageType() {
        return StageType.IMAGE_SCENE;
    }
}
