package me.piitex.renjava.api.scenes.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

public class AnimationBuilder {
    private final RenScene renScene;
    private final AnimationType animationType;
    private final AnimationTarget animationTarget;
    private final int seconds;

    public AnimationBuilder(RenScene renScene, AnimationType animationType, AnimationTarget animationTarget, int seconds) {
        this.renScene = renScene;
        this.animationType = animationType;
        this.animationTarget = animationTarget;
        this.seconds = seconds;
    }

    public Timeline build() throws ImageNotFoundException {
        Timeline timeline = null;
        // TODO: 2/7/2024 Calculate keyframes base on the time inputted.
        if (animationTarget == AnimationTarget.IMAGE) {
            ImageView imageView = new ImageView(renScene.getBackgroundImage().getImage());
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0), new KeyValue(imageView.opacityProperty(), 0)); // First second
            KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(0.5), new KeyValue(imageView.opacityProperty(), 0.2)); // First second
            KeyFrame keyFrame3 = new KeyFrame(Duration.seconds(1.5), new KeyValue(imageView.opacityProperty(), 0.5)); // First second
            KeyFrame keyFrame4 = new KeyFrame(Duration.seconds(2), new KeyValue(imageView.opacityProperty(), 1)); // First second
            timeline = new Timeline(keyFrame, keyFrame2, keyFrame3, keyFrame4);
        }
        return timeline;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public int getSeconds() {
        return seconds;
    }
}
