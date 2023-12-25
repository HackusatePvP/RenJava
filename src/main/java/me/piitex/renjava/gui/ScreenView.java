package me.piitex.renjava.gui;

import javafx.scene.layout.VBox;
import me.piitex.renjava.gui.layouts.Container;
import me.piitex.renjava.api.builders.ButtonBuilder;
import me.piitex.renjava.gui.overlay.Overlay;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

public abstract class ScreenView extends Container {
    private final Collection<Overlay> overlays = new HashSet<>();
    private final LinkedHashSet<ButtonBuilder> buttons = new LinkedHashSet<>();
    private final VBox buttonVbox = new VBox();

    public LinkedHashSet<ButtonBuilder> getButtons() {
        return buttons;
    }

    public VBox getButtonVbox() {
        return buttonVbox;
    }

    public void addOverlay(Overlay overlay) {
        overlays.add(overlay);
    }

    public Collection<Overlay> getOverlays() {
        return overlays;
    }
}
