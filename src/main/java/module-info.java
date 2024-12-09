module RenJava {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.controls;
    requires javafx.media;
    requires java.logging;
    requires org.reflections;
    requires javafx.base;
    requires java.desktop;
    requires org.apache.commons.io;
    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.slf4j2.impl;
    requires com.github.oshi;
    requires maven.artifact;
    requires org.apache.commons.lang3;
    exports me.piitex.renjava;
    exports me.piitex.renjava.configuration;
    exports me.piitex.renjava.gui;
    exports me.piitex.renjava.utils;
    exports me.piitex.renjava.api.characters;
    exports me.piitex.renjava.api.loaders;
    exports me.piitex.renjava.gui.overlays;
    exports me.piitex.renjava.gui.layouts;
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
    exports me.piitex.renjava.api.scenes.types.input;
    exports me.piitex.renjava.api.saves.data;
    exports me.piitex.renjava.api.scenes.types.animation;
    exports me.piitex.renjava.addons;
    exports me.piitex.renjava.loggers;
    exports me.piitex.renjava.gui.overlays.events;
    exports me.piitex.renjava.api;
    exports me.piitex.renjava.tasks;
    exports me.piitex.renjava.gui.menus;
    exports me.piitex.renjava.api.saves;
    opens me.piitex.renjava.loggers;
    opens me.piitex.renjava;
    opens me.piitex.renjava.api;
}