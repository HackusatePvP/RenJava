package me.piitex.renjava;

import javafx.application.HostServices;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import me.piitex.renjava.addons.Addon;
import me.piitex.renjava.addons.AddonLoader;
import me.piitex.renjava.api.Game;
import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.api.exceptions.InvalidCharacterException;
import me.piitex.renjava.api.music.Tracks;
import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.configuration.Configuration;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.configuration.SettingsProperties;
import me.piitex.renjava.events.Event;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.defaults.*;

import me.piitex.renjava.events.types.SideMenuBuildEvent;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.DisplayOrder;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.containers.ScrollContainer;
import me.piitex.renjava.gui.layouts.HorizontalLayout;
import me.piitex.renjava.gui.layouts.VerticalLayout;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.overlays.*;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.tasks.Tasks;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    // Audio Tracking
    public static Tracks TRACKS;

    public static AddonLoader ADDONLOADER;

    private Window gameWindow;

    public static RenJavaConfiguration CONFIGURATION;

    // User settings
    private SettingsProperties settings;

    private HostServices hostServices;

    private Logger logger;

    private final Map<String, Character> registeredCharacters = new HashMap<>();
    private final Collection<EventListener> registeredListeners = new HashSet<>();
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

        // Initializes the Ren logger which is separated from the application logger.
        RenLogger.init();

        this.registerListener(new MenuClickEventListener());
        this.registerListener(new GameFlowEventListener());
        this.registerListener(new StoryHandlerEventListener());
        this.registerListener(new ScenesEventListener());
        this.registerListener(new OverlayEventListener());
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

    public Window getGameWindow() {
        return gameWindow;
    }

    public void setGameWindow(Window gameWindow) {
        this.gameWindow = gameWindow;
    }

    public RenJavaConfiguration getConfiguration() {
        return CONFIGURATION;
    }

    public void setConfiguration(RenJavaConfiguration configuration) {
        this.configuration = configuration;
        CONFIGURATION = config;
    }

    public SettingsProperties getSettings() {
         return settings;
     }

    public void setSettings(SettingsProperties settings) {
         this.settings = settings;
     }

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
     * Registers an {@link EventListener} to handle game events.
     * <p>
     * This method is used to register an {@link EventListener} that handles game events.
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
     *         // Registers data to be included in the save file.
     *         MyDataClass dataClass = new MyDataClass();
     *         registerData(dataClass);
     *     }
     * }</pre>
     */
    public abstract void createBaseData();

    public abstract Window buildSplashScreen();

    /**
     * This function is used to create the main menu screen.
     *
     * <p>
     * The main menu is rendered to the game {@link Window} which is managed by the framework. This function is used to make the base {@link  Container} which is then added to the window.
     * <p>
     *     Note, the side menu will be automatically rendered and does not be to be added to this container.
     * </p>
     * </p>
     * <p>
     * <pre>
     *     {@code
     *     public void Container buildMainMenu(boolean rightClick) {
     *         Container container = new EmptyContainer(1920, 1080) // Width and height of the container
     *
     *         // Add overlays to the main menu
     *         ImageOverlay backgroundImage = new ImageOverlay("gui/main_menu.png");
     *         backgroundImage.setOrder(DisplayOrder.LOw); // Sends the background image to the back of the container.
     *         container.addOverlay(backgroundImage);
     *
     *         return container;
     *     }
     *     }
     * </pre>
     * </p>
     * @see Container
     * @see Window
     * @see EmptyContainer
     * @param rightClick If it is the right-clicked main menu.
     * @return Container to be used for the main menu.
     */
    public abstract Container buildMainMenu(boolean rightClick);

    public Container buildSideMenu(boolean rightClick) {
        Container menu = new EmptyContainer(1920, 1080, DisplayOrder.HIGH);

        ImageOverlay imageOverlay = new ImageOverlay("gui/overlay/main_menu.png");
        imageOverlay.setOrder(DisplayOrder.LOW);
        menu.addOverlay(imageOverlay);

        FontLoader uiFont = RenJava.getInstance().getConfiguration().getUiFont();

        Color hoverColor = getConfiguration().getHoverColor();

        ButtonOverlay startButton = new ButtonOverlay("menu-start-button", "Start", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        ButtonOverlay loadButton = new ButtonOverlay("menu-load-button", "Load", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        ButtonOverlay saveButton = new ButtonOverlay("menu-save-button", "Save", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        ButtonOverlay optionsButton = new ButtonOverlay("menu-preference-button", "Preferences", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        ButtonOverlay aboutButton = new ButtonOverlay("menu-about-button", "About", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        // Create vbox for the buttons. You can also do an HBox
        VerticalLayout layout = new VerticalLayout(400, 500);
        layout.setOrder(DisplayOrder.HIGH);
        layout.setX(50);
        layout.setY(250);
        layout.setSpacing(20);
        layout.addOverlays(startButton, loadButton);
        if (rightClick) {
            layout.addOverlays(saveButton);
        }
        layout.addOverlays(optionsButton, aboutButton);

        // You don't have to add the button overlays just add the layout which already contains the overlays.
        menu.addLayout(layout);

        ButtonOverlay returnButton;

        if (PLAYER.getCurrentStageType() == StageType.MAIN_MENU) {
            returnButton = new ButtonOverlay("menu-quit-button", "Quit", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        } else {
            returnButton = new ButtonOverlay("menu-return-button", "Return", Color.BLACK, uiFont, Color.TRANSPARENT, Color.TRANSPARENT, hoverColor);
        }
        returnButton.setX(25);
        returnButton.setY(980);
        menu.addOverlay(returnButton);

        SideMenuBuildEvent sideMenuBuildEvent = new SideMenuBuildEvent(menu);
        RenJava.callEvent(sideMenuBuildEvent);

        return menu;
    }

    public Container buildLoadMenu(int page) {
        Container menu = new EmptyContainer(getConfiguration().getWidth(), CONFIGURATION.getHeight(), DisplayOrder.NORMAL);
        ImageOverlay imageOverlay = new ImageOverlay("gui/main_menu.png");
        imageOverlay.setOrder(DisplayOrder.LOW);
        menu.addOverlay(imageOverlay);
        // Setup pagination.
        // 6 save slots per page
        //   2 Rows
        //   3 Columns
        // Make this customizable

        int maxSavesPerPage = 6;

        int index = ((maxSavesPerPage * page) - maxSavesPerPage) + 1;
        VerticalLayout rootLayout = new VerticalLayout(1000, 800); // The root is a vertical which stacks the two horizontal layouts.
        rootLayout.setSpacing(10);
        HorizontalLayout topLayout = new HorizontalLayout(1000, 350);
        topLayout.setSpacing(20);
        HorizontalLayout bottomLayout = new HorizontalLayout(1000, 350);
        bottomLayout.setSpacing(20);
        while (index <=  maxSavesPerPage) {
            ButtonOverlay loadButton = getButtonOverlay(page, index);
            loadButton.setOrder(DisplayOrder.HIGH);

            if (index <= 3) {
                topLayout.addOverlays(loadButton);
            } else {
                bottomLayout.addOverlays(loadButton);
            }
            index++;
        }

        rootLayout.addChildLayout(topLayout);
        rootLayout.addChildLayout(bottomLayout);

        rootLayout.setX(500);
        rootLayout.setY(250);


        menu.addLayout(rootLayout);

        // Add Page buttons below.
        // There should be 8 per view.
        int pageViewMax = 8;
        int pageIndex = 0;
        HorizontalLayout pageLayout = new HorizontalLayout(100, 100);
        while (pageIndex < pageViewMax) {
            pageIndex++;
            ButtonOverlay pageButton = new ButtonOverlay("page-" + pageIndex, pageIndex + "", Color.BLACK, new FontLoader(getConfiguration().getUiFont(), 26), 1, 1);
            pageButton.setBackgroundColor(Color.TRANSPARENT);
            pageButton.setBorderColor(Color.TRANSPARENT);
            if (page == pageIndex) {
                pageButton.setTextFill(Color.BLACK);
            }
            pageButton.setHoverColor(CONFIGURATION.getHoverColor());
            pageLayout.addOverlay(pageButton);
        }
        pageLayout.setX(1000);
        pageLayout.setY(950);

        menu.addLayout(pageLayout);

        return menu;
    }

    @NotNull
    private ButtonOverlay getButtonOverlay(int page, int index) {
        Save save = new Save(index);
        save.load(false);
        ImageOverlay saveImage;
        ButtonOverlay loadButton;
        saveImage = save.buildPreview(page);

        loadButton = new ButtonOverlay("save-" + index, saveImage, 0, 0, 414, 309);

        loadButton.onClick(event -> {
            if (PLAYER.getCurrentStageType() == StageType.LOAD_MENU) {
                if (!save.exists()) {
                    RenLogger.LOGGER.warn("Save file does not  0-exist.");
                    return;
                }
                save.load(true);
                String storyID = PLAYER.getCurrentStoryID();
                if (storyID == null) {
                    RenLogger.LOGGER.error("Save file could not be loaded. The data is either not formatted or corrupted.");
                    return;
                }
                RenLogger.LOGGER.info("Processing save file...");

                RenLogger.LOGGER.debug("Reloading story...");
                createStory();

                // Force update fields
                RenLogger.LOGGER.debug("Setting current story: " + storyID);
                getPlayer().setCurrentStory(storyID);
                RenLogger.LOGGER.debug("Initializing story...");
                getPlayer().getCurrentStory().init(); // Re-initialize story

                RenLogger.LOGGER.debug("Setting current scene: " + PLAYER.getCurrentSceneID());
                PLAYER.setCurrentScene(PLAYER.getCurrentSceneID());

                PLAYER.setRightClickMenu(false); // When the save is loaded it will render the scene leaving the main menu.

                RenLogger.LOGGER.debug("Rendering scene...");
                RenScene scene = PLAYER.getCurrentScene();
                PLAYER.getStory(storyID).displayScene(scene);

            } else if (PLAYER.getCurrentStageType() == StageType.SAVE_MENU) {
                Tasks.runAsync(() -> {
                    save.write();
                });

                // Re-render
                PLAYER.setCurrentStageType(StageType.SAVE_MENU);
                Container menu = buildLoadMenu(1); // Builds first page
                menu.addContainers(buildSideMenu(true));
                getGameWindow().clearContainers();
                getGameWindow().addContainer(menu);
                getGameWindow().render();
            }
        });

        loadButton.setBackgroundColor(Color.TRANSPARENT);
        loadButton.setBorderColor(Color.TRANSPARENT);
        return loadButton;
    }

    public Container buildSettingsMenu(boolean ui) {
        Container menu = new EmptyContainer(getConfiguration().getWidth(), getConfiguration().getHeight());
        ImageOverlay imageOverlay = new ImageOverlay("gui/main_menu.png");
        imageOverlay.setOrder(DisplayOrder.LOW);
        menu.addOverlay(imageOverlay);

        Color themeColor = getConfiguration().getThemeColor();
        Color subColor = getConfiguration().getSubColor();

        // 1 hbox 3 vboxes

        // Display    Rollback     Skip
        // Windowed     Disabled   Unseen Text
        // Full screen  Enabled    After Choices
        //              Right      Transitions


        HorizontalLayout rootLayout = new HorizontalLayout(1200, 600);
        rootLayout.setX(580);
        rootLayout.setY(100);
        rootLayout.setSpacing(100);

        VerticalLayout displayBox = new VerticalLayout(300, 400);
        TextOverlay displayText = new TextOverlay("Display", themeColor, getConfiguration().getUiFont(), 0, 0);
        ButtonOverlay windowButton = new ButtonOverlay("windowed-display", "Windowed", subColor, getConfiguration().getUiFont(), 0,0);
        ButtonOverlay fullscreenButton = new ButtonOverlay("windowed-fullscreen", "Fullscreen", subColor, getConfiguration().getUiFont(), 0,0);
        displayBox.addOverlays(displayText, windowButton, fullscreenButton);

        VerticalLayout rollbackBox = new VerticalLayout(300, 400);
        TextOverlay rollbackText = new TextOverlay("Rollback", themeColor, getConfiguration().getUiFont(), 0, 0);
        ButtonOverlay disabledButton = new ButtonOverlay("disabled-rollback", "Disabled", subColor, getConfiguration().getUiFont(), 0,0);
        ButtonOverlay leftButton = new ButtonOverlay("left-rollback", "Left", subColor, getConfiguration().getUiFont(), 0,0);
        ButtonOverlay rightButton = new ButtonOverlay("right-rollback", "Right", subColor, getConfiguration().getUiFont(), 0,0);
        rollbackBox.addOverlays(rollbackText, disabledButton, leftButton, rightButton);

        VerticalLayout skipBox = new VerticalLayout(300, 400);
        TextOverlay skipText = new TextOverlay("Skip", themeColor, getConfiguration().getUiFont(), 0, 0);
        ButtonOverlay unseenTextButton = new ButtonOverlay("unseen-skip", "Unseen Text", subColor, getConfiguration().getUiFont(), 0,0);
        ButtonOverlay afterChoicesButton = new ButtonOverlay("after-skip", "After Choices", subColor, getConfiguration().getUiFont(), 0,0);
        ButtonOverlay transitionButton = new ButtonOverlay("transitions-skip", "Transitions", subColor, getConfiguration().getUiFont(), 0,0);
        skipBox.addOverlays(skipText, unseenTextButton, afterChoicesButton, transitionButton);

        // Add all to root layout
        rootLayout.addChildLayouts(displayBox, rollbackBox, skipBox);

        menu.addLayout(rootLayout);

        // Music sliders
        VerticalLayout soundRoot = new VerticalLayout(1000, 1000);
        soundRoot.setX(1150);
        soundRoot.setY(400);
        soundRoot.setSpacing(30);

        VerticalLayout masterBox = new VerticalLayout(1000, 100);
        TextOverlay masterVolumeText = new TextOverlay("Master Volume", themeColor, getConfiguration().getUiFont(), 0, 0);
        SliderOverlay masterVolumeSlider = new SliderOverlay(0, 100, getSettings().getMasterVolume(), 0,0, 200, 200);
        masterVolumeSlider.onSliderMove(event -> {
            getSettings().setMasterVolume(event.getValue());
        });
        masterBox.addOverlays(masterVolumeText, masterVolumeSlider);

        VerticalLayout musicBox = new VerticalLayout(1000, 100);
        TextOverlay musicVolumeText = new TextOverlay("Music Volume", themeColor, getConfiguration().getUiFont(), 0, 0);
        SliderOverlay musicVolumeSlider = new SliderOverlay(0, 100, getSettings().getMusicVolume(), 0,0, 200, 200);
        musicVolumeSlider.onSliderMove(event -> {
            getSettings().setMusicVolume(event.getValue());
        });
        musicBox.addOverlays(musicVolumeText, musicVolumeSlider);

        VerticalLayout soundBox = new VerticalLayout(1000, 100);
        TextOverlay soundVolumeText = new TextOverlay("Sound Volume", themeColor, getConfiguration().getUiFont(), 0, 0);
        SliderOverlay soundVolumeSlider = new SliderOverlay(0, 100, getSettings().getSoundVolume(), 0,0, 200, 200);
        soundVolumeSlider.onSliderMove(event -> {
            getSettings().setSoundVolume(event.getValue());
        });
        soundBox.addOverlays(soundVolumeText, soundVolumeSlider);

        VerticalLayout voiceBox = new VerticalLayout(1000, 100);
        TextOverlay voiceVolumeText = new TextOverlay("Voice Volume", themeColor, getConfiguration().getUiFont(), 0, 0);
        SliderOverlay voiceVolumeSlider = new SliderOverlay(0, 100, getSettings().getVoiceVolume(), 0,0, 200, 200);
        voiceVolumeSlider.onSliderMove(event -> {
            getSettings().setVoiceVolume(event.getValue());
        });
        voiceBox.addOverlays(voiceVolumeText, voiceVolumeSlider);


        soundRoot.addChildLayouts(masterBox, musicBox, soundBox, voiceBox);

        menu.addLayout(soundRoot);

        return menu;
    }


    // I do not recommend changing or overriding this method. The licenses must be included for some of the software used.
    // This method is public just in case you implement other libraries that need their licenses displayed.
    public Container buildAboutMenu(boolean rightClicked) {
        Container menu = new EmptyContainer(getConfiguration().getWidth(), getConfiguration().getHeight());
        TextOverlay spacer = new TextOverlay("\n");
        menu.addOverlay(new ImageOverlay("gui/main_menu.png"));

        FontLoader font = new FontLoader(getConfiguration().getDefaultFont().getFont(), 24);
        TextFlowOverlay aboutText = new TextFlowOverlay("", 1300, 500);
        aboutText.add(new HyperLinkOverlay("https://github.com/HackusatePvP/RenJava", "RenJava"));
        aboutText.add(new TextOverlay(" is a VN framework inspired by RenPy and built with JavaFX. Thank you to those respective communities for their contributions. RenJava is free and open sourced licensed under GPL 3.0 for free use."));
        aboutText.setFont(font);
        aboutText.setX(500);
        aboutText.setY(150);
        menu.addOverlay(aboutText);

        // If you modify the about you must include the license information.
        TextFlowOverlay licenseText = new TextFlowOverlay("License Information. Some of the licenses below are required to be disclosed. Modify the below only to include additional required licenses.", 1300, 700);
        licenseText.setX(500);
        licenseText.setY(250);
        licenseText.setFont(new FontLoader(font, 20));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tRenJava is licensed under GNU GPL v3: "));
        licenseText.add(new HyperLinkOverlay("https://www.gnu.org/licenses/gpl-3.0.en.html"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tRenPy is licensed under MIT: "));
        licenseText.add(new HyperLinkOverlay("https://www.renpy.org/doc/html/license.html"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tJavaFX is licensed under GPL-2.0: "));
        licenseText.add(new HyperLinkOverlay("https://github.com/openjdk/jfx/blob/master/LICENSE"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tAmazon Corretto is licensed under GPL-2.0 CE: "));
        licenseText.add(new HyperLinkOverlay("https://openjdk.java.net/legal/gplv2+ce.html"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tApache software is licensed under Apache 2.0: "));
        licenseText.add(new HyperLinkOverlay("http://www.apache.org/licenses/"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tJetBrains is licensed under Apache 2.0: "));
        licenseText.add(new HyperLinkOverlay("http://www.apache.org/licenses/"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tWebP ImageIO is licensed under Apache 2.0: "));
        licenseText.add(new HyperLinkOverlay("http://www.apache.org/licenses/"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tGoogleCode SoundLibs is licensed under LGPL 2.1: "));
        licenseText.add(new HyperLinkOverlay("https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html"));
        licenseText.add(spacer);
        licenseText.add(new TextOverlay("\tOshi is licensed under MIT: "));
        licenseText.add(new HyperLinkOverlay("https://github.com/oshi/oshi/blob/master/LICENSE"));

        menu.addOverlay(licenseText);


        TextFlowOverlay buildInfo = new TextFlowOverlay("Just because the software this game uses may be free for use does not mean you have any right to re-distribute. " +
                "The author holds and retrains rights to distribute and sell their game as they wish. " +
                "Consumers hold no rights when re-distributing or publishing this game without explicit permission from the author. " +
                "Consumers also have no ownership over this digital product and must follow the terms provided by the author. " +
                "If no terms were provided then you must use the following. You are not allowed to re-sell or re-distribute this game. " +
                "If you paid for this game you did not pay for ownership but rather permission to use this game. This permission can be revoked at any time for any reason." +
                "These terms also apply to any addons that may come with the game.",1300, 800);
        buildInfo.setX(500);
        buildInfo.setY(600);
        buildInfo.setFont(new FontLoader(font, 20));
        buildInfo.add(spacer);
        buildInfo.add(spacer);
        buildInfo.add(new TextOverlay("RenJava Version: " + getBuildVersion()));
        buildInfo.add(spacer);
        buildInfo.add(new TextOverlay("Game Version: " + getVersion()));
        buildInfo.add(spacer);
        buildInfo.add(new TextOverlay("Author: " + getAuthor()));
        buildInfo.add(spacer);

        menu.addOverlay(buildInfo);

        return menu;
    }

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
        Map<EventListener, Method> lowestMethods = new HashMap<>();
        Map<EventListener, Method> lowMethods = new HashMap<>();
        Map<EventListener, Method> normalMethods = new HashMap<>();
        Map<EventListener, Method> highMethods = new HashMap<>();
        Map<EventListener, Method> highestMethods = new HashMap<>();

        Collection<EventListener> eventListeners = new HashSet<>(getInstance().getRegisteredListeners());
        for (Addon addon : ADDONLOADER.getAddons()) {
            eventListeners.addAll(addon.getRegisteredListeners());
        }

        for (EventListener listener : eventListeners) {
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
                            case HIGHEST -> highestMethods.put(listener, method);
                            case HIGH -> highMethods.put(listener,method);
                            case NORMAL -> normalMethods.put(listener,method);
                            case LOW -> lowMethods.put(listener,method);
                            case LOWEST -> lowestMethods.put(listener,method);
                        }
                    }
                }
            }
        }

        highestMethods.forEach((listener, method) -> {
            invokeMethod(listener, method, event);
        });
        highMethods.forEach((listener, method) -> {
            invokeMethod(listener, method, event);
        });
        normalMethods.forEach((listener, method) -> {
            invokeMethod(listener, method, event);
        });
        lowMethods.forEach((listener, method) -> {
            invokeMethod(listener, method, event);
        });
        lowestMethods.forEach((listener, method) -> {
            invokeMethod(listener, method, event);
        });

        // Clear resource usage
        lowestMethods.clear();
        lowMethods.clear();
        normalMethods.clear();
        highMethods.clear();
        highestMethods.clear();
        eventListeners.clear();
    }

    private static void invokeMethod(EventListener listener, Method method, Event event) {
        try {
            if (event.isSync()) {
                method.invoke(listener, event);
            } else {
                Tasks.runAsync(() -> {
                    try {
                        method.invoke(listener, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        RenLogger.LOGGER.error("Could not invoke event method for '" + method.getName() + "'", e);
                        writeStackTrace(e);
                    }
                });
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            RenLogger.LOGGER.error("Could not invoke event method for '" + method.getName() + "'", e);
            writeStackTrace(e);
        }
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

        Window errorWindow = new Window("Error", StageStyle.DECORATED, getInstance().getConfiguration().getGameIcon(), 920, 650, false);
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

        errorWindow.addContainers(container);

        errorWindow.render();

        // Clear resource usage
        texts.clear();

    }
}