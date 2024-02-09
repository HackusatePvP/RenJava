package me.piitex.renjava.api.scenes.types;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.text.*;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.TextFlowBuilder;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.animation.AnimationBuilder;
import me.piitex.renjava.api.scenes.text.StringFormatter;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.SceneAnimationStartEvent;
import me.piitex.renjava.events.types.SceneBuildEvent;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

import me.piitex.renjava.gui.overlay.*;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.util.LinkedList;


/**
 * The ImageScene class represents an image scene in the RenJava framework.
 * It is used to display an image and text associated with a character.
 * Image scenes are typically used to present visuals and dialogue during gameplay or narrative progression.
 *
 * <p>
 * To create an image scene, instantiate the ImageScene class with the necessary parameters, such as the scene ID, character, dialogue, and background image loader.
 * The character and dialogue parameters can be set to null if they are optional and not needed for a particular image scene.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * Story story = new ExampleStory("example-story);
 * Character myCharacter = new Character("character-1", "John", Color.BLUE);
 * ImageLoader backgroundLoader = new ImageLoader("background.png");
 * ImageScene scene = new ImageScene("scene-1", myCharacter, "This is character text!", backgroundLoader);
 * story.addScene(scene);
 * }</pre>
 * </p>
 *
 * <p>
 * Note: The ImageScene class is used to create image scenes in the RenJava framework.
 * </p>
 */
public class ImageScene extends RenScene {
    private Character character;
    private String dialogue;
    private final ImageLoader backgroundImage;

    private String characterDisplayName;

    private final RenJavaConfiguration configuration;

    private static final RenJava renJava = RenJava.getInstance();

    /**
     * Creates an ImageScene object representing an image scene in the RenJava framework.
     * An image scene is used to display an image and text associated with a character.
     * Image scenes are typically used to present visuals and dialogue during gameplay or narrative progression.
     *
     * @param id        The ID used to identify the scene.
     * @param character The character who is talking. Pass null if no character is talking in the scene.
     * @param dialogue  The dialogue of the character. Pass null or an empty string if no one is talking.
     * @param loader    The background image loader for the scene.
     */
    public ImageScene(String id, @Nullable Character character, String dialogue, ImageLoader loader) {
        super(id, loader);
        this.character = character;
        this.dialogue = dialogue;
        this.backgroundImage = loader;
        if (character != null) {
            this.characterDisplayName = character.getDisplayName();
        }
        configuration = renJava.getConfiguration();
    }

    public Character getCharacter() {
        return character;
    }

    public String getCharacterNameDisplay() {
        return characterDisplayName;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public Menu build(Stage stage, boolean ui) {
        Menu rootMenu = new Menu(backgroundImage, configuration.getWidth(), configuration.getHeight());

        Text characterDisplay = null;
        if (dialogue != null && !dialogue.isEmpty()) {
            if (getCharacterNameDisplay() != null) {
                // Set character display
                characterDisplay = new Text(getCharacterNameDisplay());
            } else {
                characterDisplay = new Text(character.getDisplayName());
            }
            characterDisplay.setFill(character.getColor());
        }

        if (dialogue != null && !dialogue.isEmpty()) {
            Image textbox = null;
            try {
                textbox = new ImageLoader("gui/textbox.png").build();
            } catch (ImageNotFoundException e) {
                renJava.getLogger().severe(e.getMessage());
            } finally {
                if (textbox != null) {
                    Menu textboxMenu = new Menu(configuration.getWidth(), configuration.getHeight() - textbox.getHeight());

                    try {
                        ImageOverlay textBoxImage = new ImageOverlay(new ImageLoader("gui/textbox.png").build(), configuration.getDialogueBoxX() + configuration.getDialogueOffsetX(), configuration.getDialogueBoxY() + configuration.getDialogueOffsetY());
                        textboxMenu.addOverlay(textBoxImage);
                    } catch (ImageNotFoundException e) {
                        renJava.getLogger().severe(e.getMessage());
                    }

                    LinkedList<Text> texts = StringFormatter.formatText(dialogue);

                    if (texts.isEmpty()) {
                        Text text = new Text(dialogue);
                        text.setFont(renJava.getConfiguration().getDialogueFont().getFont());
                        TextFlowBuilder textFlowBuilder = new TextFlowBuilder(text, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
                        textboxMenu.addOverlay(new TextFlowOverlay(textFlowBuilder, configuration.getTextX() + configuration.getTextOffsetX(), configuration.getTextY() + configuration.getTextOffsetY()));
                    } else {
                        // FIXME: 12/29/2023 Duplicate code
                        TextFlowBuilder textFlowBuilder = new TextFlowBuilder(texts, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
                        textboxMenu.addOverlay(new TextFlowOverlay(textFlowBuilder, configuration.getTextX() + configuration.getTextOffsetX(), configuration.getTextY() + configuration.getTextOffsetY()));
                    }

                    characterDisplay.setFont(new FontLoader(renJava.getConfiguration().getDefaultFont().getFont(), configuration.getCharacterTextSize()).getFont());
                    characterDisplay.setFill(character.getColor());
                    characterDisplay.setX(configuration.getCharacterTextX() + configuration.getCharacterTextOffsetX());
                    characterDisplay.setY(configuration.getCharacterTextY() + configuration.getCharacterTextOffsetY());

                    textboxMenu.addOverlay(new TextOverlay(characterDisplay, characterDisplay.getX(), characterDisplay.getY(), 1, 1));
                    rootMenu.addMenu(textboxMenu);
                }
            }
        }
        for (File file : getStyleSheets()) {
            try {
                stage.getScene().getStylesheets().add(file.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        // Call SceneBuild event
        SceneBuildEvent event = new SceneBuildEvent(this, rootMenu);
        RenJava.callEvent(event);

        return rootMenu;
    }

    @Override
    public void render(Menu menu) {
        renJava.setStage(renJava.getStage(), StageType.IMAGE_SCENE);
        menu.render(null, this); // FIXME: 12/29/2023 Render depending on if ui is toggled

        SceneStartEvent event = new SceneStartEvent(this);
        RenJava.callEvent(event);

    }

    @Override
    public StageType getStageType() {
        return StageType.IMAGE_SCENE;
    }
}
