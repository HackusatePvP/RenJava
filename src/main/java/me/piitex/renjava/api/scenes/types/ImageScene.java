package me.piitex.renjava.api.scenes.types;

import javafx.scene.paint.Color;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.text.StringFormatter;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.SceneBuildEvent;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.loaders.ImageLoader;

import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.overlays.*;
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
 * TODO
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
    private FontLoader font;
    private String characterDisplayName;

    private final RenJavaConfiguration configuration;

    private static final RenJava renJava = RenJava.getInstance();

    /**
     * Creates an ImageScene object representing an image scene in the RenJava framework.
     * An image scene is used to display an image and text associated with a character.
     * Image scenes are typically used to present visuals and dialogue during gameplay or narrative progression.
     *
     * <p>
     *     <pre>
     *         {@code
     *           ImageScene scene = new ImageScene("1", null, null, null); // This is just an empty black scene
     *           ImageScene scene = new ImageScene("2", character, "This is dialogue", new ImageOverlay("image.png"); // This is a basic scene with an image and dialogue.
     *         }
     *     </pre>
     * </p>
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
            RenJava.PLAYER.setLastDisplayedImage(new AbstractMap.SimpleEntry<>(getStory().getId(), backgroundImage));
        }
        if (character != null) {
            this.characterDisplayName = character.getDisplayName();
        }
        configuration = RenJava.CONFIGURATION;
        font = configuration.getDialogueFont();
    }

    public ImageScene(String id, @Nullable Character character, String dialogue) {
        super(id, null);
        this.character = character;
        this.dialogue = dialogue;
        backgroundImage = RenJava.PLAYER.getLastDisplayedImage().getValue();
        setBackgroundImage(backgroundImage);
        configuration = RenJava.CONFIGURATION;
        font = configuration.getDialogueFont();
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

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    public FontLoader getDialogueFont() {
        return font;
    }

    public void setDialogueFont(FontLoader font) {
        this.font = font;
    }

    @Override
    public Container build(boolean ui) {
        Container container = new EmptyContainer(configuration.getWidth(), configuration.getHeight());
        if (backgroundImage != null) {
            backgroundImage.setOrder(DisplayOrder.LOW); // Bg should always be at low priority. They will be pushed to the back of the scene.
            container.addOverlays(backgroundImage);
        } else {
            BoxOverlay boxOverlay = new BoxOverlay(container.getWidth(), configuration.getHeight(), Color.BLACK);
            boxOverlay.setOrder(DisplayOrder.LOW);
            container.addOverlay(boxOverlay);
        }

        if (ui) {
            String characterDisplay;
            if (character != null) {
                if (getCharacterNameDisplay() != null) {
                    // Set character display
                    characterDisplay = getCharacterNameDisplay();
                } else {
                    characterDisplay = character.getDisplayName();
                }

                if (dialogue != null && !dialogue.isEmpty()) {
                    ImageLoader textbox = new ImageLoader("gui/textbox.png");
                    Container textboxMenu = new EmptyContainer(0, 0, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());

                    ImageOverlay textBoxImage = new ImageOverlay(textbox, configuration.getDialogueBoxX() + configuration.getDialogueOffsetX(), configuration.getDialogueBoxY() + configuration.getDialogueOffsetY());
                    textboxMenu.addOverlay(textBoxImage);

                    LinkedList<Overlay> texts = StringFormatter.formatText(dialogue);
                    TextFlowOverlay textFlowOverlay;
                    if (texts.isEmpty()) {
                        TextOverlay text = new TextOverlay(dialogue);
                        text.setFont(RenJava.CONFIGURATION.getDialogueFont());
                        textFlowOverlay = new TextFlowOverlay(text, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
                    } else {
                        textFlowOverlay = new TextFlowOverlay(texts, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());
                    }
                    textFlowOverlay.setX(configuration.getTextX() + configuration.getTextOffsetX());
                    textFlowOverlay.setY(configuration.getTextY() + configuration.getTextOffsetY());
                    textFlowOverlay.setTextFillColor(configuration.getDialogueColor());
                    textFlowOverlay.setFont(font);
                    textboxMenu.addOverlay(textFlowOverlay);

                    TextOverlay characterText = new TextOverlay(characterDisplay, new FontLoader(configuration.getCharacterDisplayFont(), configuration.getCharacterTextSize()),
                            configuration.getCharacterTextX() + configuration.getCharacterTextOffsetX(),
                            configuration.getCharacterTextY() + configuration.getCharacterTextOffsetY());
                    characterText.setTextFill(character.getColor());
                    characterText.setOrder(DisplayOrder.HIGH);
                    textboxMenu.addOverlay(characterText);

                    container.addContainers(textboxMenu);
                }
            }
        }


        for (File file : getStyleSheets()) {
            try {
                RenJava.getInstance().getGameWindow().getScene().getStylesheets().add(file.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                renJava.getLogger().error(e.getMessage());
            }
        }

        // Call SceneBuild event.
        SceneBuildEvent event = new SceneBuildEvent(this, container);
        RenJava.getEventHandler().callEvent(event);

        return container;
    }

    @Override
    public StageType getStageType() {
        return StageType.IMAGE_SCENE;
    }
}
