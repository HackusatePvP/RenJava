package me.piitex.renjava.gui.overlay;

import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.overlay.events.*;

public class SliderOverlay implements Overlay {
    private final double maxValue, minValue, currentValue;
    private double x, y;
    private double blockIncrement;

    private ISliderChange iSliderChange;
    private IOverlayHover iOverlayHover;
    private IOverlayHoverExit iOverlayHoverExit;
    private IOverlayClick iOverlayClick;
    private IOverlayClickRelease iOverlayClickRelease;

    public SliderOverlay(double maxValue, double minValue, double currentValue, double x, double y) {
        // Sliders don't have regions like images or buttons do.
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.currentValue = currentValue;
        this.x = x;
        this.y = y;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getBlockIncrement() {
        return blockIncrement;
    }

    public void setBlockIncrement(double blockIncrement) {
        this.blockIncrement = blockIncrement;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public Transitions getTransition() {
        return null;
    }

    public void setOnSliderChange(ISliderChange iSliderChange) {
        this.iSliderChange = iSliderChange;
    }

    public ISliderChange getSliderChange() {
        return iSliderChange;
    }

    @Override
    public void setOnclick(IOverlayClick iOverlayClick) {
        this.iOverlayClick = iOverlayClick;
    }

    @Override
    public void setOnHover(IOverlayHover iOverlayHover) {
        this.iOverlayHover = iOverlayHover;
    }

    @Override
    public void setOnClickRelease(IOverlayClickRelease iOverlayClickRelease) {
        this.iOverlayClickRelease = iOverlayClickRelease;
    }

    @Override
    public void setOnHoverExit(IOverlayHoverExit iOverlayHoverExit) {
        this.iOverlayHoverExit = iOverlayHoverExit;
    }

    @Override
    public IOverlayClick getOnClick() {
        return iOverlayClick;
    }

    @Override
    public IOverlayHover getOnHover() {
        return iOverlayHover;
    }

    @Override
    public IOverlayHoverExit getOnHoverExit() {
        return iOverlayHoverExit;
    }

    @Override
    public IOverlayClickRelease getOnRelease() {
        return iOverlayClickRelease;
    }

}
