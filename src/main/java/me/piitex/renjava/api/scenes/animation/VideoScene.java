package me.piitex.renjava.api.scenes.animation;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.text.StringFormatter;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.overlays.*;

import java.util.LinkedList;

public class VideoScene extends RenScene {
    private Character character;
    private String filePath;

    // Video scenes can have a textbox
    private String dialogue;
    private FontLoader font;
    private String characterDisplayName;
    private boolean loop = false;

    private MediaOverlay media;

    private final RenJavaConfiguration configuration;

    private static final RenJava renJava = RenJava.getInstance();

    public VideoScene(String id, String mediaFilePath) {
        super(id, null);
        filePath = mediaFilePath;
        this.character = null;
        this.font = null;
        configuration = RenJava.getInstance().getConfiguration();
    }

    public VideoScene(String id, String mediaFilePath, boolean loop) {
        super(id, null);
        filePath = mediaFilePath;
        this.character = null;
        this.font = null;
        this.loop = loop;
        configuration = RenJava.getInstance().getConfiguration();
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

    public MediaOverlay getMedia() {
        return media;
    }

    @Override
    public Container build(boolean ui) {
        Container container = new EmptyContainer(configuration.getWidth(), configuration.getHeight());

        // Render video first
        MediaOverlay mediaOverlay = new MediaOverlay(filePath, 0, 0, configuration.getWidth(), configuration.getHeight());
        mediaOverlay.setLoop(loop);
        container.addOverlay(mediaOverlay);

        this.media = mediaOverlay;

        if (dialogue != null && !dialogue.isEmpty()) {
            // Render TextBox
            String characterDisplay;
            if (character != null) {
                if (getCharacterNameDisplay() != null) {
                    // Set character display
                    characterDisplay = getCharacterNameDisplay();
                } else {
                    characterDisplay = character.getDisplayName();
                }
                ImageLoader textbox = new ImageLoader("gui/textbox.png");
                Container textboxMenu = new EmptyContainer(0, 0, configuration.getDialogueBoxWidth(), configuration.getDialogueBoxHeight());

                ImageOverlay textBoxImage = new ImageOverlay(textbox, configuration.getDialogueBoxX() + configuration.getDialogueOffsetX(), configuration.getDialogueBoxY() + configuration.getDialogueOffsetY());
                textboxMenu.addOverlay(textBoxImage);

                LinkedList<Overlay> texts = StringFormatter.formatText(dialogue);
                TextFlowOverlay textFlowOverlay;
                if (texts.isEmpty()) {
                    TextOverlay text = new TextOverlay(dialogue);
                    text.setFontLoader(renJava.getConfiguration().getDialogueFont());
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

        return container;
    }

    @Override
    public void render(Window window, boolean ui) {
        Container container = build(ui);

        // Clear window
        window.clearContainers();

        window.addContainer(container);

        window.render();

        SceneStartEvent event = new SceneStartEvent(this);
        RenJava.callEvent(event);
    }

    @Override
    public StageType getStageType() {
        return StageType.ANIMATION_SCENE;
    }
}
