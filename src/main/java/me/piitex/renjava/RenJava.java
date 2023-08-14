package me.piitex.renjava;

import javafx.stage.Stage;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.stories.StoryManager;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.defaults.GameFlowEventListener;
import me.piitex.renjava.events.defaults.MenuClickEventListener;
import me.piitex.renjava.events.defaults.StoryHandlerEventListener;
import me.piitex.renjava.gui.splashscreen.SplashScreenView;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.builders.FontLoader;
import me.piitex.renjava.gui.title.MainTitleScreenView;
import me.piitex.renjava.api.music.Track;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.*;

/**
 * The `RenJava` class serves as the entry point for the RenJava framework. It provides the core functionality and structure for creating visual novel games.
 * <p>
 * To use the RenJava framework, create a new class that extends the `RenJava` class and override its methods to define the behavior of your game.
 * The extended class will serve as the entry point for your game.
 * <p>
 * The `RenJava` class handles various aspects of the game, including managing the game window, handling events, managing characters and stories, and saving and loading game data.
 * It also provides methods for registering event listeners, characters, and persistent data objects.
 * <p>
 * To start the game, create an instance of your extended `RenJava` class and pass the necessary parameters, such as the game name, author, and version, to the constructor.
 * The `RenJava` framework will automatically create an instance of your class and initialize the game.
 * <p>
 * Note: Do not call the `RenJava` constructor directly. The framework creates a new instance of your class automatically using reflection.
 *
 */
public abstract class RenJava {
    private final String name;
    private final String author;
    private final String version;
    private final Logger logger;
    private final Player player;
    private Stage stage; // Move this somewhere else.
    private StageType stageType;
    private RenJavaConfiguration configuration;
    private FontLoader defaultFont;
    // Story manager
    private StoryManager storyManager;

    // Audio tracking
    private Track track;

    private final Map<String, Character> registeredCharacters = new HashMap<>();
    private final Collection<EventListener> registeredListeners = new HashSet<>();
    private final Collection<PersistentData> registeredData = new HashSet<>();

    private static RenJava instance;

    /**
     * Entry point for the RenJava framework. This class is designed to be extended by your own class, which will serve as the entry point for your game.
     * <p>
     * Note: Do not call this constructor directly. The RenJava framework creates a new instance of your class automatically using reflection.
     *
     * @param name    The name of the game, used for displaying the game in the window and other various places.
     * @param author  The author of the game.
     * @param version The current version of the game, used to display specific information about the game.
     */
    public RenJava(String name, String author, String version) {
        instance = this;
        this.name = name;
        this.author = author;
        this.version = version;
        this.player = new Player();
        // Load logger
        this.logger = Logger.getLogger(name);
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("log.txt");
            fileHandler.setFormatter(new RenLoggerFormat());
            logger.addHandler(fileHandler);
            logger.info("Starting Ren-Java");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.defaultFont = new FontLoader("JandaManateeSolid.ttf", 16);
        this.registerListener(new MenuClickEventListener());
        this.registerListener(new GameFlowEventListener());
        this.registerListener(new StoryHandlerEventListener());
        this.registerData(player);
        new RenLoader(this);
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public Player getPlayer() {
        return player;
    }

    public Logger getLogger() {
        return logger;
    }

    public String getBuildVersion() {
        return "0.0.153";
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage, StageType type) {
        this.stage = stage;
        this.stageType = type;
    }

    public StageType getStageType() {
        return stageType;
    }

    public RenJavaConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RenJavaConfiguration configuration) {
        this.configuration = configuration;
    }

    public FontLoader getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(FontLoader defaultFont) {
        this.defaultFont = defaultFont;
    }

    public StoryManager getStoryManager() {
        return storyManager;
    }

    public void setStoryManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public void registerCharacter(Character character) {
        registeredCharacters.put(character.getId().toLowerCase(), character);
    }

    public Collection<Character> getCharacters() {
        return registeredCharacters.values();
    }

    public Character getCharacter(String id) {
        return registeredCharacters.get(id.toLowerCase());
    }

    /**
     * Registers a data class implementing the {@link PersistentData} interface for saving and loading data.
     * <p>
     * The {@code registerData} method is used to enable the saving feature for a data class. By registering the data class,
     * its fields annotated with the {@link Data} annotation will be included in the save file.
     * <p>
     * To register a data class, pass an instance of the data class that implements the {@link PersistentData} interface as
     * the parameter to this method.
     *
     * @param data The data class implementing the {@link PersistentData} interface to be registered for saving and loading data.
     *
     * @see PersistentData
     * @see Data
     */
    public void registerData(PersistentData data) {
        registeredData.add(data);
    }

