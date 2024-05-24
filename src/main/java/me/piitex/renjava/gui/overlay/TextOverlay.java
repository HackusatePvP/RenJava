package me.piitex.renjava.gui.overlay;


import javafx.scene.text.Text;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.overlay.events.IOverlayClick;
import me.piitex.renjava.gui.overlay.events.IOverlayClickRelease;
import me.piitex.renjava.gui.overlay.events.IOverlayHover;

public class TextOverlay implements Overlay, Region {
    private final Text text;
    private FontLoader fontLoader;
    private double x;
    private double y;
    private double scaleX, scaleY;
    private double width, height;
    private Transitions transitions;

    private IOverlayClick iOverlayClick;
    private IOverlayHover iOverlayHover;
    private IOverlayClickRelease iOverlayClickRelease;

    public TextOverlay(String text, double x, double y) {
        this.text = new Text(text);
        this.x = x;
        this.y = y;
    }

    public TextOverlay(String text, FontLoader fontLoader, double x, double y) {
        this.text = new Text(text);
        this.fontLoader = fontLoader;
        this.x = x;
        this.y = y;
    }

    public TextOverlay(Text text, double x, double y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public TextOverlay(Text text, FontLoader fontLoader, double x, double y) {
        this.text = text;
        this.fontLoader = fontLoader;
        this.x = x;
        this.y = y;
    }

    public Text getText() {
        return text;
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
    public double scaleX() {
        return scaleX;
    }

    @Override
    public double scaleY() {
        return scaleY;
    }

    @Override
    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    @Override
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    @Override
    public double width() {
        return width;
    }

    @Override
    public double height() {
        return height;
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    @Override
    public Transitions getTransition() {
        return transitions;
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
    public IOverlayClick getOnClick() {
        return iOverlayClick;
    }

    @Override
    public IOverlayHover getOnHover() {
        return iOverlayHover;
    }

    @Override
    public IOverlayClickRelease getOnRelease() {
        return iOverlayClickRelease;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }
}
