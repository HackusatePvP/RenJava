package me.piitex.renjava.api.scenes.transitions.types;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.SceneEndTransitionFinishEvent;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class FadingTransition extends Transitions {
    private final double fromValue;
    private final double toValue;
    private final int cycleCount;
    private final boolean autoReverse;
    private boolean playing = false;

    private FadeTransition fadeTransition;

    private Color color = Color.BLACK;

    // Cheap hack
    private static FadingTransition previousTranition = null;

    public FadingTransition(double fromValue, double toValue, double duration, Color color) {
        super(duration);
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.cycleCount = 1;
        this.autoReverse = false;
        this.color = color;
    }

    public FadingTransition(double fromValue, double toValue, int cycleCount, boolean autoReverse, double duration) {
        super(duration);
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.cycleCount = cycleCount;
        this.autoReverse = autoReverse;
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

    public Color getColor() {
        return color;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void play(RenScene scene, Node node) {
        fadeTransition = new FadeTransition(Duration.valueOf(getDuration() + "ms"));
        fadeTransition.setFromValue(getFromValue());
        fadeTransition.setToValue(getToValue());
        fadeTransition.setCycleCount(getCycleCount());
        fadeTransition.setAutoReverse(isAutoReverse());
        fadeTransition.setNode(node);
        fadeTransition.setDuration(Duration.seconds(getDuration()));
        fadeTransition.setOnFinished(actionEvent -> {
            if (getOnFinish() != null) {
                getOnFinish().onEnd(actionEvent);
            }
            playing = false;

            SceneEndTransitionFinishEvent endEvent = new SceneEndTransitionFinishEvent(scene, this);
            RenJava.callEvent(endEvent);
        });
        if (previousTranition != null) {
            previousTranition.stop(); // Stop previous animation
        }
        playing = true;
        fadeTransition.play();
        previousTranition = this;
    }

    @Override
    public void stop() {
        RenLogger.LOGGER.debug("Stopping transition...");
        if (fadeTransition != null) {
            fadeTransition.stop();
            playing = false;
        }
    }
}
