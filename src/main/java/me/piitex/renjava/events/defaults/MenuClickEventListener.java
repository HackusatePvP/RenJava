package me.piitex.renjava.events.defaults;

import javafx.application.Platform;
import javafx.scene.control.Button;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.gui.StageType;

public class MenuClickEventListener implements EventListener {
    private static final RenJava renJava = RenJava.getInstance();

    @Listener
    public void onButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();
        boolean rightClicked = renJava.getPlayer().isRightClickMenu();
        Window gameWindow = renJava.getGameWindow();

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

            // Caching the save menu may not be a good idea...
            renJava.getPlayer().setCurrentStageType(StageType.LOAD_MENU);
            gameWindow.clearContainers();
            Container load = renJava.buildLoadMenu(1); //TODO: Pages
            Container side = renJava.buildSideMenu(rightClicked);
            side.setOrder(DisplayOrder.HIGH);
            load.addContainers(side);
            gameWindow.addContainer(load);
            gameWindow.render();
        }
        if (button.getId().equalsIgnoreCase("menu-preference-button")) {
            renJava.getPlayer().setCurrentStageType(StageType.OPTIONS_MENU);
            Container container = renJava.getPreferenceMenu();
            Container side = renJava.getSideMenu();
            container.addContainer(side);
            side.setOrder(DisplayOrder.HIGH);
            gameWindow.clearContainers();
            gameWindow.addContainer(container);
            gameWindow.render();

//            renJava.getPlayer().setCurrentStageType(StageType.OPTIONS_MENU);
//            gameWindow.clearContainers();
//            Container settings = renJava.buildSettingsMenu(rightClicked);
//            Container side = renJava.buildSideMenu(rightClicked);
//            side.setOrder(DisplayOrder.HIGH);
//            gameWindow.addContainer(settings);
//            gameWindow.render();
        }
        if (button.getId().equalsIgnoreCase("menu-about-button")) {
            renJava.getPlayer().setCurrentStageType(StageType.ABOUT_MENU);
            Container about;
            if (rightClicked) {
                //TODO
            } else {

            }
//            Container about = renJava.buildAboutMenu(rightClicked);
//            about.addContainers(renJava.buildSideMenu(rightClicked));
//            gameWindow.addContainer(about);
//            gameWindow.render();
        }
        if (button.getId().equalsIgnoreCase("menu-save-button")) {
            renJava.getPlayer().setCurrentStageType(StageType.SAVE_MENU);
            Container menu = renJava.buildLoadMenu(1); // Builds first page
            menu.addContainers(renJava.buildSideMenu(rightClicked));
            gameWindow.addContainer(menu);
            gameWindow.render();
        }
        if (button.getId().equalsIgnoreCase("menu-quit-button")) {
            renJava.getAddonLoader().disable();
            Platform.exit();
        }

        if (button.getId().equalsIgnoreCase("menu-return-button")) {
            if (renJava.getPlayer().getCurrentStageType() == StageType.MAIN_MENU) {
                renJava.getAddonLoader().disable();
                Platform.exit();
                return;
            }
            renJava.getPlayer().setCurrentStageType(StageType.MAIN_MENU);
            Container menu;
            if (rightClicked) {
                menu = renJava.getHelpRightMenu();
                menu.addContainer(renJava.getSideRightMenu());
            } else {
                menu = renJava.getMainMenu();
                menu.addContainer(renJava.getSideMenu());
            }
            gameWindow.clearContainers();
            gameWindow.addContainers(menu);
            gameWindow.render();
        }
    }

}
