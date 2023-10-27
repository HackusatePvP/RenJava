package me.piitex.renjava.gui.title;

import me.piitex.renjava.gui.ScreenView;

public abstract class CustomTitleScreen {
    private final ScreenView titleScreenView;

    protected CustomTitleScreen(ScreenView titleScreenView) {
        this.titleScreenView = titleScreenView;
    }

    public abstract void build();

    public ScreenView getTitleScreenView() {
        return titleScreenView;
    }
}
