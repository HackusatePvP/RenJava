package me.piitex.renjava.api.scenes.types;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

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
        Group root = new Group();

        Image background;
        try {
            background = loader.build();
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imageView = new ImageView(background);
        root.getChildren().add(imageView);
        hookOverlays(root);
        setStage(stage, root, StageType.IMAGE_SCENE, false);
    }

    public int getDuration() {
        return duration;
    }
}
