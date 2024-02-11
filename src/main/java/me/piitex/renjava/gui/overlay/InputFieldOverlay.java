package me.piitex.renjava.gui.overlay;

import me.piitex.renjava.api.builders.InputFieldBuilder;

public record InputFieldOverlay(InputFieldBuilder inputFieldBuilder) implements Overlay {

    @Override
    public double x() {
        return inputFieldBuilder.getX();
    }

    @Override
    public double y() {
        return inputFieldBuilder.getY();
    }
}
