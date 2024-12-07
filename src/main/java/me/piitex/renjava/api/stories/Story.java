package me.piitex.renjava.api.stories;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.APINote;
import me.piitex.renjava.api.scenes.types.animation.VideoScene;
import me.piitex.renjava.events.types.SceneStartEvent;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.ImageScene;
import me.piitex.renjava.api.scenes.types.InteractableScene;
import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.api.stories.handler.StoryEndInterface;
import me.piitex.renjava.api.stories.handler.StoryStartInterface;
import me.piitex.renjava.events.types.SceneEndEvent;
import org.slf4j.Logger;

import java.util.*;

/**
 * The Story class represents a narrative or gameplay progression in the RenJava framework.
 * It provides a way to organize and present a collection of scenes in a specific order to create a cohesive story or gameplay experience.
 * Stories provide a structured way to define the flow and progression of the game.
 *
 * <p>
 * To create a custom story, create a subclass of the Story class and implement the necessary methods and functionality to define your story.
 * Add scenes to the story using the {@link #addScene(RenScene)} or {@link #addScenes(RenScene...)} methods.
 * Define the starting and ending points of the story using the {@link #onStart(StoryStartInterface)} and {@link #onEnd(StoryEndInterface)} methods, respectively.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * public class MyStory extends Story {
 *
 *     public MyStory(String id) {
 *         super(id);
 *     }
 *
 *      @Override
 *      public void init() {
 *          // Add scenes to the story.
 *      }
 *
 *     // Implement necessary methods and functionality for your story
 * }
 * }</pre>
 * </p>
 *
 * <p>
 * Note: The Story class is now abstract and should be extended to create custom stories.
 * </p>
 *
 * @see RenScene
 * @see ImageScene
 * @see InteractableScene
 * @see VideoScene
 * @see ChoiceScene
 * @see InputScene
 */
public abstract class Story {
    private final String id;

    private final LinkedHashMap<String, RenScene> scenes = new LinkedHashMap<>(); // Linked maps should order by insertion.
    private final TreeMap<Integer, RenScene> sceneIndexMap = new TreeMap<>();

    private StoryStartInterface startInterface;
    private StoryEndInterface endInterface;

    private final Logger logger = RenLogger.LOGGER;

    /**
     * Creates a base story line. This can also be referred to character events or even chapters.
     * @param id Used to get the scene later.
     */
    public Story(String id) {
        this.id = id;
        // Global var

        RenLogger.LOGGER.info("Registering story '{}'", id);
        RenJava.PLAYER.addStory(this); // Registers the story.
    }

    public Story onEnd(StoryEndInterface endInterface) {
        this.endInterface = endInterface;
        return this;
    }

    public Story onStart(StoryStartInterface storyStartInterface) {
        this.startInterface = storyStartInterface;
        return this;
    }

    public StoryStartInterface getStartInterface() {
        return startInterface;
    }

    public StoryEndInterface getEndInterface() {
        return endInterface;
    }

    public abstract void init();

    public String getId() {
        return id;
    }

    /**
     * Displays the first scene of the story and begins the story route.
     */
    public void start() {
        // Update RenJava Player BEFORE the scenes are added
        RenJava.PLAYER.setCurrentStory(this.getId());

        clear(); // Clear previous mappings (allows refreshing)
        init(); // Initialize when starting

        RenScene renScene = getScene(0); // Gets the first scene index.

        if (renScene == null) {
            RenLogger.LOGGER.error("Story has no scenes in index. Is the story empty?");
            return;
        }

        RenLogger.LOGGER.debug("Rendering first scene...");
        displayScene(renScene, false, true);
    }

    /**
     * Clears the existing scenes and initializes the story again by calling the `init()` method.
     * This method is useful when you want to reset and update variables.
     * Every time a story starts, it is refreshed.
     */
    public void refresh() {
        scenes.clear();
        sceneIndexMap.clear();
        init();
    }

    public void clear() {
        scenes.clear();
        sceneIndexMap.clear();
    }

    /**
     * Scenes are ordered the same way they are created. The first scene in a story is the first scene that was created.
     * @param scene Scene to add the story.
     */
    public void addScene(RenScene scene) {
        if (scenes.containsKey(scene.getId())) {
            logger.warn(new DuplicateSceneIdException(scene.getId()).getMessage());
            scenes.replace(scene.getId(), scenes.get(id), scene);
            return;
        }
        scene.setStory(this);
        scenes.put(scene.getId(), scene);
        int index = sceneIndexMap.size();
        sceneIndexMap.put(index, scene);
        scene.setIndex(index);
    }

