package me.piitex.renjava.events.defaults;

import javafx.scene.control.Button;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.api.saves.Load;
import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.api.saves.exceptions.SaveFileNotFound;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.gui.about.AboutScreenView;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.load.LoadScreenView;
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
            // TODO: 10/20/2023  new LoadScreenView(new ImageLoader("gui/overlay/game_menu.png")).build(RenJava.getInstance().getStage(), true);
            try {
                new Load(1);
            } catch (SaveFileNotFound e) {
                throw new RuntimeException(e);
            }
        }
        if (button.getId().equalsIgnoreCase("menu-preference-button")) {
            new PreferenceScreenView(new ImageLoader("gui/overlay/game_menu.png")).build(RenJava.getInstance().getStage(), true);
        }
        if (button.getId().equalsIgnoreCase("menu-about-button")) {
            new AboutScreenView(new ImageLoader("gui/overlay/game_menu.png")).build(RenJava.getInstance().getStage(), true);
        }
        if (button.getId().equalsIgnoreCase("menu-save-button")) {
            new Save(1, RenJava.getInstance().getPlayer().getCurrentStory().getId(), RenJava.getInstance().getPlayer().getCurrentScene().getId());
        }
    }

}
