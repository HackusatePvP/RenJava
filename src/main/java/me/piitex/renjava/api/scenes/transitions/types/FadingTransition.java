package me.piitex.renjava.api.scenes.transitions.types;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.events.types.TransitionStopEvent;
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

    private RenScene scene;

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
    public void play(Node root) {
        setPlayed(true);
        fadeTransition = new FadeTransition(Duration.valueOf(getDuration() + "ms"));
        fadeTransition.setFromValue(getFromValue());
        fadeTransition.setToValue(getToValue());
        fadeTransition.setCycleCount(getCycleCount());
        fadeTransition.setAutoReverse(isAutoReverse());
        fadeTransition.setNode(root);
        fadeTransition.setDuration(Duration.seconds(getDuration()));
        fadeTransition.setOnFinished(actionEvent -> {
            handleEvents(scene);
            playing = false;
            RenJava.PLAYER.setCurrentTransition(null);
        });
        playing = true;
        RenJava.PLAYER.setCurrentTransition(this);
        fadeTransition.play();
    }

    @Override
    public void play(RenScene scene) {
        this.scene = scene;
        Node root = RenJava.getInstance().getGameWindow().getRoot();
        play(root);
    }

    @Override
    public void stop() {
        if (fadeTransition != null && playing) {
            // Check the from and to vaules.
            // If its a fade-in jump to the end of the transition.
            // If its a fade-out jump to the beginning
            if (fromValue < toValue) {
                fadeTransition.jumpTo(Duration.INDEFINITE);
            } else {
                fadeTransition.jumpTo(Duration.ZERO);
            }
            try {
                fadeTransition.stop();
                fadeTransition.getNode().setOpacity(1); // Resets opacity
                handleEvents(scene);
            } catch (Exception e) {
                RenLogger.LOGGER.error("Error stopping transition!", e);
                RenJava.writeStackTrace(e);
            }
            RenJava.PLAYER.setCurrentTransition(null);
            playing = false;

        }
    }

    public void handleEvents(RenScene scene) {
        TransitionStopEvent event = new TransitionStopEvent(this, scene);

        if (this == scene.getStartTransition()) {
            event.setStartTransition(true);
        }

        if (getEngineInterface() != null) {
            // Interface for the engine to handle on.
            // DO NOT modify this.
            getEngineInterface().onEnd(event);
        }
        if (getOnFinish() != null) {
            getOnFinish().onEnd(event);
        }

        RenJava.getEventHandler().callEvent(event);
    }
}
