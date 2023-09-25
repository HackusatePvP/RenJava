package me.piitex.renjava.api.stories;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.APIChange;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.ImageScene;
import me.piitex.renjava.api.scenes.types.InteractableScene;
import me.piitex.renjava.api.stories.handler.StoryEndInterface;
import me.piitex.renjava.api.stories.handler.StoryStartInterface;
import me.piitex.renjava.events.exceptions.DuplicateSceneIdException;

import java.util.*;
import java.util.logging.Logger;


/**
 * The Story class represents a narrative or gameplay progression in the RenJava framework.
 * It provides a way to organize and present a collection of scenes in a specific order to create a cohesive story or gameplay experience.
 * Stories provide a structured way to define the flow and progression of the game.
 *
 * <p>
 * To create a custom story, instantiate the Story class with a unique ID and add scenes to it using the {@link #addScene(RenScene)} or {@link #addScenes(RenScene...)} methods.
 * Define the starting and ending points of the story using the {@link #onStart(StoryStartInterface)} and {@link #onEnd(StoryEndInterface)} methods, respectively.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * Story myStory = new Story("my-story-id");
 * myStory.addScene(scene1);
 * myStory.addScene(scene2);
 * myStory.onStart(() -> {
 *     // Code to execute when the story starts
 * });
 * myStory.onEnd(() -> {
 *     // Code to execute when the story ends
 * });
 * }</pre>
 * </p>
 *
 * <p>
 * Note: The Story class is no longer abstract and can be instantiated directly to create custom stories.
 * </p>
 *
 * @see RenScene
 * @see ImageScene
 * @see InteractableScene
 * @see StoryStartInterface
 * @see StoryEndInterface
 */
@APIChange(changedVersion = "0.0.153", description = "Story class use to be abstract however upon testing and feedback it will work the same as scenes. JavaDoc has been updated to reflect these changes.")
public class Story {
    private final String id;

    private final LinkedHashMap<String, RenScene> scenes = new LinkedHashMap<>(); // Linked maps should order by insertion.
    private final TreeMap<Integer, RenScene> sceneIndexMap = new TreeMap<>();

    private StoryStartInterface startInterface;
    private StoryEndInterface endInterface;

    private final Logger logger = RenJava.getInstance().getLogger();

    /**
     * Creates a base story line. This can also be referred to character events or even chapters.
     * @param id Used to get the scene later.
     */
    public Story(String id) {
        this.id = id;
        // Global var
        RenJava renJava = RenJava.getInstance();
        renJava.getStoryManager().addStory(this); // Registers the story.
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

    public String getId() {
        return id;
    }

    /**
     * Displays the first scene of the story and begins the story route.
     */
    public void start() {
        logger.info("Building scene...");
        RenScene renScene = getScene(0); // Gets the first scene index.
        renScene.build(RenJava.getInstance().getStage());

        // Update RenJava Player
        RenJava.getInstance().getPlayer().setCurrentStory(this.getId());
    }

    public void refresh() {
        scenes.clear();
        sceneIndexMap.clear();
    }

    /**
     * Scenes are ordered the same way they are created. The first scene in a story is the first scene that was created.
     * @param scene Scene to add the story.
     */
    @APIChange(changedVersion = "0.0.153", description = "The RenJava Player will now automatically track the stories when they start.")
    public void addScene(RenScene scene) {
        if (scenes.containsKey(scene.getId())) {
            new DuplicateSceneIdException(scene.getId()).printStackTrace();
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
        // FIXME: 8/19/2023 subtracting by one doesn't make sense this may be wrong but haven't tested to find out.
        return scenes.size() - 1; // Always subtract one because indexing is 0 based. First entry of the index is 0 while the size will be one.
    }

    /**
     * Gets the scene based on the index
     * @param index The index of the scene you want to get.
     * @return {@link RenScene} or null if the index does not exist.
     */
    public RenScene getScene(int index) {
        logger.info("Map size: " + sceneIndexMap.size());
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
        int index = scene.getIndex() + 1;
        return sceneIndexMap.get(index);
    }

    public RenScene getNextSceneFromCurrent() {
        return getNextScene(RenJava.getInstance().getPlayer().getCurrentScene().getId());
    }

    /**
     * Gets the next scene based on the current scene id.
     * @param id ID of the previous scene.
     * @return Returns the previous RenScene or null.
     * @throws NullPointerException if the previous scene is invalid.
     */
    public RenScene getPreviousScene(String id) {
        RenScene scene = scenes.get(id);
        int index = scene.getIndex() + 1;
        return sceneIndexMap.get(index);
    }

    public RenScene getPreviousSceneFromCurrent() {
        return getPreviousScene(RenJava.getInstance().getPlayer().getCurrentScene().getId());
    }

    public LinkedHashMap<String, RenScene> getScenes() {
        return scenes;
    }

    public TreeMap<Integer, RenScene> getSceneIndexMap() {
        return sceneIndexMap;
    }
}