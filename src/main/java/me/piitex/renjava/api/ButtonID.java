package me.piitex.renjava.api;

public enum ButtonID {
    START("menu-start-button"),
    LOAD("menu-load-button"),
    SAVE("menu-load-button"),
    SETTINGS("menu-preference-button"),
    ABOUT("menu-about-button"),
    QUIT("menu-quit-button"),
    RETURN("menu-return-button")
    ;

    final String id;
    ButtonID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
