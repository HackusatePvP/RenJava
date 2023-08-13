package me.piitex.renjava.gui;

public enum StageType {
    MAIN_MENU,
    LOAD_MENU,
    OPTIONS_MENU,
    ABOUT_MENU,
    HELP_MENU,
    /**
     * @deprecated GAME_WINDOW is deprecated as it will be removed in later releases and replaced with more specific game window types.
     */
    @Deprecated GAME_WINDOW // Expand on this (animation window, ui menu ect...)
}
