package me.piitex.renjava.api.scenes.types;

import javafx.scene.text.*;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.text.StringFormatter;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.SceneBuildEvent;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.loaders.ImageLoader;

import me.piitex.renjava.gui.overlay.*;
import me.piitex.renjava.loggers.RenLogger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.util.AbstractMap;
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
    private ImageOverlay backgroundImage;
    private Font font;
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
     * @param backgroundImage    The background image loader for the scene.
     */
    public ImageScene(String id, @Nullable Character character, String dialogue, @Nullable ImageOverlay backgroundImage) {
        super(id, backgroundImage);
        this.character = character;
        this.dialogue = dialogue;
        if (backgroundImage != null) {
            this.backgroundImage = backgroundImage;
            renJava.getPlayer().setLastDisplayedImage(new AbstractMap.SimpleEntry<>(getStory().getId(), backgroundImage));
        }
        if (character != null) {
            this.characterDisplayName = character.getDisplayName();
        }
        configuration = renJava.getConfiguration();
        font = configuration.getDialogueFont().getFont();
    }

    public ImageScene(String id, @Nullable Character character, String dialogue) {
        super(id, null);
        this.character = character;
        this.dialogue = dialogue;
        backgroundImage = renJava.getPlayer().getLastDisplayedImage().getValue();
        setBackgroundImage(backgroundImage);
        configuration = renJava.getConfiguration();
        font = configuration.getDialogueFont().getFont();
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

    public Font getDialogueFont() {
        return font;
    }

    public void setDialogueFont(Font font) {
        this.font = font;
    }

    @Override
    public Menu build(boolean ui) {
        Menu rootMenu = new Menu(configuration.getWidth(), configuration.getHeight(), backgroundImage);

        if (ui) {
            Text characterDisplay;
            if (character != null) {
                if (getCharacterNameDisplay() != null) {
                    // Set character display
                    characterDisplay = new Text(getCharacterNameDisplay());
                } else {
                    characterDisplay = new Text(character.getDisplayName());
                }
                characterDisplay.setFill(character.getColor());

                if (dialogue != null && !dialogue.isEmpty()) {
                    RenLogger.LOGGER.debug("Rendering textbox");
                    ImageLoader textbox = new ImageLoader("gui/textbox.png");
                    Menu textboxMenu = new Menu(configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());

                    ImageOverlay textBoxImage = new ImageOverlay(textbox, configuration.getDialogueBoxX() + configuration.getDialogueOffsetX(), configuration.getDialogueBoxY() + configuration.getDialogueOffsetY());
                    textboxMenu.addOverlay(textBoxImage);

                    LinkedList<Text> texts = StringFormatter.formatText(dialogue);
                    TextFlowOverlay textFlowOverlay;
                    if (texts.isEmpty()) {
                        Text text = new Text(dialogue);
                        text.setFont(renJava.getConfiguration().getDialogueFont().getFont());
                        textFlowOverlay = new TextFlowOverlay(text, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
                    } else {
                        textFlowOverlay = new TextFlowOverlay(texts, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
                    }
                    textFlowOverlay.setX(configuration.getTextX() + configuration.getTextOffsetX());
                    textFlowOverlay.setY(configuration.getTextY() + configuration.getTextOffsetY());
                    textFlowOverlay.setTextColor(configuration.getDialogueColor());
                    textFlowOverlay.setFont(font);
                    textboxMenu.addOverlay(textFlowOverlay);

                    characterDisplay.setFill(character.getColor());
                    TextOverlay characterText = new TextOverlay(characterDisplay, new FontLoader(configuration.getCharacterDisplayFont(), configuration.getCharacterTextSize()),
                            configuration.getCharacterTextX() + configuration.getCharacterTextOffsetX(),
                            configuration.getCharacterTextY() + configuration.getCharacterTextOffsetY());

                    characterDisplay.setX(configuration.getCharacterTextX() + configuration.getCharacterTextOffsetX());
                    characterDisplay.setY(configuration.getCharacterTextY() + configuration.getCharacterTextOffsetY());

                    textboxMenu.addOverlay(characterText);

                    RenLogger.LOGGER.debug("Textbox Menu Debug: " + textboxMenu.getOverlays().size());

                    rootMenu.addMenu(textboxMenu);

                    RenLogger.LOGGER.debug("Root Menu Debug: " + rootMenu.getChildren().size());
                }
            }
        }


        for (File file : getStyleSheets()) {
            try {
                RenJava.getInstance().getStage().getScene().getStylesheets().add(file.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                renJava.getLogger().error(e.getMessage());
            }
        }

        // Call SceneBuild event
        SceneBuildEvent event = new SceneBuildEvent(this, rootMenu);
        RenJava.callEvent(event);

        return rootMenu;
    }

    @Override
    public void render(Menu menu, boolean update) {
        if (update) {
            renJava.setStage(renJava.getStage(), StageType.IMAGE_SCENE);
        }
        menu.render(this);

        SceneStartEvent event = new SceneStartEvent(this);
        RenJava.callEvent(event);
    }

    @Override
    public StageType getStageType() {
        return StageType.IMAGE_SCENE;
    }
}
