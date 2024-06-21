package me.piitex.renjava.events.defaults;

import javafx.application.Platform;
import javafx.scene.control.Button;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.events.types.*;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.gui.Menu;
import me.piitex.renjava.gui.StageType;

public class MenuClickEventListener implements EventListener {
    private static final RenJava renJava = RenJava.getInstance();

    @Listener
    public void onButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();
        boolean rightClicked = renJava.getPlayer().isRightClickMenu();

        if (button.getId().equalsIgnoreCase("menu-start-button")) {
            RenLogger.LOGGER.info("Creating new game...");
            renJava.createBaseData();
            renJava.createStory();

            // Call GameStartEvent
            GameStartEvent event1 = new GameStartEvent(renJava);
            RenJava.callEvent(event1);

            renJava.start();
        }
        if (button.getId().equalsIgnoreCase("menu-load-button")) {
            renJava.setStage(renJava.getStage(), StageType.LOAD_MENU);
            Menu menu = renJava.buildLoadMenu(1); // Builds first page
            menu.addMenu(renJava.buildSideMenu(rightClicked));
            menu.render();
        }
        if (button.getId().equalsIgnoreCase("menu-preference-button")) {
            renJava.setStage(renJava.getStage(), StageType.OPTIONS_MENU);
            Menu settings = renJava.buildSettingsMenu();
            settings.addMenu(renJava.buildSideMenu(rightClicked));
            settings.render();
        }
        if (button.getId().equalsIgnoreCase("menu-about-button")) {
            renJava.setStage(renJava.getStage(), StageType.ABOUT_MENU);
            Menu about = renJava.buildAboutMenu();
            about.addMenu(renJava.buildSideMenu(rightClicked));
            about.render();
        }
        if (button.getId().equalsIgnoreCase("menu-save-button")) {
            renJava.setStage(renJava.getStage(), StageType.SAVE_MENU);
            Menu menu = renJava.buildLoadMenu(1); // Builds first page
            menu.addMenu(renJava.buildSideMenu(rightClicked));
            menu.render();
        }
        if (button.getId().equalsIgnoreCase("menu-quit-button")) {
            renJava.getAddonLoader().disable();
            Platform.exit();
        }

        // Handle loading and saving buttons
//        if (renJava.getStageType() == StageType.LOAD_MENU) {
//            // Loading
//            if (button.getId().contains("save-")) {
//                int slot = Integer.parseInt(button.getId().replace("save-", ""));
//                Save save = new Save(slot);
//                if (!save.exists()) {
//                    RenLogger.LOGGER.warn("Save file does not exist.");
//                    return;
//                }
//
//                save.load(true);
//
//                String storyID = renJava.getPlayer().getCurrentStoryID();
//                if (storyID == null) {
//                    RenLogger.LOGGER.error("Save file could not be loaded. The data is either not formatted or corrupted.");
//                    return;
//                }
//
//                renJava.createStory();
//
//                // Force update fields
//                renJava.getPlayer().setCurrentStory(storyID);
//                renJava.getPlayer().getCurrentStory().init(); // Re-initialize story
//
//                renJava.getPlayer().setCurrentScene(renJava.getPlayer().getCurrentSceneID());
//
//                renJava.getPlayer().getCurrentStory().displayScene(renJava.getPlayer().getCurrentSceneID());
//            }
//        }
//        if (renJava.getStageType() == StageType.SAVE_MENU) {
//            // Save
//            System.out.println("Button id: " + button.getId());
//            if (!button.getId().startsWith("save-")) return;
//            int slot = Integer.parseInt(button.getId().replace("save-", ""));
//            Save save = new Save(slot);
//            save.write();
//
//            // Re-render
//            renJava.setStage(renJava.getStage(), StageType.SAVE_MENU);
//            Menu menu = renJava.buildLoadMenu(1); // Builds first page
//            menu.addMenu(renJava.buildSideMenu(true));
//            menu.render();
//        }
        if (button.getId().equalsIgnoreCase("menu-return-button")) {
            if (renJava.getStageType() == StageType.MAIN_MENU) {
                renJava.getAddonLoader().disable();
                Platform.exit();
                return;
            }
            renJava.setStage(renJava.getStage(), StageType.MAIN_MENU);
            Menu menu = renJava.buildTitleScreen(rightClicked);
            menu.addMenu(renJava.buildSideMenu(rightClicked));
            menu.render();
        }
    }

}
