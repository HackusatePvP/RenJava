package me.piitex.renjava.gui.overlay;

import me.piitex.renjava.api.builders.TextFlowBuilder;

public record TextFlowOverlay(TextFlowBuilder textFlowBuilder, double x, double y) implements Overlay {

}
