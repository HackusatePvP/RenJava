package me.piitex.renjava.gui.menus;

import javafx.scene.paint.Color;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.SideMenuBuildEvent;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.layouts.HorizontalLayout;
import me.piitex.renjava.gui.layouts.VerticalLayout;
import me.piitex.renjava.gui.overlays.*;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.tasks.Tasks;

import java.util.LinkedList;

public class DefaultMainMenu implements MainMenu {

    @Override
    public Container mainMenu(boolean rightClicked) {
        // Empty container behaves like the old menu system.
        // It is essentially an empty box which you add overlays to.
        Container menu = new EmptyContainer(RenJava.CONFIGURATION.getWidth(), RenJava.CONFIGURATION.getHeight());
        menu.addOverlay(new ImageOverlay("gui/main_menu.png"));

        // Basic text overlay
        TextOverlay gameText = new TextOverlay(RenJava.getInstance().getName() + ' ' + RenJava.getInstance().getVersion(), new FontLoader(RenJava.CONFIGURATION.getUiFont(), 36), 1500, 975);

        // Add the overlay to the container
        menu.addOverlay(gameText);

        return menu;
    }

    @Override
    public Container sideMenu(boolean rightClick) {
        Container menu = new EmptyContainer(1920, 1080, DisplayOrder.HIGH);

        ImageOverlay imageOverlay = new ImageOverlay("gui/overlay/main_menu.png");
        imageOverlay.setOrder(DisplayOrder.LOW);
        menu.addOverlay(imageOverlay);

        FontLoader uiFont = RenJava.CONFIGURATION.getUiFont();

        Color hoverColor = RenJava.CONFIGURATION.getHoverColor();

        ButtonOverlay startButton = new ButtonOverlay("menu-start-button", "Start", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        ButtonOverlay loadButton = new ButtonOverlay("menu-load-button", "Load", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        ButtonOverlay saveButton = new ButtonOverlay("menu-save-button", "Save", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        ButtonOverlay optionsButton = new ButtonOverlay("menu-preference-button", "Preferences", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        ButtonOverlay aboutButton = new ButtonOverlay("menu-about-button", "About", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        // Create vbox for the buttons. You can also do an HBox
        VerticalLayout layout = new VerticalLayout(400, 500);
        layout.setOrder(DisplayOrder.HIGH);
        layout.setX(50);
        layout.setY(250);
        layout.setSpacing(20);
        layout.addOverlays(startButton, loadButton);
        if (rightClick) {
            layout.addOverlays(saveButton);
        }
        layout.addOverlays(optionsButton, aboutButton);

        // You don't have to add the button overlays just add the layout which already contains the overlays.
        menu.addLayout(layout);

        ButtonOverlay returnButton;

        if (!RenJava.PLAYER.isRightClickMenu()) {
            returnButton = new ButtonOverlay("menu-quit-button", "Quit", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        } else {
            returnButton = new ButtonOverlay("menu-return-button", "Return", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        }
        returnButton.setX(25);
        returnButton.setY(980);
        menu.addOverlay(returnButton);

        SideMenuBuildEvent sideMenuBuildEvent = new SideMenuBuildEvent(menu);
        RenJava.callEvent(sideMenuBuildEvent);

        return menu;
    }

    @Override
    public Container loadMenu(boolean rightClicked, int page, boolean loadMenu) {
        Container menu = new EmptyContainer(RenJava.CONFIGURATION.getWidth(), RenJava.CONFIGURATION.getHeight(), DisplayOrder.NORMAL);
        ImageOverlay imageOverlay = new ImageOverlay("gui/main_menu.png");
        imageOverlay.setOrder(DisplayOrder.LOW);
        menu.addOverlay(imageOverlay);

        TextOverlay menuText;
        if (loadMenu) {
            menuText = new TextOverlay("LOAD", RenJava.CONFIGURATION.getUiFont());
        } else {
            menuText = new TextOverlay("SAVE", RenJava.CONFIGURATION.getUiFont());
        }
        menuText.setX(RenJava.CONFIGURATION.getMidPoint().getKey());
        menuText.setY(50);

        menu.addOverlay(menuText);

        // Setup pagination.
        // 6 save slots per page
        //   2 Rows
        //   3 Columns
        // Make this customizable

        int maxSavesPerPage = 6;

        int index = ((maxSavesPerPage * page) - maxSavesPerPage) + 1;
        VerticalLayout rootLayout = new VerticalLayout(1000, 800); // The root is a vertical which stacks the two horizontal layouts.
        rootLayout.setSpacing(10);
        HorizontalLayout topLayout = new HorizontalLayout(1000, 350);
        topLayout.setSpacing(20);
        HorizontalLayout bottomLayout = new HorizontalLayout(1000, 350);
        bottomLayout.setSpacing(20);
        // Save the containers to re-render the view (refreshes)

        Window gameWindow = RenJava.getInstance().getGameWindow();

        LinkedList<Container> containers = new LinkedList<>(gameWindow.getContainers());
        while (index <=  maxSavesPerPage) {
            Save save = new Save(index);
            save.load(false);
            // Create a button box
            VerticalLayout buttonBox = new VerticalLayout(414, 320);

            ButtonOverlay loadButton = savePreview(save, page, index);
            loadButton.setOrder(DisplayOrder.HIGH);
            buttonBox.addOverlay(loadButton);

            FontLoader bottomFont = new FontLoader(RenJava.CONFIGURATION.getUiFont(), 12);
            TextOverlay createdTime = new TextOverlay(save.getLocalizedCreationDate(), bottomFont);
            createdTime.setY(-50); // Moves the text up a little in the vbox
            createdTime.setX(120); // Moves over to the center hopefully
            buttonBox.addOverlay(createdTime);
            if (save.getName() != null && !save.getName().isEmpty() && !save.getName().equalsIgnoreCase("null")) {
                TextOverlay saveName = new TextOverlay(save.getName(), bottomFont);
                buttonBox.addOverlay(saveName);
            }

            if (index <= 3) {
                topLayout.addChildLayout(buttonBox);
            } else {
                bottomLayout.addChildLayout(buttonBox);
            }
            index++;
        }

        // Once the fetching is done re-render the view.
        gameWindow.setContainers(containers);
        gameWindow.render();

        rootLayout.addChildLayout(topLayout);
        rootLayout.addChildLayout(bottomLayout);

        rootLayout.setX(500);
        rootLayout.setY(250);


        menu.addLayout(rootLayout);

        // Add Page buttons below.
        // There should be 8 per view.
        int pageViewMax = 8;
        int pageIndex = 0;
        HorizontalLayout pageLayout = new HorizontalLayout(100, 100);
        while (pageIndex < pageViewMax) {
            pageIndex++;
            ButtonOverlay pageButton = new ButtonOverlay("page-" + pageIndex, pageIndex + "", Color.BLACK, new FontLoader(RenJava.CONFIGURATION.getUiFont(), 26), 1, 1);
            pageButton.setBackgroundColor(Color.TRANSPARENT);
            pageButton.setBorderColor(Color.TRANSPARENT);
            if (page == pageIndex) {
                pageButton.setTextFill(Color.BLACK);
            }
            pageButton.setHoverColor(RenJava.CONFIGURATION.getHoverColor());
            pageLayout.addOverlay(pageButton);
        }
        pageLayout.setX(1000);
        pageLayout.setY(950);

        menu.addLayout(pageLayout);

        return menu;
    }

    @Override
    public Container settingMenu(boolean rightClicked) {
        Container menu = new EmptyContainer(RenJava.CONFIGURATION.getWidth(), RenJava.CONFIGURATION.getHeight());
        ImageOverlay imageOverlay = new ImageOverlay("gui/main_menu.png");
        imageOverlay.setOrder(DisplayOrder.LOW);
        menu.addOverlay(imageOverlay);

        Color themeColor = RenJava.CONFIGURATION.getThemeColor();
        Color subColor = RenJava.CONFIGURATION.getSubColor();

        // 1 hbox 3 vboxes

        // Display    Rollback     Skip
        // Windowed     Disabled   Unseen Text
        // Full screen  Enabled    After Choices
        //              Right      Transitions


        HorizontalLayout rootLayout = new HorizontalLayout(1200, 600);
        rootLayout.setX(580);
        rootLayout.setY(100);
        rootLayout.setSpacing(100);

        VerticalLayout displayBox = new VerticalLayout(300, 400);
        TextOverlay displayText = new TextOverlay("Display", themeColor, RenJava.CONFIGURATION.getUiFont(), 0, 0);
        ButtonOverlay windowButton = new ButtonOverlay("windowed-display", "Windowed", subColor, RenJava.CONFIGURATION.getUiFont(), 0,0);
        ButtonOverlay fullscreenButton = new ButtonOverlay("windowed-fullscreen", "Fullscreen", subColor, RenJava.CONFIGURATION.getUiFont(), 0,0);
        displayBox.addOverlays(displayText, windowButton, fullscreenButton);

        VerticalLayout rollbackBox = new VerticalLayout(300, 400);
        TextOverlay rollbackText = new TextOverlay("Rollback", themeColor, RenJava.CONFIGURATION.getUiFont(), 0, 0);
        ButtonOverlay disabledButton = new ButtonOverlay("disabled-rollback", "Disabled", subColor, RenJava.CONFIGURATION.getUiFont(), 0,0);
        ButtonOverlay leftButton = new ButtonOverlay("left-rollback", "Left", subColor, RenJava.CONFIGURATION.getUiFont(), 0,0);
        ButtonOverlay rightButton = new ButtonOverlay("right-rollback", "Right", subColor, RenJava.CONFIGURATION.getUiFont(), 0,0);
        rollbackBox.addOverlays(rollbackText, disabledButton, leftButton, rightButton);

        VerticalLayout skipBox = new VerticalLayout(300, 400);
        TextOverlay skipText = new TextOverlay("Skip", themeColor, RenJava.CONFIGURATION.getUiFont(), 0, 0);
        ButtonOverlay unseenTextButton = new ButtonOverlay("unseen-skip", "Unseen Text", subColor, RenJava.CONFIGURATION.getUiFont(), 0,0);
        ButtonOverlay afterChoicesButton = new ButtonOverlay("after-skip", "After Choices", subColor, RenJava.CONFIGURATION.getUiFont(), 0,0);
        ButtonOverlay transitionButton = new ButtonOverlay("transitions-skip", "Transitions", subColor, RenJava.CONFIGURATION.getUiFont(), 0,0);
        skipBox.addOverlays(skipText, unseenTextButton, afterChoicesButton, transitionButton);

        // Add all to root layout
        rootLayout.addChildLayouts(displayBox, rollbackBox, skipBox);

        menu.addLayout(rootLayout);

        // Music sliders
        VerticalLayout soundRoot = new VerticalLayout(1000, 1000);
        soundRoot.setX(1150);
        soundRoot.setY(400);
        soundRoot.setSpacing(30);

        VerticalLayout masterBox = new VerticalLayout(1000, 100);
        TextOverlay masterVolumeText = new TextOverlay("Master Volume", themeColor, RenJava.CONFIGURATION.getUiFont(), 0, 0);
        SliderOverlay masterVolumeSlider = new SliderOverlay(0, 100, RenJava.SETTINGS.getMasterVolume(), 0,0, 200, 200);
        masterVolumeSlider.onSliderMove(event -> {
            RenJava.SETTINGS.setMasterVolume(event.getValue());
        });
        masterBox.addOverlays(masterVolumeText, masterVolumeSlider);

        VerticalLayout musicBox = new VerticalLayout(1000, 100);
        TextOverlay musicVolumeText = new TextOverlay("Music Volume", themeColor, RenJava.CONFIGURATION.getUiFont(), 0, 0);
        SliderOverlay musicVolumeSlider = new SliderOverlay(0, 100, RenJava.SETTINGS.getMusicVolume(), 0,0, 200, 200);
        musicVolumeSlider.onSliderMove(event -> {
            RenJava.SETTINGS.setMusicVolume(event.getValue());
        });
        musicBox.addOverlays(musicVolumeText, musicVolumeSlider);

        VerticalLayout soundBox = new VerticalLayout(1000, 100);
        TextOverlay soundVolumeText = new TextOverlay("Sound Volume", themeColor, RenJava.CONFIGURATION.getUiFont(), 0, 0);
        SliderOverlay soundVolumeSlider = new SliderOverlay(0, 100, RenJava.SETTINGS.getSoundVolume(), 0,0, 200, 200);
        soundVolumeSlider.onSliderMove(event -> {
            RenJava.SETTINGS.setSoundVolume(event.getValue());
        });
        soundBox.addOverlays(soundVolumeText, soundVolumeSlider);

        VerticalLayout voiceBox = new VerticalLayout(1000, 100);
        TextOverlay voiceVolumeText = new TextOverlay("Voice Volume", themeColor, RenJava.CONFIGURATION.getUiFont(), 0, 0);
        SliderOverlay voiceVolumeSlider = new SliderOverlay(0, 100, RenJava.SETTINGS.getVoiceVolume(), 0,0, 200, 200);
        voiceVolumeSlider.onSliderMove(event -> {
            RenJava.SETTINGS.setVoiceVolume(event.getValue());
        });
        voiceBox.addOverlays(voiceVolumeText, voiceVolumeSlider);


        soundRoot.addChildLayouts(masterBox, musicBox, soundBox, voiceBox);

        menu.addLayout(soundRoot);

        return menu;
    }

    @Override
    public Container aboutMenu(boolean rightClicked) {
        Container menu = new EmptyContainer(RenJava.CONFIGURATION.getWidth(), RenJava.CONFIGURATION.getHeight());
        TextOverlay spacer = new TextOverlay("\n");
        menu.addOverlay(new ImageOverlay("gui/main_menu.png"));

        FontLoader font = new FontLoader(RenJava.CONFIGURATION.getDefaultFont().getFont(), 24);
        TextFlowOverlay aboutText = new TextFlowOverlay("", 1300, 500);
        aboutText.add(new HyperLinkOverlay("https://github.com/HackusatePvP/RenJava", "RenJava"));
        aboutText.add(new TextOverlay(" is a VN framework inspired by RenPy and built with JavaFX. Thank you to those respective communities for their contributions. RenJava is free and open sourced licensed under GPL 3.0 for free use."));
        aboutText.setFont(font);
        aboutText.setX(500);
        aboutText.setY(150);
        menu.addOverlay(aboutText);

        // If you modify the about you must include the license information.
        TextFlowOverlay licenseText = new TextFlowOverlay("License Information. Some of the licenses below are required to be disclosed. Modify the below only to include additional required licenses.", 1300, 700);
        licenseText.setX(500);
        licenseText.setY(250);
        licenseText.setFont(new FontLoader(font, 20));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tRenJava is licensed under GNU GPL v3: "));
        licenseText.add(new HyperLinkOverlay("https://www.gnu.org/licenses/gpl-3.0.en.html"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tRenPy is licensed under MIT: "));
        licenseText.add(new HyperLinkOverlay("https://www.renpy.org/doc/html/license.html"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tJavaFX is licensed under GPL-2.0: "));
        licenseText.add(new HyperLinkOverlay("https://github.com/openjdk/jfx/blob/master/LICENSE"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tAmazon Corretto is licensed under GPL-2.0 CE: "));
        licenseText.add(new HyperLinkOverlay("https://openjdk.java.net/legal/gplv2+ce.html"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tApache software is licensed under Apache 2.0: "));
        licenseText.add(new HyperLinkOverlay("http://www.apache.org/licenses/"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tJetBrains is licensed under Apache 2.0: "));
        licenseText.add(new HyperLinkOverlay("http://www.apache.org/licenses/"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tWebP ImageIO is licensed under Apache 2.0: "));
        licenseText.add(new HyperLinkOverlay("http://www.apache.org/licenses/"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tGoogleCode SoundLibs is licensed under LGPL 2.1: "));
        licenseText.add(new HyperLinkOverlay("https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tOshi is licensed under MIT: "));
        licenseText.add(new HyperLinkOverlay("https://github.com/oshi/oshi/blob/master/LICENSE"));

        menu.addOverlay(licenseText);


        TextFlowOverlay buildInfo = new TextFlowOverlay("Just because the software this game uses may be free for use does not mean you have any right to re-distribute. " +
                "The author holds and retrains rights to distribute and sell their game as they wish. " +
                "Consumers hold no rights when re-distributing or publishing this game without explicit permission from the author. " +
                "Consumers also have no ownership over this digital product and must follow the terms provided by the author. " +
                "If no terms were provided then you must use the following. You are not allowed to re-sell or re-distribute this game. " +
                "If you paid for this game you did not pay for ownership but rather permission to use this game. This permission can be revoked at any time for any reason." +
                "These terms also apply to any addons that may come with the game.",1300, 800);
        buildInfo.setX(500);
        buildInfo.setY(600);
        buildInfo.setFont(new FontLoader(font, 20));
        buildInfo.add(spacer);
        buildInfo.add(spacer);
        buildInfo.add(new TextOverlay("RenJava Version: " + RenJava.getInstance().getBuildVersion()));
        buildInfo.add(spacer);
        buildInfo.add(new TextOverlay("Game Version: " + RenJava.getInstance().getVersion()));
        buildInfo.add(spacer);
        buildInfo.add(new TextOverlay("Author: " + RenJava.getInstance().getAuthor()));
        buildInfo.add(spacer);

        menu.addOverlay(buildInfo);

        return menu;
    }

    @Override
    public ButtonOverlay savePreview(Save save, int page, int index) {
        ImageOverlay saveImage = new ImageOverlay("gui/button/slot_idle_background.png");
        ButtonOverlay loadButton;

        // 384 215
        // 404 319

        loadButton = new ButtonOverlay("save-" + index, saveImage, 0, 0, 404, 319);
        loadButton.setAlignGraphicToBox(false);
        ImageOverlay preview = save.buildPreview(page);
        if (preview != null) {
            preview.setWidth(384);
            preview.setHeight(215);
        }

        loadButton.addImage(preview);

        loadButton.onClick(event -> {
            if (RenJava.PLAYER.getCurrentStageType() == StageType.LOAD_MENU) {
                if (!save.exists()) {
                    RenLogger.LOGGER.warn("Save file does not  0-exist.");
                    return;
                }
                save.load(true);
                String storyID = RenJava.PLAYER.getCurrentStoryID();
                if (storyID == null) {
                    RenLogger.LOGGER.error("Save file could not be loaded. The data is either not formatted or corrupted.");
                    return;
                }
                RenLogger.LOGGER.info("Processing save file...");

                RenLogger.LOGGER.debug("Reloading story...");
                RenJava.getInstance().createStory();

                // Force update fields
                RenLogger.LOGGER.debug("Setting current story: " + storyID);
                RenJava.PLAYER.setCurrentStory(storyID);
                RenLogger.LOGGER.debug("Initializing story...");
                RenJava.PLAYER.getCurrentStory().init(); // Re-initialize story

                RenLogger.LOGGER.debug("Setting current scene: " + RenJava.PLAYER.getCurrentSceneID());
                RenJava.PLAYER.setCurrentScene(RenJava.PLAYER.getCurrentSceneID());

                RenJava.PLAYER.setRightClickMenu(false); // When the save is loaded it will render the scene leaving the main menu.

                RenLogger.LOGGER.debug("Rendering scene...");
                RenScene scene = RenJava.PLAYER.getCurrentScene();
                RenJava.PLAYER.getStory(storyID).displayScene(scene);

            } else if (RenJava.PLAYER.getCurrentStageType() == StageType.SAVE_MENU) {
                Tasks.runAsync(save::write);
                // Re-render
                RenJava.PLAYER.setCurrentStageType(StageType.SAVE_MENU);
                Container menu = loadMenu(false, 1, false); // Builds first page
                menu.addContainers(sideMenu(true));
                RenJava.getInstance().getGameWindow().clearContainers();
                RenJava.getInstance().getGameWindow().addContainer(menu);
                RenJava.getInstance().getGameWindow().render();
            }
        });

        loadButton.setBackgroundColor(Color.TRANSPARENT);
        loadButton.setBorderColor(Color.TRANSPARENT);
        return loadButton;
    }
}
