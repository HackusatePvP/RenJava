package me.piitex.renjava;

import javafx.application.HostServices;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import me.piitex.renjava.addons.AddonLoader;
import me.piitex.renjava.api.Game;
import me.piitex.renjava.api.exceptions.InvalidCharacterException;
import me.piitex.renjava.api.music.Tracks;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.configuration.Configuration;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.events.EventHandler;
import me.piitex.renjava.events.defaults.*;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.containers.ScrollContainer;
import me.piitex.renjava.gui.layouts.VerticalLayout;
import me.piitex.renjava.gui.menus.MainMenu;
import me.piitex.renjava.gui.overlays.*;
import me.piitex.renjava.loggers.RenLogger;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;

/**
 * The RenJava class serves as the entry point for the RenJava framework. It provides the core functionality and structure for creating visual novel games.
 * <p>
 * To use the RenJava framework, create a new class that extends the `RenJava` class and override its methods to define the behavior of your game.
 * The extended class will serve as the entry point for your game.
 * <p>
 * The `RenJava` class handles various aspects of the game, including managing the game window, handling events, managing characters and stories, and saving and loading game data.
 * It also provides methods for registering event listeners, characters, and persistent data objects.
 * <p>
 * The Game and Configuration annotations are used to build default systems for the framework. Although it's not a hard requirement creating these annotations
  * is very important for other systems to work as intended. The Logger relies on the Game annotation to pass the name of the logger.
  * The GUI relies on the Configuration to properly display the window.
 * <pre>{@code
  *    @Game(name = "My Game", author = "You", version = "1.0);
  *    @Configuration(title = "My Game 1.0", width = 1920, height = 1080);
 *     public class MyGameClass extends RenJava {
 *         // abstraction methods.
 *     }
 * }</pre>
 *
 * Note: Do not call the `RenJava` constructor directly. The framework creates a new instance of your class automatically using reflections.
 *
 * @see Game
 * @see Configuration
 */
public abstract class RenJava {
    protected String name;
    protected String author;
    protected String version;
    protected int id;
    public static Player PLAYER;
    public static RenJavaConfiguration CONFIGURATION;
    // Audio Tracking
    public static Tracks TRACKS;
    public static AddonLoader ADDONLOADER;
    // User settings
    public static SettingsProperties SETTINGS;
    public static EventHandler EVENTHANDLER;

    private Window gameWindow;
    private static Window errorWindow;
    private MainMenu mainMenu;

    private HostServices hostServices;

    private Logger logger;

    private final Map<String, Character> registeredCharacters = new HashMap<>();
    private final Collection<PersistentData> registeredData = new HashSet<>();

    protected String buildVersion;


    // Error tracking
    private static long lastErrorTimeStamp;
    private static int spamTrack = 0;

    private static RenJava instance;

    protected RenJava() {
        // Super is executed first then the superior method is executed.
        instance = this;
    }

