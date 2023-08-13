package me.piitex.renjava.events.defaults;

import javafx.scene.control.Button;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.prefernces.PreferenceScreenView;

public class MenuClickEventListener implements EventListener {

    @Listener
    public void onButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();
        if (button.getId().equalsIgnoreCase("menu-start-button")) {
            RenJava.getInstance().getLogger().info("Creating new game...");
            RenJava.getInstance().start();
        }
        if (button.getId().equalsIgnoreCase("menu-load-button")) {
            // FIXME: 8/11/2023 Doing a save for testing (no save button currently)
            new Save(1);
            // Handle load game.
        }
        if (button.getId().equalsIgnoreCase("menu-preference-button")) {
            new PreferenceScreenView(new ImageLoader("gui/overlay/game_menu.png")).build(RenJava.getInstance().getStage());
        }
    }

}
