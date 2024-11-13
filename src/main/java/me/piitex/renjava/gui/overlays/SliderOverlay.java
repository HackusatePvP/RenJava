package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.control.Slider;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.events.types.OverlayClickReleaseEvent;
import me.piitex.renjava.events.types.SliderChangeEvent;
import me.piitex.renjava.gui.overlays.events.ISliderChange;
import me.piitex.renjava.loggers.RenLogger;

import java.io.File;
import java.net.MalformedURLException;

public class SliderOverlay extends Overlay implements Region {
    private double width, height;
    private double scaleWidth = 1, scaleHeight = 1;
    private final double maxValue, minValue, currentValue;
    private double blockIncrement;

    private ISliderChange sliderChange;

    public SliderOverlay(double minValue, double maxValue, double currentValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
    }

    public SliderOverlay(double minValue, double maxValue, double currentValue, double x, double y, double width, double height) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        setX(x);
        setY(y);
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

    public ISliderChange getSliderChange() {
        return sliderChange;
    }

    public void onSliderMove(ISliderChange sliderChange) {
        this.sliderChange = sliderChange;
    }

    @Override
    public Node render() {
        Slider slider = new Slider(minValue, maxValue, currentValue);
        slider.setPrefSize(width * scaleWidth, height * scaleHeight);
        slider.setTranslateX(getX());
        slider.setTranslateY(getY());
        slider.setBlockIncrement(blockIncrement);
        // To design sliders we NEED a css file which contains the styling. I'm not able to inline this via code which sucks.
        // Hopefully the slider gets improvements in JavaFX.

        // Check if they have css files
        File sliderCss = new File(System.getProperty("user.dir") + "/game/css/slider.css");
        if (!sliderCss.exists()) {
            RenLogger.LOGGER.error("Slider css file does not exist. Please run or copy the css files from RSDK. Without a slider css file, the sliders won't properly be designed.");
        } else {
            try {
                slider.getStylesheets().add(sliderCss.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                RenLogger.LOGGER.error("Could not load css file", e);
                RenJava.writeStackTrace(e);
            }
        }
        // Handle slider events
        slider.setOnMouseDragged(event -> {
            SliderChangeEvent changeEvent = new SliderChangeEvent(this, slider.getValue());
            getSliderChange().onSliderChange(changeEvent);
            RenJava.callEvent(changeEvent);

            RenJava.callEvent(new OverlayClickReleaseEvent(this, event));
        });

        setInputControls(slider);
        renderTransitions(slider);
        return slider;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setWidth(double w) {
        this.width = w;
    }

    @Override
    public void setHeight(double h) {
        this.height = h;
    }

    @Override
    public double getScaleWidth() {
        return scaleWidth;
    }

    @Override
    public void setScaleWidth(double w) {
        this.scaleWidth = w;
    }

    @Override
    public double getScaleHeight() {
        return scaleHeight;
    }

    @Override
    public void setScaleHeight(double h) {
        this.scaleHeight = h;
    }
}
