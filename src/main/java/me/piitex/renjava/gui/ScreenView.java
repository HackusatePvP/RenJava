package me.piitex.renjava.gui;

import me.piitex.renjava.api.gui.Container;
import me.piitex.renjava.gui.builders.ButtonBuilder;
import me.piitex.renjava.gui.overlay.Overlay;

import java.util.Collection;
import java.util.HashSet;

public abstract class ScreenView extends Container {

    @Deprecated
    private final Collection<Overlay> overlays = new HashSet<>();

    private ButtonBuilder startButton;
    private ButtonBuilder loadButton;
    private ButtonBuilder optionsButton;
    private ButtonBuilder aboutButton;
    private ButtonBuilder helpButton;
    private ButtonBuilder quitButton;

    public ButtonBuilder getStartButton() {
        return startButton;
    }

    public void setStartButton(ButtonBuilder startButton) {
        this.startButton = startButton;
    }

    public ButtonBuilder getLoadButton() {
        return loadButton;
    }

    public void setLoadButton(ButtonBuilder loadButton) {
        this.loadButton = loadButton;
    }

    public ButtonBuilder getOptionsButton() {
        return optionsButton;
    }

    public void setOptionsButton(ButtonBuilder optionsButton) {
        this.optionsButton = optionsButton;
    }

    public ButtonBuilder getAboutButton() {
        return aboutButton;
    }

    public void setAboutButton(ButtonBuilder aboutButton) {
        this.aboutButton = aboutButton;
    }

    public ButtonBuilder getHelpButton() {
        return helpButton;
    }

    public void setHelpButton(ButtonBuilder helpButton) {
        this.helpButton = helpButton;
    }

    public ButtonBuilder getQuitButton() {
        return quitButton;
    }

    public void setQuitButton(ButtonBuilder quitButton) {
        this.quitButton = quitButton;
    }

    public void addOverlay(Overlay overlay) {
        overlays.add(overlay);
    }

    public Collection<Overlay> getOverlays() {
        return overlays;
    }
}
