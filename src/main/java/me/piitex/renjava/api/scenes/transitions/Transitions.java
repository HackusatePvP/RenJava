package me.piitex.renjava.api.scenes.transitions;

import javafx.scene.Node;
import me.piitex.renjava.api.scenes.RenScene;
import org.jetbrains.annotations.Nullable;

public abstract class Transitions {
    private TransitionFinishInterface engineInterface;
    private TransitionFinishInterface finishInterface;
    private double duration;
    private RenScene scene;

    private boolean played;

    public Transitions(double duration) {
        this.duration = duration;
    }

    public Transitions onFinish(TransitionFinishInterface finishInterface) {
        this.finishInterface = finishInterface;
        return this;
    }

    public Transitions onEngineFinish(TransitionFinishInterface finishInterface) {
        this.engineInterface = finishInterface;
        return this;
    }

    public TransitionFinishInterface getEngineInterface() {
        return engineInterface;
    }

    public TransitionFinishInterface getOnFinish() {
        return finishInterface;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public abstract boolean isPlaying();

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public RenScene getScene() {
        return scene;
    }

    public void setScene(RenScene scene) {
        this.scene = scene;
    }

    public abstract void play(Node node);

    public abstract void play(RenScene scene);

    public abstract void stop();

}
