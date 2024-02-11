package me.piitex.renjava.tasks;

import javafx.application.Platform;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.types.SceneEndEvent;

import java.util.AbstractMap;
import java.util.TimerTask;

public class KeyHeldTask extends TimerTask {
    private final RenScene scene;

    private final boolean cancel = false;

    public KeyHeldTask(RenScene scene) {
        this.scene = scene;
    }

    @Override
    public void run() {
        RenJava.getInstance().getLogger().info("Skipping scene...");
        Player player = RenJava.getInstance().getPlayer();
        AbstractMap.SimpleEntry<Story, String> entry = new AbstractMap.SimpleEntry<>(scene.getStory(), scene.getId());
        if (player.getViewedScenes().get(entry) != null) {
            // Player has seen scene
            Platform.runLater(() -> {
                SceneEndEvent endEvent = new SceneEndEvent(scene);
                RenJava.callEvent(endEvent);
            });
        }
    }
}