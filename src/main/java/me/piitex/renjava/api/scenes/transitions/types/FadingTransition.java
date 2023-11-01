package me.piitex.renjava.api.scenes.transitions.types;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import me.piitex.renjava.api.scenes.transitions.TransitionType;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class FadingTransition extends Transitions {
    private final TransitionType transitionType;
    private final double fromValue;
    private final double toValue;
    private final int cycleCount;
    private final boolean autoReverse;

    private FadeTransition fadeTransition;

    public FadingTransition(TransitionType transitionType, double fromValue, double toValue, int cycleCount, boolean autoReverse, int duration) {
        super(duration);
        this.transitionType = transitionType;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.cycleCount = cycleCount;
        this.autoReverse = autoReverse;
    }

    public TransitionType getTransitionType() {
        return transitionType;
    }

    public double getFromValue() {
        return fromValue;
    }

    public double getToValue() {
        return toValue;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public boolean isAutoReverse() {
        return autoReverse;
    }

    @Override
    public void play(Node node) {
        fadeTransition = new FadeTransition(Duration.valueOf(getDuration() + "ms"));
        fadeTransition.setFromValue(getFromValue());
        fadeTransition.setToValue(getToValue());
        fadeTransition.setCycleCount(getCycleCount());
        fadeTransition.setAutoReverse(isAutoReverse());
        fadeTransition.setNode(node);
        fadeTransition.play();
    }

    @Override
    public void stop() {
        if (fadeTransition != null) {
            fadeTransition.stop();
        }
    }
}
