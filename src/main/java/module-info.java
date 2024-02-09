module RenJava {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires java.logging;
    requires org.reflections;
    requires javafx.base;
    requires java.desktop;
    requires org.apache.commons.io;
    requires org.jetbrains.annotations;
    requires com.goxr3plus.streamplayer;
    opens me.piitex.renjava;
    exports me.piitex.renjava;
    exports me.piitex.renjava.configuration;
    exports me.piitex.renjava.gui;
    exports me.piitex.renjava.utils;
    exports me.piitex.renjava.api.characters;
    exports me.piitex.renjava.api.builders;
    exports me.piitex.renjava.gui.overlay;
    exports me.piitex.renjava.gui.layouts;
    exports me.piitex.renjava.gui.layouts.impl;
    exports me.piitex.renjava.events;
    exports me.piitex.renjava.events.exceptions;
    exports me.piitex.renjava.events.types;
    exports me.piitex.renjava.api.scenes;
    exports me.piitex.renjava.api.stories;
    exports me.piitex.renjava.api.music;
    exports me.piitex.renjava.api.scenes.types;
    exports me.piitex.renjava.api.scenes.types.choices;
    exports me.piitex.renjava.api.scenes.transitions;
    exports me.piitex.renjava.api.player;
    exports me.piitex.renjava.api.stories.handler;
    exports me.piitex.renjava.gui.exceptions;
    exports me.piitex.renjava.gui.splashscreen;
    exports me.piitex.renjava.api.scenes.types.input;
    exports me.piitex.renjava.api.saves.data;
    exports me.piitex.renjava.api.scenes.animation;
    exports me.piitex.renjava.addons;
}