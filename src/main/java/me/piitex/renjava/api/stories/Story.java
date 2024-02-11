package me.piitex.renjava.api.stories;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.AnimationScene;
import me.piitex.renjava.api.scenes.types.ImageScene;
import me.piitex.renjava.api.scenes.types.InteractableScene;
import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.api.stories.handler.StoryEndInterface;
import me.piitex.renjava.api.stories.handler.StoryStartInterface;
import me.piitex.renjava.events.exceptions.DuplicateSceneIdException;
import me.piitex.renjava.events.types.SceneBuildEvent;
import me.piitex.renjava.events.types.SceneEndEvent;
import me.piitex.renjava.gui.Menu;

import java.util.*;
import java.util.logging.Logger;

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
 * @see AnimationScene
 * @see ChoiceScene
 * @see InputScene
 */
public abstract class Story {
    private final String id;

    private final LinkedHashMap<String, RenScene> scenes = new LinkedHashMap<>(); // Linked maps should order by insertion.
    private final TreeMap<Integer, RenScene> sceneIndexMap = new TreeMap<>();

    private StoryStartInterface startInterface;
    private StoryEndInterface endInterface;

    private final Logger logger = RenJava.getInstance().getLogger();

    private final RenJava renJava;

    /**
     * Creates a base story line. This can also be referred to character events or even chapters.
     * @param id Used to get the scene later.
     */
    public Story(String id) {
        this.id = id;
        // Global var
        renJava = RenJava.getInstance();
        renJava.getPlayer().addStory(this); // Registers the story.
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
        init(); // Initialize when starting

        logger.info("Building scene...");
        RenScene renScene = getScene(0); // Gets the first scene index.

        Menu menu = renScene.build(true);

        renJava.getPlayer().updateScene(renScene); // Set to current scene.

        SceneBuildEvent buildEvent = new SceneBuildEvent(renScene, menu);
        RenJava.callEvent(buildEvent);

        renScene.render(menu);

        // Update RenJava Player
        renJava.getPlayer().setCurrentStory(this.getId());
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

    /**
     * Refreshes a specific scene in the story by replacing it with a new instance of the scene.
     * This method is useful when you want to update a scene dynamically during the story.
     *
     * @param sceneID The ID of the scene to refresh.
     */
    public void refresh(String sceneID) {
        RenScene scene = getScene(sceneID);
        scenes.replace(sceneID, scene, scene);
    }

    /**
     * Scenes are ordered the same way they are created. The first scene in a story is the first scene that was created.
     * @param scene Scene to add the story.
     */
    public void addScene(RenScene scene) {
        if (scenes.containsKey(scene.getId())) {
            logger.severe(new DuplicateSceneIdException(scene.getId()).getMessage());
            return;
        }
        scenes.put(scene.getId(), scene);
        int index = sceneIndexMap.size();
        sceneIndexMap.put(index, scene);
        scene.setIndex(index);
        scene.setStory(this); // Updates the scene data.
    }

    /**
     * Scenes are ordered the same way they are created. The first scene in a story is the first scene that was created.
     * @param scenes All the scenes to add to the story.
     */
    public void addScenes(RenScene... scenes) {
        for (RenScene renScene : scenes) {
            this.scenes.put(renScene.getId(), renScene);
            int index = sceneIndexMap.size();
            sceneIndexMap.put(index, renScene);
            renScene.setIndex(index);
            renScene.setStory(this);
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
     * @throws NullPointerException if the next scene is invalid.
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
        return getNextScene(renJava.getPlayer().getCurrentScene().getId());
    }

    public RenScene getCurrentScene() {
        return renJava.getPlayer().getCurrentScene();
    }

    /**
     * Gets the next scene based on the current scene id.
     * @param id ID of the previous scene.
     * @return Returns the previous RenScene or null.
     * @throws NullPointerException if the previous scene is invalid.
     */
    public RenScene getPreviousScene(String id) {
        RenScene scene = scenes.get(id);
        int index = scene.getIndex() - 1;
        return sceneIndexMap.get(index);
    }

    public RenScene getPreviousSceneFromCurrent() {
        return getPreviousScene(renJava.getPlayer().getCurrentScene().getId());
    }

    public void displayScene(int index) {
        RenScene scene = getScene(index);
        displayScene(scene);
    }

    public void displayScene(String id) {
        RenScene scene = getScene(id);
        displayScene(scene);
    }

    public void displayScene(RenScene scene) {
        SceneEndEvent event = new SceneEndEvent(getCurrentScene());
        RenJava.callEvent(event);
        RenJava.getInstance().getPlayer().updateScene(scene);
        Menu menu = scene.build(true);
        menu.render(null, scene);
    }

    public void displayNextScene() {
        SceneEndEvent event = new SceneEndEvent(getPreviousSceneFromCurrent());
        RenJava.callEvent(event);
        Menu menu = getNextSceneFromCurrent().build(true);
        menu.render(null, getNextSceneFromCurrent());
    }

    public LinkedHashMap<String, RenScene> getScenes() {
        return scenes;
    }

    public TreeMap<Integer, RenScene> getSceneIndexMap() {
        return sceneIndexMap;
    }
}