package me.piitex.renjava;

import javafx.stage.Stage;
import me.piitex.renjava.api.APIChange;
import me.piitex.renjava.api.APINote;
import me.piitex.renjava.api.music.Tracks;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.defaults.GameFlowEventListener;
import me.piitex.renjava.events.defaults.MenuClickEventListener;
import me.piitex.renjava.events.defaults.ScenesEventListener;
import me.piitex.renjava.events.defaults.StoryHandlerEventListener;
import me.piitex.renjava.gui.splashscreen.SplashScreenView;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.gui.title.CustomTitleScreen;
import me.piitex.renjava.gui.title.MainTitleScreenView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.*;

 /**
 * The RenJava class serves as the entry point for the RenJava framework. It provides the core functionality and structure for creating visual novel games.
 * <p>
 * To use the RenJava framework, create a new class that extends the `RenJava` class and override its methods to define the behavior of your game.
 * The extended class will serve as the entry point for your game.
 * <p>
 * The `RenJava` class handles various aspects of the game, including managing the game window, handling events, managing characters and stories, and saving and loading game data.
 * It also provides methods for registering event listeners, characters, and persistent data objects.
 * <p>
 * To start the game, create an instance of your extended `RenJava` class and pass the necessary parameters, such as the game name, author, and version, to the constructor.
 * The `RenJava` framework will automatically create an instance of your class and initialize the game.
 * <pre>{@code
 *     public class MyGameClass extends RenJava {
 *
 *         // Note: The constructor cannot contain any parameters.
 *         public MyGameClass() {
 *             super("game name", "author", "version");
 *         }
 *
 *         // abstraction methods.
 *     }
 * }</pre>
 *
 * Note: Do not call the `RenJava` constructor directly. The framework creates a new instance of your class automatically using reflections.
 */
public abstract class RenJava {
    private final String name;
    private final String author;
    private final String version;
    private final Logger logger;
    private final Player player;
    // Audio Tracking
    private final Tracks tracks;
    private Stage stage; // Move this somewhere else.
    private StageType stageType;

    private MainTitleScreenView mainTitleScreenView;
    private CustomTitleScreen customTitleScreen;

    private RenJavaConfiguration configuration;

    @Deprecated
    @APIChange(description = "Field has been moved to the RenJavaConfiguration.", changedVersion = "0.0.311")
    private FontLoader defaultFont;

    // User settings
     private SettingsProperties settings;

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
        this.tracks = new Tracks();
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
        this.registerListener(new MenuClickEventListener());
        this.registerListener(new GameFlowEventListener());
        this.registerListener(new StoryHandlerEventListener());
        this.registerListener(new ScenesEventListener());
        this.registerData(player);
        this.registerData(tracks);
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

    public Tracks getTracks() {
        return tracks;
    }

    public Logger getLogger() {
        return logger;
    }

    public String getBuildVersion() {
        return "0.0.311";
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

     public MainTitleScreenView getMainTitleScreenView() {
         return mainTitleScreenView;
     }

     public void setMainTitleScreenView(MainTitleScreenView mainTitleScreenView) {
         this.mainTitleScreenView = mainTitleScreenView;
     }

     public CustomTitleScreen getCustomTitleScreen() {
         return customTitleScreen;
     }

     public void setCustomTitleScreen(CustomTitleScreen customTitleScreen) {
         this.customTitleScreen = customTitleScreen;
     }

     public RenJavaConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RenJavaConfiguration configuration) {
        this.configuration = configuration;
    }

    @Deprecated
    @APIChange(description = "Field is being moved to the RenJavaConfiguration.", changedVersion = "0.0.311")
    public FontLoader getDefaultFont() {
        return configuration.getDefaultFont();
    }

    @Deprecated
    @APIChange(description = "Field is being moved to the RenJavaConfiguration.", changedVersion = "0.0.311")
    public void setDefaultFont(FontLoader defaultFont) {
        configuration.setDefaultFont(defaultFont);
    }

     public SettingsProperties getSettings() {
         return settings;
     }

     public void setSettings(SettingsProperties settings) {
         this.settings = settings;
     }

     /**
     * Registers a character in the RenJava framework.
     * <p>
     * The registerCharacter() method is used to register a character in the RenJava framework.
     * Registered characters can be accessed and managed by other parts of the framework using their unique ID.
     *
     * @param character The character object to be registered.
     *
     * @see Character
     * @see RenJava#getCharacter(String)
     */
    public void registerCharacter(Character character) {
        registeredCharacters.put(character.getId().toLowerCase(), character);
    }

    public Collection<Character> getCharacters() {
        return registeredCharacters.values();
    }

    /**
     * Retrieves a character by its ID.
     * <p>
     * The getCharacter() method is used to retrieve a character object based on its ID.
     * Characters are registered using the registerCharacter() method and can be accessed using their unique ID.
     *
     * @param id The ID of the character to retrieve.
     * @return The character object with the specified ID, or null if no character is found with the given ID.
     *
     * @see Character
     * @see RenJava#registerCharacter(Character)
     */
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

