package me.piitex.renjava.events.defaults;

import javafx.application.Platform;
import javafx.scene.control.Button;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.api.saves.Load;
import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.api.saves.exceptions.SaveFileNotFound;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.gui.Menu;

public class MenuClickEventListener implements EventListener {
    private static final RenJava renJava = RenJava.getInstance();

    @Listener
    public void onButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();
        if (button.getId().equalsIgnoreCase("menu-start-button")) {
            renJava.getLogger().info("Creating new game...");
            renJava.start();
        }
        if (button.getId().equalsIgnoreCase("menu-load-button")) {
            // NOTE: 10/20/2023  new LoadScreenView(new ImageLoader("gui/overlay/game_menu.png")).build(renJava.getStage(), true);
            try {
                new Load(1);
            } catch (SaveFileNotFound e) {
                throw new RuntimeException(e);
            }
        }
        if (button.getId().equalsIgnoreCase("menu-preference-button")) {
            //new PreferenceScreenView(new ImageLoader("gui/overlay/game_menu.png")).build(renJava.getStage(), true);
            Menu settings = renJava.buildSettingsScreen();
            settings.addMenu(renJava.buildSideMenu());

            settings.render(null, null);
        }
        if (button.getId().equalsIgnoreCase("menu-about-button")) {
            Menu about = renJava.buildAboutScreen();
            about.addMenu(renJava.buildSideMenu());

            about.render(null, null);
        }
        if (button.getId().equalsIgnoreCase("menu-save-button")) {
            new Save(1, renJava.getPlayer().getCurrentStory().getId(), renJava.getPlayer().getCurrentScene().getId());
        }
        if (button.getId().equalsIgnoreCase("menu-quit-button")) {
            renJava.getAddonLoader().disable();
            Platform.exit();
        }
    }

}
