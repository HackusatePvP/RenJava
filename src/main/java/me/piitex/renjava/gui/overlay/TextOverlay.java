package me.piitex.renjava.gui.overlay;


import javafx.scene.text.Text;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class TextOverlay implements Overlay {
    private final Text text;
    private FontLoader fontLoader;
    private final double x;
    private final double y;
    private Transitions transitions;

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

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    @Override
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }
}
