package me.piitex.renjava.api.scenes.transitions;

import javafx.scene.Node;
import me.piitex.renjava.api.scenes.RenScene;
import org.jetbrains.annotations.Nullable;

public abstract class Transitions {
    private TransitionFinishInterface finishInterface;
    private double duration;

    public Transitions(double duration) {
        this.duration = duration;
    }

    public Transitions onFinish(TransitionFinishInterface finishInterface) {
        this.finishInterface = finishInterface;
        return this;
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

    public abstract void play(@Nullable RenScene scene, Node node);

    public abstract void stop();

}
