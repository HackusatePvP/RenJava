package me.piitex.renjava;

import javafx.stage.Stage;
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
import me.piitex.renjava.music.Track;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.*;

/**
 * Entry for framework. Create your own class and extend this to start.
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
        return "1.0.0.110";
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

    public void registerData(PersistentData data) {
        registeredData.add(data);
    }

    public Collection<PersistentData> getRegisteredData() {
        return registeredData;
    }

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

    public abstract MainTitleScreenView buildTitleScreen();

    public abstract void createStory();

    public abstract void start();

    public static RenJava getInstance() {
        return instance;
    }

    /* All static methods below */

    /**
     * Invokes all {@link Listener} methods for the given {@link Event}
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
            try {
                method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), event);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        for (Method method : normalMethods) {
            try {
                method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), event);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        for (Method method : lowMethods) {
            try {
                method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), event);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}