    /**
     * Returns a collection of registered persistent data objects.
     * <p>
     * The getRegisteredData() method is used to retrieve a collection of persistent data objects that have been registered using the registerData() method.
     * These data objects implement the PersistentData interface and are used for saving and loading data in the game.
     * @return A collection of registered persistent data objects.
     *
     * @see PersistentData
     * @see RenJava#registerData(PersistentData)
     */
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
     * This method is called before the game's title screen is shown. It is recommended to implement this method to perform any necessary setup or initialization tasks before the game starts.
     * <p>
     * In the `preEnabled()` method, you can create player data, initialize global variables, or perform any other actions that need to be done before the game begins.
     * This method provides an opportunity to set up the initial state of the game and prepare any necessary resources.
     * <p>
     * It is important to note that this method is called before the title screen is loaded, so any UI-related operations should be avoided in this method.
     * Instead, focus on setting up the game's initial state and preparing any data or variables that will be used throughout the game.
     * <p>
     * Implementing this method allows you to customize the behavior of your game and ensure that it is properly initialized before the player starts interacting with it.
     * <p>
     * Example usage:
     * <pre>{@code
     *     public void preEnabled() {
     *         // Register events, setup global variables, ect...
     *     }
     * }</pre>
     */
    public abstract void preEnabled();

    /**
     * This method is called after the splash screen is created and before the title screen is loaded. It is used to create characters and perform any necessary setup tasks.
     * <p>
     * In the `createBaseData()` method, you can create instances of your characters, initialize their properties, and add them to the game.
     * You can also perform any other necessary setup tasks that need to be done before the game starts.
     * <p>
     * This method provides an opportunity to set up the initial characters and perform any necessary setup tasks that will be used in your game.
     * It is recommended to create and configure your characters and perform any necessary setup tasks in this method to ensure they are ready for use when the game starts.
     * <p>
     * Example usage:
     * <pre>{@code
     *     public void createBaseData() {
     *         // Create and configure characters
     *         new Character("character1", "Character 1", Color.RED);
     *         new Character("character2", "Character 2", Color.BLUE);
     *
     *         // Perform any other necessary setup tasks
     *     }
     * }</pre>
     */
    public abstract void createBaseData();

    /**
     * Called to create a splash screen that is displayed before the title screen is loaded.
     * <p>
     * The `buildSplashScreen()` method should return a `SplashScreenView` object, which represents the splash screen view to be displayed.
     * You can customize the appearance and behavior of the splash screen by configuring the `SplashScreenView` object.
     * <p>
     * The `SplashScreenView` class provides methods for setting the splash screen image, duration, and any additional UI elements or animations.
     * You can use these methods to create an engaging and visually appealing splash screen for your game.
     * <p>
     * Example usage:
     * <pre>{@code
     *     public SplashScreenView buildSplashScreen() {
     *         // Create a SplashScreenView object
     *         SplashScreenView splashScreen = new SplashScreenView();
     *
     *         // Set the splash screen image
     *         splashScreen.setImage("splash.png");
     *
     *         // Set the duration of the splash screen (in seconds)
     *         splashScreen.setDuration(3);
     *
     *         // Return the SplashScreenView object
     *         return splashScreen;
     *     }
     * }</pre>
     *
     * @return A `SplashScreenView` object representing the splash screen view to be displayed.
     */
    public abstract SplashScreenView buildSplashScreen();

    /**
     * Called to create the main menu. (This is NOT optional)
     * @return A MainTitleScreenView object which is parsed to a stage
     */
    public abstract MainTitleScreenView buildTitleScreen();

    /**
     * Function used to create your story methods.
     * <p>
     * In the `createStory()` method, you can define and create your story methods.
     * This is where you can define the flow of your visual novel game, including the scenes, choices, and branching paths.
     * <p>
     * You can create a subclass of the `Story` class and implement the necessary methods and functionality to define your story.
     * Add scenes to the story, define the starting and ending points, and handle the flow of the story.
     * <p>
     * Example usage:
     * <pre>{@code
     *     public class MyStory extends Story {
     *
     *         public MyStory(String id) {
     *             super(id);
     *         }
     *
     *         @Override
     *         private void init() {
     *             // You can create scenes here.
     *         }
     *
     *     }
     * }</pre>
     *
     * Initializing Story class:
     * <pre>{@code
     *     public class MyRenJavaClass extends RenJava {
     *
     *         @Override
     *         public void createStory() {
     *           new MyStoryClass("my-story-id);
     *         }
     *
     *     }
     * }</pre>
     */
    public abstract void createStory();

    /**
     * Displays the first scene of the story and begins the story route.
     * <br>
     * <p>
     *     The start() method is used to initiate the story by displaying the first scene of the story.
     *     It retrieves the current story from the StoryManager using the story ID and builds the scene on the stage.
     *     This method should be called when the game starts.
     * </p>
     *
     * Example usage:
     * <pre>{@code
     *     // Get the current story from the StoryManager
     *     Story myStory = this.getStoryManager().getStory("my-story");
     *     // Start the story
     *     myStory.start();
     * }</pre>
     */
    public abstract void start();

    /* All static methods below */

    public static RenJava getInstance() {
        return instance;
    }

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
        Collection<Method> lowestMethods = new HashSet<>();
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
                            case LOWEST -> lowestMethods.add(method);
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
        for (Method method : lowestMethods) {
            invokeMethod(method, event);
        }
    }

    private static void invokeMethod(Method method, Event event) {
        try {
            method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), event);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}