package me.piitex.renjava.gui.overlay;

import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.gui.overlay.events.IOverlayClick;
import me.piitex.renjava.gui.overlay.events.IOverlayHover;

public class HyperlinkOverlay implements Overlay {
    private final String label;
    private final String link;
    private FontLoader font;
    private double x, y;

    private Transitions transitions;
    private IOverlayHover iOverlayHover;
    private IOverlayClick iOverlayClick;

    public HyperlinkOverlay(String label, String link, double x, double y) {
        this.label = label;
        this.link = link;
        this.x = x;
        this.y = y;
    }

    public HyperlinkOverlay(String label, String link, FontLoader font, double x, double y) {
        this.label = label;
        this.link = link;
        this.font = font;
        this.x = x;
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public String getLink() {
        return link;
    }

    public FontLoader getFont() {
        return font;
    }

    public void setFont(FontLoader font) {
        this.font = font;
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
    public IOverlayClick getOnClick() {
        return iOverlayClick;
    }

    @Override
    public IOverlayHover getOnHover() {
        return iOverlayHover;
    }
}
