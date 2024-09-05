package me.piitex.renjava.api.scenes.transitions.types;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.events.types.FadingTransitionEndEvent;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import org.slf4j.Logger;;

public class FadingTransition extends Transitions {
    private final double fromValue;
    private final double toValue;
    private final int cycleCount;
    private final boolean autoReverse;

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
    public void play(Node node) {
        // Remove logger
        Logger logger = RenLogger.LOGGER;
        logger.info("Playing fading transition...");

        fadeTransition = new FadeTransition(Duration.valueOf(getDuration() + "ms"));
        fadeTransition.setFromValue(getFromValue());
        fadeTransition.setToValue(getToValue());
        fadeTransition.setCycleCount(getCycleCount());
        fadeTransition.setAutoReverse(isAutoReverse());
        fadeTransition.setNode(node);
        fadeTransition.setDuration(Duration.seconds(getDuration()));
        fadeTransition.setOnFinished(actionEvent -> {
            logger.info("Fading transition finished. Calling onEnd event.");
            if (getOnFinish() != null) {
                getOnFinish().onEnd(actionEvent);
            }

            FadingTransitionEndEvent endEvent = new FadingTransitionEndEvent(this);
            RenJava.callEvent(endEvent);
        });
        if (previousTranition != null) {
            previousTranition.stop(); // Stop previous animation
        }
        fadeTransition.play();
        previousTranition = this;
    }

    @Override
    public void stop() {
        if (fadeTransition != null) {
            fadeTransition.stop();
        }
    }
}