    protected void init() {
        // Run after super
        PLAYER = new Player();
        TRACKS = new Tracks();
        EVENTHANDLER = new EventHandler();

        EVENTHANDLER.registerListener(new MenuClickEventListener());
        EVENTHANDLER.registerListener(new GameFlowEventListener());
        EVENTHANDLER.registerListener(new StoryHandlerEventListener());
        EVENTHANDLER.registerListener(new ScenesEventListener());
        EVENTHANDLER.registerListener(new OverlayEventListener());
        this.registerData(PLAYER);
        this.registerData(TRACKS);
        new RenLoader(this);
        ADDONLOADER = new AddonLoader();
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

    public Integer getID() {
        return id;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * @return The RenJava version the game uses.
     */
    public String getBuildVersion() {
        return buildVersion;
    }

    /**
     * <p>
     * The game {@link Window} is the main window which renders the scenes. The framework will automatically clear containers during the rendering process. Modifying the game window is not fully supported.
     * Please use the proper events to modify containers as modifications to the game window may have no effect.
     * @return The current window for the game.
     */
    public Window getGameWindow() {
        return gameWindow;
    }

    public void setGameWindow(Window gameWindow) {
        this.gameWindow = gameWindow;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public void setConfiguration(RenJavaConfiguration config) {
        CONFIGURATION = config;
    }

    public void setSettings(SettingsProperties settings) {
         SETTINGS = settings;
     }

    /**
     * @return The systems host services such as web browser.
     */
    public HostServices getHost() {
         return hostServices;
     }

    public void setHost(HostServices services) {
         this.hostServices = services;
     }

     public Collection<File> getSaves() {
         return new LinkedHashSet<>(Arrays.asList(new File(System.getProperty("user.dir") + "/game/saves/").listFiles()));
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
        if (!registeredCharacters.containsKey(id)) {
            InvalidCharacterException characterException = new InvalidCharacterException(id);
            RenLogger.LOGGER.error("Could not retrieve character '" + id + "'!", characterException);
            writeStackTrace(characterException);
            return null;
        }
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
     *         // Registers data to be included in the save file.
     *         MyDataClass dataClass = new MyDataClass();
     *         registerData(dataClass);
     *     }
     * }</pre>
     */
    public abstract void createBaseData();

    public abstract Window buildSplashScreen();

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
     *     Using the {@link Player} object, call the first story (aka event) of the game.
     * </p>
     *
     * Example usage:
     * <pre>{@code
     *  @Override
     *  public void start() {
     *     this.getPlayer().startStory("my-story");
     *  }
     *
     * }</pre>
     */
    public abstract void start();

    /* All static methods below */

    public static RenJava getInstance() {
        return instance;
    }

    public static EventHandler getEventHandler() {
        return EVENTHANDLER;
    }

    /**
     * Opens the provided link in the players default browser.
     * <p>
     *     Example Usage:
     * <p>
     * <pre>{@code
     *  openLink("https://www.google.com");
     * }</pre>
     * </p>
     *
     * @param url Full url link.
     */
    public static void openLink(String url) {
        getInstance().getHost().showDocument(url);
    }

    public static void writeStackTrace(Exception e) {
        if (lastErrorTimeStamp > 0) {
            spamTrack++;
            if (spamTrack > 5) {
                Date last = new Date(lastErrorTimeStamp);
                Date current = new Date(System.currentTimeMillis());
                long seconds = (current.getTime() - last.getTime()) / DateUtils.MILLIS_PER_SECOND;
                if (seconds < 30) {
                    RenLogger.LOGGER.error("An error occurred within 30s of a previous error. To prevent spam this error has been silenced.");
                    return;
                }
            }
        } else {
            spamTrack = 0;
        }

        lastErrorTimeStamp = System.currentTimeMillis();

        File file = new File(System.getProperty("user.dir") + "/stacktrace.txt");
        try {
            file.createNewFile();
        } catch (IOException ex) {
            RenLogger.LOGGER.error("Could not create error file!", ex);
            RenJava.writeStackTrace(ex);
            return;
        }

        try {
            PrintStream printStream = new PrintStream(file);
            e.printStackTrace(printStream);
        } catch (FileNotFoundException ex) {
            RenLogger.LOGGER.error("Could not write error file!", ex);
            RenJava.writeStackTrace(e);
        }

        if (errorWindow == null) {
            errorWindow = new Window("Error", StageStyle.DECORATED, CONFIGURATION.getGameIcon(), 920, 650, false);
        } else {
            errorWindow.clearContainers();
        }
        errorWindow.build(true);

        errorWindow.setFullscreen(false);
        errorWindow.setMaximized(false);
        errorWindow.updateBackground(Color.WHITE);

        VerticalLayout rootLayout = new VerticalLayout(900, 600);
        ScrollContainer container = new ScrollContainer(rootLayout,0, 0, 900, 600);

        TextOverlay text = new TextOverlay("An error has occurred during the application. A stacktrace file has been created. Please send the file and current log to the author. You can close this window to continue but the game may be unstable.");


        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        e.printStackTrace(writer);

        TextOverlay stackTrace = new TextOverlay(sw.toString());
        LinkedList<Overlay> texts = new LinkedList<>();
        texts.add(text);
        texts.add(stackTrace);

        TextFlowOverlay textFlowOverlay = new TextFlowOverlay(texts, 900, 600);
        rootLayout.addOverlay(textFlowOverlay);

        container.addLayout(rootLayout);

        errorWindow.addContainers(container);

        errorWindow.render();
    }
}