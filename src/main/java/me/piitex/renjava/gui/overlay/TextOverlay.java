package me.piitex.renjava.gui.overlay;


import javafx.scene.text.Text;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class TextOverlay implements Overlay {
    private final Text text;
    private double x;
    private double y;
    private Transitions transitions;

    public TextOverlay(Text text, double x, double y) {
        this.text = text;
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
    public Transitions getTransition() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }
}
