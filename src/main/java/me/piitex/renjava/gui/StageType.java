package me.piitex.renjava.gui;

import me.piitex.renjava.api.APIChange;

@APIChange(changedVersion = "0.0.153", description = "Added more scene types. GAME_WINDOW will be removed next version.")
public enum StageType {
    MAIN_MENU,
    LOAD_MENU,
    OPTIONS_MENU,
    ABOUT_MENU,
    HELP_MENU,
    IMAGE_SCENE,
    ANIMATION_SCENE,
    INTERACTABLE_SCENE,
    INPUT_SCENE,
    CHOICE_SCENE,

}
