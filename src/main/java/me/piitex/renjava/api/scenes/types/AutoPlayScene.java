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
    public Menu build(boolean ui) {
        return new ImageScene(null, character, dialogue, backgroundImage).build(ui);
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
