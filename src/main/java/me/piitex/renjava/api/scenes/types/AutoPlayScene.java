package me.piitex.renjava.api.scenes.types;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;
import org.jetbrains.annotations.Nullable;

public class AutoPlayScene extends RenScene {
    private final ImageLoader backgroundImage;

    private final Character character;
    private final String dialogue;

    private final int duration;

    private static final RenJava renJava = RenJava.getInstance();

    public AutoPlayScene(String id, @Nullable Character character, @Nullable String dialogue, ImageLoader backgroundImage, int duration) {
        super(id, backgroundImage);
        this.character = character;
        this.dialogue = dialogue;
        this.backgroundImage = backgroundImage;
        this.duration = duration;
    }

    @Override
    public Menu build(boolean ui) {
        return new ImageScene(null, character, dialogue, backgroundImage).build(ui);
    }

    @Override
    public void render(Menu menu) {
        System.out.println("Rendering: " + getId());
        menu.render();
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
