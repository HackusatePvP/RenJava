package me.piitex.renjava.api.scenes;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.animation.AnimationBuilder;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.api.scenes.types.SceneBuildInterface;
import me.piitex.renjava.gui.layouts.Container;

import me.piitex.renjava.api.scenes.types.AnimationScene;
import me.piitex.renjava.api.scenes.types.ImageScene;
import me.piitex.renjava.api.scenes.types.InteractableScene;
import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.api.stories.Story;

import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.Overlay;
import me.piitex.renjava.gui.overlay.TextOverlay;


import java.io.File;
import java.util.Collection;
import java.util.HashSet;


/**
 * The RenScene class represents a scene in the RenJava framework.
 * It serves as a base class for different types of scenes, such as image scenes and interactable scenes.
 * Scenes are used to display visuals and interact with the player during the gameplay or narrative progression.
 *
 * <p>
 * Note: Developers should not create custom RenScene types. RenJava is designed to work with the specific types of scenes provided by the framework.
 * </p>
 *
 * @see ImageScene
 * @see InteractableScene
 * @see AnimationScene
 * @see InputScene
 * @see ChoiceScene
 */
public abstract class RenScene extends Container {
    private final String id;
    private ImageLoader backgroundImage;
    private Story story;
    private int index;
    private SceneStartInterface startInterface;
    private SceneEndInterface endInterface;
    private SceneBuildInterface buildInterface;
    private AnimationBuilder startAnimation;

    private Transitions startTransition;
    private Transitions endTransition;

    private StageType stageType;

    private final Collection<Overlay> additionalOverlays = new HashSet<>();

    private final Collection<File> styleSheets = new HashSet<>();


    public RenScene(String id, ImageLoader backgroundImage) {
        this.id = id;
        this.backgroundImage = backgroundImage;
        setStory(RenJava.getInstance().getPlayer().getCurrentStory()); // Update the current story.
    }

    public RenScene onStart(SceneStartInterface sceneInterface) {
        this.startInterface = sceneInterface;
        return this;
    }

    public RenScene onEnd(SceneEndInterface endInterface) {
        this.endInterface = endInterface;
        return this;
    }

    public RenScene onBuild(SceneBuildInterface buildInterface) {
        this.buildInterface = buildInterface;
        return this;
    }

    public RenScene setBeginningAnimation(AnimationBuilder animation) {
        this.startAnimation = animation;
        return this;
    }

    public RenScene setBeginningTransition(Transitions transition) {
        this.startTransition = transition;
        return this;
    }

    public RenScene setEndTransition(Transitions transition) {
        this.endTransition = transition;
        return this;
    }

    public AnimationBuilder getStartAnimation() {
        return startAnimation;
    }

    public Transitions getStartTransition() {
        return startTransition;
    }

    public Transitions getEndTransition() {
        return endTransition;
    }

    public String getId() {
        return id;
    }

    public ImageLoader getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(ImageLoader backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public SceneStartInterface getStartInterface() {
        return startInterface;
    }

    public SceneEndInterface getEndInterface() {
        return endInterface;
    }

    public SceneBuildInterface getBuildInterface() {
        return buildInterface;
    }

    public void addOverlay(Overlay overlay) {
        additionalOverlays.add(overlay);
    }

    public Collection<Overlay> getAdditionalOverlays() {
        return additionalOverlays;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public abstract StageType getStageType();

    public void addStyleSheets(File file) {
        styleSheets.add(file);
    }

    public Collection<File> getStyleSheets() {
        return styleSheets;
    }
}
