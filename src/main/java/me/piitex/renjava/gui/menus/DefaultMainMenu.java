package me.piitex.renjava.gui.menus;

import me.piitex.engine.ui.containers.Container;
import me.piitex.engine.ui.overlays.ButtonOverlay;
import me.piitex.renjava.api.saves.Save;

public class DefaultMainMenu implements MainMenu {

    @Override
    public Container mainMenu(boolean rightClick) {
        return null;
    }

    @Override
    public Container sideMenu(boolean rightClick) {
        return null;
    }

    @Override
    public Container loadMenu(boolean rightClick, int page, boolean loadMenu) {
        return null;
    }

    @Override
    public Container settingMenu(boolean rightClick) {
        return null;
    }

    @Override
    public Container aboutMenu(boolean rightClick) {
        return null;
    }

    @Override
    public ButtonOverlay savePreview(Save save, int page, int index) {
        return null;
    }
}