    public Collection<PersistentData> getRegisteredData() {
        return registeredData;
    }

    /**
     * Registers an {@link EventListener} to handle game events.
     * <p>
     * The {@code registerListener} method is used to register an {@link EventListener} that handles game events.
     * Event listeners can listen for specific events and perform actions in response to those events.
     * <p>
     * To register an event listener, pass an instance of the event listener class that implements the {@link EventListener} interface
     * as the parameter to this method.
     *
     * @param listener The event listener implementing the {@link EventListener} interface to be registered for handling game events.
     *
     * @see EventListener
     * @see Listener
     */
    public void registerListener(EventListener listener) {
        this.registeredListeners.add(listener);
    }

    public Collection<EventListener> getRegisteredListeners() {
        return registeredListeners;
    }

    /**
     * This is called before the games title screen is shown. It is recommended to create player data and global vars at this stage.
     */
    public abstract void preEnabled();

    /**
     * This is called after the splash screen is created and before the title screen is loaded. Create characters and story elements here.
     */
    public abstract void createBaseData();

    /**
     * Called to create a 800x800 splash screen (small box). (Optional)
     *
     * @return A SplashScreenView object which is parsed to a stage.
     */
    public abstract SplashScreenView buildSplashScreen();

    /**
     * Called to create the main menu. (This is NOT optional)
     * @return A MainTitleScreenView object which is parsed to a stage
     */
    public abstract MainTitleScreenView buildTitleScreen();

    /**
     * Function used to create your story methods.
     */
    public abstract void createStory();

    /**
     * Function called when a player starts a new game.
     */
    public abstract void start();

    public static RenJava getInstance() {
        return instance;
    }

    /* All static methods below */

    /**
     * The event system in RenJava allows for the handling of various game events and actions. Events are an extension of "actions" during the game, such as player clicks or game flow changes.
     * <p>
     * The event system follows a listener-based architecture, where event listeners can register to listen for specific events and perform actions in response to those events.
     * <p>
     * To create a custom event, you can extend the {@link Event} class and define your own event-specific properties and methods.
     * <p>
     * To handle events, you can implement the {@link EventListener} interface and register your listener with the RenJava framework.
     * <p>
     * The event system supports different event priorities, allowing you to control the order in which listeners are invoked for a particular event.
     * <p>
     * To trigger an event, you can use the {@link RenJava#callEvent(Event)} method, which will invoke all registered listeners for that event.
     * <p>
     * To handle events, annotate a method with the {@link Listener} annotation and provide the event type as the parameter. For example:
     * <pre>{@code
     *     @Listener
     *     public void onMouseClickEvent(MouseClickEvent event) {
     *         // Code to handle the mouse click event
     *     }
     * }</pre>
     *
     * @see Event
     * @see EventListener
     * @see Listener
     *
     * @param event Event to be executed.
     */
    public static void callEvent(Event event) {
        Collection<Method> lowMethods = new HashSet<>();
        Collection<Method> normalMethods = new HashSet<>();
        Collection<Method> highMethods = new HashSet<>();
        for (EventListener listener : RenJava.getInstance().getRegisteredListeners()) {
            for (Method method : listener.getClass().getMethods()) {
                if (method.isAnnotationPresent(Listener.class)) {
                    Class<?>[] params = method.getParameterTypes();
                    boolean scan = false;
                    for (Class<?> param : params) {
                        if (param.isInstance(event)) {
                            scan = true;
                            break;
                        }
                    }
                    if (scan) {
                        Listener listener1 = method.getAnnotation(Listener.class);
                        switch (listener1.priority()) {
                            case HIGH -> highMethods.add(method);
                            case NORMAL -> normalMethods.add(method);
                            case LOW -> lowMethods.add(method);
                        }
                    }
                }
            }
        }
        // There has got to be a way to make this better.
        for (Method method : highMethods) {
            invokeMethod(method, event);
        }
        for (Method method : normalMethods) {
            invokeMethod(method, event);
        }
        for (Method method : lowMethods) {
           invokeMethod(method, event);
        }
    }

    private static void invokeMethod(Method method, Event event) {
        try {
            method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), event);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}