    /**
     * Scenes are ordered the same way they are created. The first scene in a story is the first scene that was created.
     * @param scenes All the scenes to add to the story.
     */
    public void addScenes(RenScene... scenes) {
        for (RenScene renScene : scenes) {
            addScene(renScene);
        }
    }

    /**
     * Gets the current index of the scene. Index represents the numeric order of the scenes. The first scene in a story has an index of 0.
     * @param scene The scene you want the index of.
     * @return Index of the scene provided. This will return -1 if the scene was not found.
     */
    public int getSceneIndex(RenScene scene) {
        return scene.getIndex();
    }

    /**
     * Gets the last index of all the scenes.
     * @return The last index.
     */
    public int getLastIndex() {
        return scenes.size() - 1; // Always subtract one because indexing is 0 based. First entry of the index is 0 while the size will be one.
    }

    /**
     * Gets the scene based on the index
     * @param index The index of the scene you want to get.
     * @return {@link RenScene} or null if the index does not exist.
     */
    public RenScene getScene(int index) {
        return sceneIndexMap.get(index);
    }

    /**
     * Gets a scene by its string id.
     * @param id of the Scene
     * @return the scene of the id or null if none found.
     */
    public RenScene getScene(String id) {
        return scenes.get(id);
    }

    /**
     * Gets the next scene based on the current scene id.
     * @param id ID of the next scene.
     * @return Returns the next RenScene or null.
     */
    public RenScene getNextScene(String id) {
        RenScene scene = scenes.get(id);
        if (scene == null) {
            return null;
        }
        int index = scene.getIndex() + 1;
        return sceneIndexMap.get(index);
    }

    public RenScene getNextSceneFromCurrent() {
        return getNextScene(RenJava.PLAYER.getCurrentScene().getId());
    }

    public RenScene getCurrentScene() {
        return RenJava.PLAYER.getCurrentScene();
    }

    /**
     * Gets the next scene based on the current scene id.
     * @param id ID of the previous scene.
     * @return Returns the previous RenScene or null.
     */
    public RenScene getPreviousSceneFromID(String id) {
        RenScene scene = scenes.get(id);
        int index = scene.getIndex() - 1;
        return sceneIndexMap.get(index);
    }

    public RenScene getPreviousSceneFromCurrent() {
        return getPreviousSceneFromID(RenJava.PLAYER.getCurrentScene().getId());
    }

    @APINote(description = "Scene indexes start at 0 which is the first scene. The second scene would be '1'.")
    public void displayScene(int index) {
        RenScene scene = getScene(index);
        displayScene(scene);
    }

    public void displayScene(String id) {
        RenScene scene = getScene(id);
        displayScene(scene);
    }


    public void displayScene(RenScene scene) {
        displayScene(scene, false);
    }

    public void displayScene(RenScene scene, boolean rollback) {
        displayScene(scene, rollback, true);
    }

    public void displayScene(RenScene scene, boolean rollback, boolean events) {
        RenLogger.LOGGER.info("Rendering scene {} in story {}", scene.getId(), scene.getStory().getId());
        if (events) {
            SceneEndEvent endEvent = new SceneEndEvent(getCurrentScene());
            RenJava.callEvent(endEvent);

            SceneStartEvent startEvent = new SceneStartEvent(scene);
            RenJava.callEvent(startEvent);
        }

        RenJava.PLAYER.updateScene(scene);

        scene.render(RenJava.getInstance().getGameWindow(),true, events);

        RenJava.PLAYER.setCurrentStageType(scene.getStageType());
        if (!rollback) {
            // 0,1,2,3,
            RenJava.PLAYER.getViewedScenes().put(RenJava.PLAYER.getViewedScenes().size() + 1, Map.entry(scene.getId(), this.getId()));
            RenJava.PLAYER.getRolledScenes().put(RenJava.PLAYER.getRolledScenes().size() + 1, Map.entry(scene.getId(), this.getId()));
        }
    }

    public void displayNextScene() {
        RenScene renScene = getNextSceneFromCurrent();
        displayScene(renScene);
    }

    public LinkedHashMap<String, RenScene> getScenes() {
        return scenes;
    }

    public TreeMap<Integer, RenScene> getSceneIndexMap() {
        return sceneIndexMap;
    }
}