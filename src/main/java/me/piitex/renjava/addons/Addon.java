package me.piitex.renjava.addons;

import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.loggers.ApplicationLogger;
import me.piitex.renjava.events.EventListener;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;

/**
 * <p>
 * The Addon class represents an addon in the RenJava application.
 * Addons are third-party jar files created to provide additional content similar to DLCs.
 * Addons can access the entire RenJava framework, including features like PersistentData and Characters.
 * <p>
 * To create a new addon, create a jar file with the necessary classes and resources.
 * The addon jar file should be placed in the 'addons' directory of the RenJava application.
 * <p>
 * Example usage:
 * <pre>{@code
 * public class MyAddon extends Addon {
 *     public MyAddon() {
 *         super("MyAddon");
 *     }
 *
 *     @Override
 *     public void onLoad() {
 *         // Code to execute when the addon is loaded
 *     }
 *
 *     @Override
 *     public void onDisable() {
 *         // Code to execute when the addon is disabled
 *     }
 * }
 * }</pre>
 */
public abstract class Addon {
    private final String name;
    private final Collection<EventListener> registeredListeners = new HashSet<>();
    private final Collection<PersistentData> registeredData = new HashSet<>();
    private final Collection<Addon> dependencies = new HashSet<>();
    private final Logger logger;

    /**
     * Constructs a new Addon with the specified name.
     *
     * @param name The name of the addon.
     */
    public Addon(String name) {
        this.name = name;
        logger = new ApplicationLogger(name).LOGGER;
    }

    /**
     * This method is called when the addon is loaded.
     * Subclasses should implement this method to perform any necessary initialization.
     */
    public abstract void onLoad();

    /**
     * This method is called when the addon is disabled.
     * Subclasses should implement this method to clean up any resources or perform any necessary cleanup actions.
     */
    public abstract void onDisable();

    /**
     * Returns the name of the addon.
     *
     * @return The name of the addon.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the logger associated with the addon.
     *
     * @return The logger associated with the addon.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Registers an event listener for the addon.
     *
     * @param listener The event listener to register.
     */
    public void registerListener(EventListener listener) {
        registeredListeners.add(listener);
    }

    /**
     * Returns the collection of registered event listeners for the addon.
     *
     * @return The collection of registered event listeners.
     */
    public Collection<EventListener> getRegisteredListeners() {
        return registeredListeners;
    }


    /**
     * Registers data to be saved to the save file.
     *
     * @param data The data class to register.
     */
    public void registerData(PersistentData data) {
        registeredData.add(data);
    }

    /**
     * @return The collection of registered data for this addon.
     */
    public Collection<PersistentData> getRegisteredData() {
        return registeredData;
    }

    /**
     * Returns the collection of dependencies for the addon.
     * Dependencies are other addons that this addon relies on.
     *
     * @return The collection of dependencies for the addon.
     */
    public Collection<Addon> getDependencies() {
        return dependencies;
    }
}