package me.piitex.renjava.events.defaults;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.menus.MainMenu;
import me.piitex.renjava.gui.overlays.ButtonOverlay;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.prompts.Prompt;

public class MenuClickEventListener implements EventListener {
    private static final RenJava renJava = RenJava.getInstance();

    @Listener
    public void onButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();
        boolean rightClicked = RenJava.PLAYER.isRightClickMenu();
        Window gameWindow = renJava.getGameWindow();
        MainMenu mainMenu = renJava.getMainMenu();

        if (button.getId().equalsIgnoreCase("menu-start-button")) {
            RenLogger.LOGGER.info("Creating new game...");
            RenJava.PLAYER.resetSession();
            renJava.createBaseData();
            renJava.createStory();

            // Call GameStartEvent
            GameStartEvent event1 = new GameStartEvent(renJava);
            RenJava.getEventHandler().callEvent(event1);

            renJava.start();
        }
        if (button.getId().equalsIgnoreCase("menu-load-button") && RenJava.PLAYER.getCurrentStageType() != StageType.LOAD_MENU) {
            // Caching the save menu may not be a good idea...
            RenJava.PLAYER.setCurrentStageType(StageType.LOAD_MENU);

            Container load = mainMenu.loadMenu(rightClicked, 1, true); //TODO: Pages
            Container side = mainMenu.sideMenu(rightClicked);
            side.setOrder(DisplayOrder.HIGH);
            load.addContainers(side);

            gameWindow.clearContainers();
            gameWindow.addContainer(load);
            gameWindow.render();

        }
        if (button.getId().equalsIgnoreCase("menu-preference-button") && RenJava.PLAYER.getCurrentStageType() != StageType.OPTIONS_MENU) {
            RenJava.PLAYER.setCurrentStageType(StageType.OPTIONS_MENU);
            Container container = mainMenu.settingMenu(rightClicked);
            Container side = mainMenu.sideMenu(rightClicked);
            container.addContainer(side);

            gameWindow.clearContainers();

            gameWindow.addContainer(container);
            gameWindow.render();
        }
        if (button.getId().equalsIgnoreCase("menu-about-button") && RenJava.PLAYER.getCurrentStageType() != StageType.ABOUT_MENU) {
            RenJava.PLAYER.setCurrentStageType(StageType.ABOUT_MENU);
            Container container = mainMenu.aboutMenu(rightClicked);
            Container side = mainMenu.sideMenu(rightClicked);
            side.setOrder(DisplayOrder.HIGH);
            container.addContainer(side);

            gameWindow.clearContainers();

            gameWindow.addContainer(container);
            gameWindow.render();
        }
        if (button.getId().equalsIgnoreCase("menu-save-button")) {
            RenJava.PLAYER.setCurrentStageType(StageType.SAVE_MENU);

            Container menu = mainMenu.loadMenu(rightClicked,1, false); // Builds first page
            Container side = mainMenu.sideMenu(rightClicked);
            side.setOrder(DisplayOrder.HIGH);
            menu.addContainers(side);

            gameWindow.clearContainers();

            gameWindow.addContainer(menu);
            gameWindow.render();
        }
        if (button.getId().equalsIgnoreCase("menu-quit-button")) {
            // Prompt before exiting...
            RenJava.ADDONLOADER.disable();
            Platform.exit();

        }

        if (button.getId().equalsIgnoreCase("menu-return-button")) {

            // Prompt them before quitting to main menu.

            // Only prompt if they are in a save.
            if (RenJava.PLAYER.getCurrentScene() != null) {

                Prompt prompt = new Prompt("Are you sure you want to exit to main menu?");

                Window window = prompt.getPromptWindow();

                ButtonOverlay confirm = new ButtonOverlay("yes", "Confirm", Color.WHITE, RenJava.CONFIGURATION.getUiFont());
                confirm.setX(10);
                confirm.setY(300);

                confirm.onClick(event1 -> {
                    // Exit to the main menu
                    RenJava.PLAYER.setCurrentStageType(StageType.MAIN_MENU);
                    Container menu = mainMenu.mainMenu(false);
                    Container side = mainMenu.sideMenu(false);
                    RenJava.PLAYER.resetSession();
                    side.setOrder(DisplayOrder.HIGH);
                    menu.addContainer(side);
                    gameWindow.clearContainers();
                    gameWindow.addContainers(menu);
                    gameWindow.render();

                    // Close the prompt
                    prompt.closeWindow();
                });

                prompt.addOverlay(confirm);

                ButtonOverlay cancel = new ButtonOverlay("cancel", "Cancel", Color.WHITE, RenJava.CONFIGURATION.getUiFont());
                cancel.setX(700);
                cancel.setY(300);

                cancel.onClick(event1 -> {
                    // Executes when they cancel the prompt
                    window.close();
                });

                prompt.addOverlay(cancel);

                prompt.render();
            } else {
                // Exit to the main menu
                RenJava.PLAYER.setCurrentStageType(StageType.MAIN_MENU);
                Container menu = mainMenu.mainMenu(false);
                Container side = mainMenu.sideMenu(false);
                RenJava.PLAYER.resetSession();
                side.setOrder(DisplayOrder.HIGH);
                menu.addContainer(side);
                gameWindow.clearContainers();
                gameWindow.addContainers(menu);
                gameWindow.render();
            }
        }
    }

}
