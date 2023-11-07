package me.piitex.renjava.api.scenes.types;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.builders.VideoLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

public class AnimationScene extends RenScene {
    private Character character;
    private final VideoLoader loader;
    private String dialogue;
    private String characterDisplayName;

    public AnimationScene(String id, VideoLoader loader) {
        super(id, null);
        this.loader = loader;
    }

    public AnimationScene(String id, VideoLoader videoLoader, Character character, String dialogue) {
        super(id, null);
        this.loader = videoLoader;
        this.character = character;
        this.dialogue = dialogue;
        this.characterDisplayName = character.getDisplayName();
    }

    public Character getCharacter() {
        return character;
    }

    public String getCharacterNameDisplay() {
        return characterDisplayName;
    }

    @Override
    public void build(Stage stage, boolean ui) {
        Group root = new Group();
        MediaPlayer player = loader.getPlayer();
        MediaView mediaView = new MediaView(player);
        root.getChildren().add(mediaView);

        if (ui) {
            // Adds the text box stuff if necessary.
            Text text = null;
            Text characterDisplay = null;
            if (dialogue != null && !dialogue.isEmpty()) {
                text = new Text(dialogue);
                if (getCharacterNameDisplay() != null && !getCharacterNameDisplay().isEmpty()) {
                    // Set character display
                    RenJava.getInstance().getLogger().info("Character Display Name Validation: " + getCharacterNameDisplay());
                    characterDisplay = new Text(getCharacterNameDisplay());
                } else {
                    RenJava.getInstance().getLogger().info("Character Display Name Validation: " + character.getDisplayName());
                    characterDisplay = new Text(character.getDisplayName());
                }
                characterDisplay.setFill(character.getColor());
                text.setFont(RenJava.getInstance().getDefaultFont().getFont()); // TODO: 8/1/2023 Set a default font/make font param or something
            }

            if (text != null) {
                Image textbox;
                try {
                    textbox = new ImageLoader("/gui/textbox.png").build();
                } catch (ImageNotFoundException e) {
                    throw new RuntimeException(e);
                }
                ImageView imageView = new ImageView(textbox);
                imageView.setY(1080 - textbox.getHeight()); // Set the text box to the bottom
                root.getChildren().add(imageView);

                // This is the size of the text box (This is set the top right corner of the image)
                double setX = imageView.getX();
                double setY = imageView.getY();
                // Create a text flow pane for the text
                TextFlow texFlow = new TextFlow();
                texFlow.setTextAlignment(TextAlignment.JUSTIFY);
                texFlow.setPrefSize(1000, 600);
                texFlow.setTranslateX(setX + 250); // Over 250 to the right
                texFlow.setTranslateY(setY + 100); // Down 100
                texFlow.getChildren().add(text); // Add the text to the textflow
                root.getChildren().add(texFlow);

                characterDisplay.setFont(new FontLoader("JandaManateeSolid.ttf", 36).getFont());
                characterDisplay.setX(setX + 200);
                characterDisplay.setY(setY + 70);

                // Set the text a little down from the top and over to the right.
                // Name of character will be top right of the text box
                root.getChildren().add(characterDisplay);
            }
        }

        RenJava.getInstance().getPlayer().setCurrentScene(this.getId());
        // Add the displayName in the top

        // play video
        // TODO: 9/26/2023 loop until player ends scene -optional maybe add param or something
        loader.play(true);

        hookOverlays(root);

        setStage(stage, root, StageType.ANIMATION_SCENE, false);

    }

    @Override
    public StageType getStageType() {
        return StageType.ANIMATION_SCENE;
    }
}
