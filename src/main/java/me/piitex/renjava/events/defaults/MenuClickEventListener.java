package me.piitex.renjava.events.defaults;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import me.piitex.renjava.gui.overlay.Overlay;
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
            // NOTE: 10/20/2023  new LoadScreenView(new ImageLoader("gui/overlay/game_menu.png")).build(renJava.getStage(), true);
            Menu menu = renJava.buildLoadMenu(1); // Builds first page
            menu.addMenu(renJava.buildSideMenu());
            menu.render();
            renJava.setStage(renJava.getStage(), StageType.LOAD_MENU); // Update stage type
        }
        if (button.getId().equalsIgnoreCase("menu-preference-button")) {
            //new PreferenceScreenView(new ImageLoader("gui/overlay/game_menu.png")).build(renJava.getStage(), true);
            Menu settings = renJava.buildSettingsMenu();
            settings.addMenu(renJava.buildSideMenu());

            settings.render();
        }
        if (button.getId().equalsIgnoreCase("menu-about-button")) {
            Menu about = renJava.buildAboutMenu();
            about.addMenu(renJava.buildSideMenu());

            about.render();
            renJava.setStage(renJava.getStage(), StageType.ABOUT_MENU);
        }
        if (button.getId().equalsIgnoreCase("menu-save-button")) {
            //new Save(1, renJava.getPlayer().getCurrentStory().getId(), renJava.getPlayer().getCurrentScene().getId());
            Menu menu = renJava.buildLoadMenu(1); // Builds first page
            menu.addMenu(renJava.buildSideMenu());
            menu.render();

            renJava.setStage(renJava.getStage(), StageType.SAVE_MENU);
        }
        if (button.getId().equalsIgnoreCase("menu-quit-button")) {
            renJava.getAddonLoader().disable();
            Platform.exit();
        }

        // Handle loading and saving buttons
        if (renJava.getStageType() == StageType.LOAD_MENU) {
            // Loading
            if (button.getId().contains("save-")) {
                int slot = Integer.parseInt(button.getId().replace("save-", ""));
                Save save = new Save(slot);
                if (!save.exists()) {
                    RenLogger.LOGGER.warn("Save file does not exist.");
                    return;
                }

                save.load(true);

                String storyID = renJava.getPlayer().getCurrentStoryID();
                if (storyID == null) {
                    RenLogger.LOGGER.error("Save file could not be loaded. The data is either not formatted or corrupted.");
                    return;
                }

                renJava.createStory();

                // Force update fields
                renJava.getPlayer().setCurrentStory(storyID);
                renJava.getPlayer().getCurrentStory().init(); // Re-initialize story

                renJava.getPlayer().setCurrentScene(renJava.getPlayer().getCurrentSceneID());

                renJava.getPlayer().getCurrentStory().displayScene(renJava.getPlayer().getCurrentSceneID());
            }
        }
        if (renJava.getStageType() == StageType.SAVE_MENU) {
            // Save
            System.out.println("Button id: " + button.getId());
            if (!button.getId().startsWith("save-")) return;
            int slot = Integer.parseInt(button.getId().replace("save-", ""));
            Save save = new Save(slot);
            save.write();

            // Re-render
            Menu menu = renJava.buildLoadMenu(1); // Builds first page
            menu.addMenu(renJava.buildSideMenu());
            menu.render();
            renJava.setStage(renJava.getStage(), StageType.SAVE_MENU); // Update stage type
        }
    }

    @Listener
    public void onOverlayClick(OverlayClickEvent event) {
        Overlay overlay = event.getOverlay();

        if (overlay.getOnClick() != null) {
            overlay.getOnClick().onClick(event);
        }
    }

    @Listener
    public void onOverlayHover(OverlayHoverEvent event) {
        Overlay overlay = event.getOverlay();

        if (overlay.getOnHover() != null) {
            overlay.getOnHover().onHover(event);
        }

        if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.getButton();

            if (buttonOverlay.isHover()) {
                if (buttonOverlay.getHoverColor() != null) {
                   button.setTextFill(buttonOverlay.getHoverColor());
                }
                if (buttonOverlay.getHoverImage() != null) {
                    try {
                        button.setGraphic(new ImageView(buttonOverlay.getHoverImage().build()));
                    } catch (ImageNotFoundException e) {
                        RenLogger.LOGGER.error(e.getMessage());
                    }
                }
            }
        }
    }

    @Listener
    public void onOverlayExit(OverlayExitEvent event) {
        Overlay overlay = event.getOverlay();

        if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.getButton();
            button.setTextFill(buttonOverlay.getTextFill());
            button.setStyle(button.getStyle()); // Re-init the style (hopefully this forces the button back to normal.)
            if (buttonOverlay.getImage() != null) {
                button.setGraphic(new ImageView(buttonOverlay.getImage().getImage()));
            }
        }
    }

}
