package me.piitex.renjava.addons;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.RenLoggerFormat;
import me.piitex.renjava.events.EventListener;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

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
 *     @literal @Override
 *     public void onLoad() {
 *         // Code to execute when the addon is loaded
 *     }
 *
 *     @literal @Override
 *     public void onDisable() {
 *         // Code to execute when the addon is disabled
 *     }
 * }
 * }</pre>
 */
public abstract class Addon {
    private final String name;
    private final Collection<EventListener> registeredListeners = new HashSet<>();
    private final Collection<Addon> dependencies = new HashSet<>();
    private final Logger logger = RenJava.getInstance().getLogger();

    /**
     * Constructs a new Addon with the specified name.
     *
     * @param name The name of the addon.
     */
    public Addon(String name) {
        this.name = name;
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
     * Returns the collection of dependencies for the addon.
     * Dependencies are other addons that this addon relies on.
     *
     * @return The collection of dependencies for the addon.
     */
    public Collection<Addon> getDependencies() {
        return dependencies;
    }
}