package me.piitex.renjava.events.defaults;

import javafx.application.Platform;
import javafx.scene.control.Button;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.api.saves.exceptions.SaveFileNotFound;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.ButtonClickEvent;
import me.piitex.renjava.events.types.GameStartEvent;
import me.piitex.renjava.gui.Menu;

public class MenuClickEventListener implements EventListener {
    private static final RenJava renJava = RenJava.getInstance();

    @Listener
    public void onButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();
        if (button.getId().equalsIgnoreCase("menu-start-button")) {
            renJava.getLogger().info("Creating new game...");
            renJava.createBaseData();
            renJava.createStory();

            // Call GameStartEvent
            GameStartEvent event1 = new GameStartEvent(renJava);
            RenJava.callEvent(event1);

            renJava.start();
        }
        if (button.getId().equalsIgnoreCase("menu-load-button")) {
            // NOTE: 10/20/2023  new LoadScreenView(new ImageLoader("gui/overlay/game_menu.png")).build(renJava.getStage(), true);
            new Save(1).load(true);


            // After loading play the current scene and story
            String storyID = renJava.getPlayer().getCurrentStoryID();
            if (storyID == null) {
                renJava.getLogger().severe("Save file could not be loaded. The data is either not formatted or corrupted.");
                return;
            }

            System.out.println("Story: " + renJava.getPlayer().getCurrentStoryID());

            renJava.createStory();

            // Force update fields
            renJava.getPlayer().setCurrentStory(storyID);
            renJava.getPlayer().getCurrentStory().init(); // Re-initialize story

            renJava.getPlayer().setCurrentScene(renJava.getPlayer().getCurrentSceneID());

            renJava.getPlayer().getCurrentStory().displayScene(renJava.getPlayer().getCurrentSceneID());
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
            //new Save(1, renJava.getPlayer().getCurrentStory().getId(), renJava.getPlayer().getCurrentScene().getId());
            new Save(1).write(); // Writes the save.
        }
        if (button.getId().equalsIgnoreCase("menu-quit-button")) {
            renJava.getAddonLoader().disable();
            Platform.exit();
        }
    }

}
