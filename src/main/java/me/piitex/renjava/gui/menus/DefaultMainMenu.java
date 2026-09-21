package me.piitex.renjava.gui.menus;

import me.piitex.engine.ui.color.Color;
import me.piitex.engine.ui.containers.Container;
import me.piitex.engine.ui.layout.Layout;
import me.piitex.engine.ui.layout.VerticalLayout;
import me.piitex.engine.ui.overlays.ButtonOverlay;
import me.piitex.engine.ui.overlays.ImageOverlay;
import me.piitex.engine.ui.overlays.TextOverlay;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.configuration.RenJavaConfiguration;

import java.io.File;

public class DefaultMainMenu implements MainMenu {
    private final RenJava renJava = RenJava.getInstance();
    private final RenJavaConfiguration configuration = RenJava.getConfiguration();

    @Override
    public Container mainMenu(boolean rightClick) {
        Container container = new Container(configuration.getWidth(), configuration.getHeight());

        // Background image
        ImageOverlay background = new ImageOverlay(new File(renJava.getGuiDirectory(), "main_menu.png"));
        container.addElement(background);

        TextOverlay title = new TextOverlay(renJava.getName());
        title.setFontFile(configuration.getDefaultFont());
        title.setFontSize(48);
        title.setTextColor(Color.BLUE);

        return container;
    }

    @Override
    public Container sideMenu(boolean rightClick) {
        Container container = new Container(420, configuration.getHeight());
        container.getStyling().setBackgroundColor(Color.TRANSPARENT);
        container.getStyling().setBorderColor(Color.TRANSPARENT);

        ImageOverlay background = new ImageOverlay(new File(renJava.getGuiDirectory(), "overlay/main_menu.png"));
        container.addElement(background);

        VerticalLayout items = new VerticalLayout(400, 800);
        items.getStyling().setBackgroundColor(Color.TRANSPARENT);
        items.getStyling().setBorderColor(Color.TRANSPARENT);
        items.setAlignment(Layout.Alignment.CENTER);
        items.setSpacing(15);
        container.addElement(items);

        ButtonOverlay start = new ButtonOverlay("Start", 100, 50);
        applyStyling(start);
        items.addElement(start);

        ButtonOverlay load = new ButtonOverlay("Load", 100, 50);
        applyStyling(load);
        items.addElement(load);

        ButtonOverlay options = new ButtonOverlay("Options", 100, 50);
        applyStyling(options);
        items.addElement(options);

        ButtonOverlay about = new ButtonOverlay("About", 100, 50);
        applyStyling(about);
        items.addElement(about);

        ButtonOverlay help = new ButtonOverlay("Help", 100, 50);
        applyStyling(help);
        items.addElement(help);

        ButtonOverlay quit = new ButtonOverlay("Quit", 100, 50);
        applyStyling(quit);
        items.addElement(quit);

        return container;
    }

    private void applyStyling(ButtonOverlay button) {
        button.setFontFile(configuration.getUiFont());
        button.setFontSize(32);
        button.setTextAlignment(Layout.Alignment.CENTER_LEFT);
        button.getStyling().setBackgroundColor(Color.TRANSPARENT);
        button.getStyling().setBorderColor(Color.TRANSPARENT);
        button.getStyling().setHoverColor(Color.TRANSPARENT);
        button.getStyling().setTextHoverColor(configuration.getHoverColor());
        button.setTextColor(configuration.getTextColor());
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
