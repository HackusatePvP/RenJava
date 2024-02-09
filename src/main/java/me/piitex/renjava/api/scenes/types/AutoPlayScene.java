package me.piitex.renjava.api.scenes.types;

import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.types.SceneBuildEvent;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;

public class AutoPlayScene extends RenScene {
    private final ImageLoader backgroundImage;

    private final int duration;

    private static final RenJava renJava = RenJava.getInstance();

    public AutoPlayScene(String id, ImageLoader backgroundImage, int duration) {
        super(id, backgroundImage);
        this.backgroundImage = backgroundImage;
        this.duration = duration;
    }

    @Override
    public Menu build(Stage stage, boolean ui) {
        Menu menu = new Menu(backgroundImage, renJava.getConfiguration().getWidth(), renJava.getConfiguration().getHeight());

        SceneBuildEvent event = new SceneBuildEvent(this, menu);
        RenJava.callEvent(event);

        return menu;
    }

    @Override
    public void render(Menu menu) {
        menu.render(null, this);
        renJava.setStage(renJava.getStage(), StageType.IMAGE_SCENE);
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
