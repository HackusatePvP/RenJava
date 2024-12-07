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
    private static FadingTransition previousTransition = null;

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
            System.out.println("Calling transition end event!");

            if (getOnFinish() != null) {
                getOnFinish().onEnd(actionEvent);
            }
            playing = false;

            SceneEndTransitionFinishEvent endEvent = new SceneEndTransitionFinishEvent(scene, this);
            RenJava.callEvent(endEvent);

            RenJava.PLAYER.setTransitionPlaying(false);
        });
        if (previousTransition != null) {
            previousTransition.stop(); // Stop previous animation
        }
        RenJava.PLAYER.setTransitionPlaying(true);
        fadeTransition.play();
        previousTransition = this;
        playing = true;
    }

    @Override
    public void stop() {
        if (fadeTransition != null) {
            RenLogger.LOGGER.debug("Stopping transition...");
            fadeTransition.jumpTo(Duration.INDEFINITE);
            try {
                fadeTransition.stop();
            } catch (Exception e) {
                RenLogger.LOGGER.error("Error stopping transition!", e);
                RenJava.writeStackTrace(e);
            }
            playing = false;
        }
    }
}
