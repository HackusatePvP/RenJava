package me.piitex.renjava.api.scenes.transitions.types;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import me.piitex.renjava.api.scenes.transitions.Transitions;

public class TranslationTransition extends Transitions {
    private final double byX;
    private final double byY;
    private final double fromX;
    private final double fromY;
    private final double toX;
    private final double toY;
    private boolean playing;

    private TranslationTransition transition;

    public TranslationTransition(double byX, double byY, double fromX, double fromY, double toX, double toY, int duration) {
        super(duration);
        this.byX = byX;
        this.byY = byY;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public double getByX() {
        return byX;
    }

    public double getByY() {
        return byY;
    }

    public double getFromX() {
        return fromX;
    }

    public double getFromY() {
        return fromY;
    }

    public double getToX() {
        return toX;
    }

    public double getToY() {
        return toY;
    }

    public TranslationTransition getTransition() {
        return transition;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void play(Node node) {
        TranslateTransition transition = new TranslateTransition();
        transition.setByX(byX);
        transition.setByY(byY);
        transition.setFromX(fromX);
        transition.setFromY(fromY);
        transition.setToX(toX);
        transition.setToY(toY);
        transition.setNode(node);
        transition.setDuration(Duration.seconds(getDuration()));
        transition.setOnFinished(actionEvent -> {
            if (getOnFinish() != null) {
                getOnFinish().onEnd(actionEvent);
                playing = false;
            }
        });
        playing = true;
        transition.play();
    }

    @Override
    public void stop() {
        transition.stop();
        playing = false;
    }
}
