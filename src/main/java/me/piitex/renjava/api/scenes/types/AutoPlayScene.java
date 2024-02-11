package me.piitex.renjava.api.scenes.types;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.builders.TextFlowBuilder;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.text.StringFormatter;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.SceneBuildEvent;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.TextFlowOverlay;
import me.piitex.renjava.gui.overlay.TextOverlay;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.util.LinkedList;

public class AutoPlayScene extends RenScene {
    private final ImageLoader backgroundImage;

    // FIXME: 2/10/2024 This is redundant. It will be easier to pass a variable with ImageScene rather than make a whole new class.
    private final Character character;
    private final String dialogue;
    private String characterDisplayName;


    private final int duration;

    private static final RenJava renJava = RenJava.getInstance();


    public AutoPlayScene(String id, @Nullable Character character, @Nullable String dialogue, ImageLoader backgroundImage, int duration) {
        super(id, backgroundImage);
        this.character = character;
        this.dialogue = dialogue;
        this.backgroundImage = backgroundImage;
        if (character != null) {
            this.characterDisplayName = character.getDisplayName();
        }
        this.duration = duration;
    }

    @Override
    public Menu build(Stage stage, boolean ui) {
        Menu menu = new Menu(backgroundImage, renJava.getConfiguration().getWidth(), renJava.getConfiguration().getHeight());

        Text characterDisplayText;
        if (character != null) {
            if (characterDisplayName != null) {
                characterDisplayText = new Text(characterDisplayName);
            } else {
                characterDisplayText = new Text(character.getDisplayName());
            }
            characterDisplayText.setFill(character.getColor());
        } else {
            characterDisplayText = new Text("");
        }

        if (dialogue != null && !dialogue.isEmpty()) {
            Image textbox = null;
            try {
                textbox = new ImageLoader("gui/textbox.png").build();
            } catch (ImageNotFoundException e) {
                renJava.getLogger().severe(e.getMessage());
            } finally {
                if (textbox != null) {
                    Menu textBoxMenu = new Menu(renJava.getConfiguration().getWidth(), renJava.getConfiguration().getHeight() - textbox.getHeight());
                    try {
                        ImageOverlay textBoxImage = new ImageOverlay(new ImageLoader("gui/textbox.png").build(), renJava.getConfiguration().getDialogueBoxX() + renJava.getConfiguration().getDialogueOffsetX(),
                                renJava.getConfiguration().getDialogueBoxY() + renJava.getConfiguration().getDialogueOffsetY());
                        textBoxMenu.addOverlay(textBoxImage);
                    } catch (ImageNotFoundException e) {
                        renJava.getLogger().severe(e.getMessage());
                    }

                    LinkedList<Text> texts = StringFormatter.formatText(dialogue);
                    if (texts.isEmpty()) {
                        Text text = new Text(dialogue);
                        text.setFont(renJava.getConfiguration().getDialogueFont().getFont());
                        TextFlowBuilder textFlowBuilder = new TextFlowBuilder(text, renJava.getConfiguration().getDialogueBoxWidth(), renJava.getConfiguration().getDialogueBoxHeight());
                        textBoxMenu.addOverlay(new TextFlowOverlay(textFlowBuilder, renJava.getConfiguration().getTextX() + renJava.getConfiguration().getTextOffsetX(),
                                renJava.getConfiguration().getTextY() + renJava.getConfiguration().getTextOffsetY()));
                    } else {
                        TextFlowBuilder textFlowBuilder = new TextFlowBuilder(texts, renJava.getConfiguration().getDialogueBoxWidth(), renJava.getConfiguration().getDialogueBoxHeight());
                        textBoxMenu.addOverlay(new TextFlowOverlay(textFlowBuilder, renJava.getConfiguration().getTextX() + renJava.getConfiguration().getTextOffsetX(),
                                renJava.getConfiguration().getTextY() + renJava.getConfiguration().getTextOffsetY()));
                    }

                    characterDisplayText.setFont(new FontLoader(renJava.getConfiguration().getDefaultFont().getFont(), renJava.getConfiguration().getCharacterTextSize()).getFont());
                    characterDisplayText.setFill(character.getColor());
                    characterDisplayText.setX(renJava.getConfiguration().getCharacterTextX() + renJava.getConfiguration().getCharacterTextOffsetX());
                    characterDisplayText.setY(renJava.getConfiguration().getCharacterTextY() + renJava.getConfiguration().getCharacterTextOffsetY());

                    textBoxMenu.addOverlay(new TextOverlay(characterDisplayText, characterDisplayText.getX(), characterDisplayText.getY(), 1, 1));
                    menu.addMenu(textBoxMenu);
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

        SceneBuildEvent event = new SceneBuildEvent(this, menu);
        RenJava.callEvent(event);

        return menu;
    }

    @Override
    public void render(Menu menu) {
        menu.render(null, this);
        renJava.setStage(renJava.getStage(), StageType.IMAGE_SCENE);

        SceneStartEvent event = new SceneStartEvent(this);
        RenJava.callEvent(event);
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
