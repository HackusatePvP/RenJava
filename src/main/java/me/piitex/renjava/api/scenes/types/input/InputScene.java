package me.piitex.renjava.api.scenes.types.input;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.MouseClickEvent;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

/**
 * Input scenes are scenes that require player input (text).
 * This can be used for setting the character name and other options.
 */
public class InputScene extends RenScene {
    private final ImageLoader loader;
    private TextField inputField;
    private InputSetInterface setInterface;

    public InputScene(String id, ImageLoader loader) {
        super(id, loader);
        this.loader = loader;
    }

    public TextField getInputField() {
        return inputField;
    }

    public void onSet(InputSetInterface inputSetInterface) {
        this.setInterface = inputSetInterface;
    }

    public InputSetInterface getSetInterface() {
        return setInterface;
    }

    @Override
    public void build(Stage stage) {
        RenJava.getInstance().setStage(stage, StageType.GAME_WINDOW);
        Group root = new Group();
        // Add background image
        Image background;
        try {
            background = loader.build();
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imageView = new ImageView(background);
        root.getChildren().add(imageView);

        Image textbox = null;
        try {
            textbox = new ImageLoader("/gui/textbox.png").build();
        } catch (ImageNotFoundException e) {
            e.printStackTrace();
        }
        if (textbox != null) {
            imageView = new ImageView(textbox);
            imageView.setY(1080 - textbox.getHeight()); // Set the text box to the bottom
            root.getChildren().add(imageView);
        }

        double setX = imageView.getX();
        double setY = imageView.getY();
        inputField = new TextField();
        inputField.setTranslateX(setX + 250);
        inputField.setTranslateY(setY + 100);
        root.getChildren().add(inputField);

        Scene scene = new Scene(root);
        scene.setOnMouseClicked(event -> {
            MouseClickEvent event1 = new MouseClickEvent(event);
            RenJava.callEvent(event1);
        });
        stage.setScene(scene);
        stage.show();
        RenJava.getInstance().getPlayer().setCurrentScene(this.getId());
        SceneStartEvent startEvent = new SceneStartEvent(this);
        RenJava.callEvent(startEvent);
    }
}
