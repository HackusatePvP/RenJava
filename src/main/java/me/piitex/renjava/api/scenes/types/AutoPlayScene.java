package me.piitex.renjava.api.scenes.types;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import org.jetbrains.annotations.Nullable;

public class AutoPlayScene extends RenScene {
    private ImageOverlay backgroundImage;

    private final Character character;
    private final String dialogue;

    private final int duration;

    private boolean useImage = true;

    private static final RenJava renJava = RenJava.getInstance();

    public AutoPlayScene(String id, @Nullable Character character, @Nullable String dialogue, ImageOverlay backgroundImage, int duration) {
        super(id, backgroundImage);
        this.character = character;
        this.dialogue = dialogue;
        this.backgroundImage = backgroundImage;
        this.duration = duration;
    }

    public AutoPlayScene(String id, @Nullable Character character, @Nullable String dialogue, int duration) {
        super(id, null);
        this.character = character;
        this.dialogue = dialogue;
        this.duration = duration;
        useImage = false;
    }

    @Override
    public Container build(boolean ui) {
        if (useImage) {
            return new ImageScene(null, character, dialogue, backgroundImage).build(ui);
        } else {
            return new ImageScene(null, character, dialogue).build(ui);
        }
    }

    @Override
    public void render(Window window, boolean ui) {
        Container container = build(ui);

        window.clearContainers();

        window.addContainer(container);

        window.render();

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
