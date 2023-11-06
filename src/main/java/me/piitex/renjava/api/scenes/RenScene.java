package me.piitex.renjava.api.scenes;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.gui.Container;

import me.piitex.renjava.api.scenes.types.AnimationScene;
import me.piitex.renjava.api.scenes.types.ImageScene;
import me.piitex.renjava.api.scenes.types.InteractableScene;
import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.configuration.SettingsProperties;

import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.gui.overlay.Overlay;
import me.piitex.renjava.gui.overlay.TextOverlay;


import java.io.File;
import java.net.MalformedURLException;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;


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
    private final ImageLoader backgroundImage;
    private Story story;
    private int index;
    private SceneStartInterface startInterface;
    private SceneEndInterface endInterface;

    // Extremely experimental. Setting a global scene achives the ctrl skip feature but could cause ALOT of other issues.
    private final Stage stage = RenJava.getInstance().getStage();

    private final Collection<Overlay> additionalOverlays = new HashSet<>();

    private final Collection<File> styleSheets = new HashSet<>();

    public abstract StageType getStageType();

    public RenScene(String id, ImageLoader backgroundImage) {
        this.id = id;
        this.backgroundImage = backgroundImage;
    }

    public RenScene onStart(SceneStartInterface sceneInterface) {
        this.startInterface = sceneInterface;
        return this;
    }

    public RenScene onEnd(SceneEndInterface endInterface) {
        this.endInterface = endInterface;
        return this;
    }

    public String getId() {
        return id;
    }

    public ImageLoader getBackgroundImage() {
        return backgroundImage;
    }

    public SceneStartInterface getStartInterface() {
        return startInterface;
    }

    public SceneEndInterface getEndInterface() {
        return endInterface;
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

    public void hookOverlays(Group root) {
        for (Overlay overlay : getAdditionalOverlays()) {
            // Add the additional overlays to the scene
            if (overlay instanceof ImageOverlay imageOverlay) {
                ImageView imageView1 = new ImageView(imageOverlay.image());
                imageView1.setX(imageOverlay.x());
                imageView1.setY(imageOverlay.y());
                root.getChildren().add(imageView1);
            } else if (overlay instanceof TextOverlay textOverlay) {
                Text text1 = new Text(textOverlay.text());
                text1.setX(textOverlay.x());
                text1.setY(textOverlay.y());
                text1.setScaleX(textOverlay.xScale());
                text1.setScaleY(textOverlay.yScale());
                root.getChildren().add(text1);
            } else if (overlay instanceof ButtonOverlay buttonOverlay) {
                RenJava.getInstance().getLogger().info("Adding button...");
                Button button = buttonOverlay.button();
                button.setTranslateX(buttonOverlay.x());
                button.setTranslateY(buttonOverlay.y());
                root.getChildren().add(button);
            }
        }
    }

    public void addStyleSheets(File file) {
        styleSheets.add(file);
    }

    public void setStage(Stage stage, Group root, StageType type, boolean resetScene) {
        Logger logger = RenJava.getInstance().getLogger();
        if (resetScene) {
            logger.info("Resetting scene...");
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        for (File file : styleSheets) {
            try {
                stage.getScene().getStylesheets().add(file.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        if (stage.isFullScreen()) {
            logger.info("Stage is Fullscreen");
        } else {
            logger.info("Stage is Windowed.");
        }
        SettingsProperties settingsProperties = RenJava.getInstance().getSettings();
        if (settingsProperties.isFullscreen()) {
            stage.setFullScreen(true);
        }
        stage.show();
        RenJava.getInstance().getPlayer().setCurrentStory(this.getStory().getId());
        RenJava.getInstance().getPlayer().setCurrentScene(this.getId());
        SceneStartEvent startEvent = new SceneStartEvent(this);
        RenJava.callEvent(startEvent);
        RenJava.getInstance().setStage(stage, type);

        // Add scene to view. Complicated mapping but it shooould work
        RenJava.getInstance().getPlayer().getViewedScenes().put(new AbstractMap.SimpleEntry<>(story, this.getId()), this);
    }
}
