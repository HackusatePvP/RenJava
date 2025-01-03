package me.piitex.renjava.events;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.addons.Addon;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.tasks.Tasks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EventHandler {
    private Event currentExecutedEvent;
    private final Collection<EventListener> registeredListeners = new HashSet<>();

    public Event getCurrentExecutedEvent() {
        return currentExecutedEvent;
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
    public void callEvent(Event event) {
        if (currentExecutedEvent != null) {
            currentExecutedEvent.getLinkedEvents().add(event);
        } else {
            currentExecutedEvent = event;
        }

        Collection<EventListener> eventListeners = new HashSet<>(registeredListeners);
        for (Addon addon : RenJava.ADDONLOADER.getAddons()) {
            eventListeners.addAll(addon.getRegisteredListeners());
        }

        invokeEvent(eventListeners, event);
        eventListeners.clear();
        currentExecutedEvent = null;
    }

    public void callEventChain(Event event, Event... children) {
        for (Event child : children) {
            event.getLinkedEvents().add(child);
            child.getLinkedEvents().add(event);
        }
        if (currentExecutedEvent != null) {
            currentExecutedEvent.getLinkedEvents().add(event);
        } else {
            currentExecutedEvent = event;
        }

        Collection<EventListener> eventListeners = new HashSet<>(registeredListeners);
        for (Addon addon : RenJava.ADDONLOADER.getAddons()) {
            eventListeners.addAll(addon.getRegisteredListeners());
        }

        for (Event child : children) {
            invokeEvent(eventListeners, child);
        }

        invokeEvent(eventListeners, event); // Handles parent last
        eventListeners.clear();
        currentExecutedEvent = null;
    }

    private void invokeEvent(Collection<EventListener> eventListeners, Event event) {
        Map<EventListener, Method> lowestMethods = new HashMap<>();
        Map<EventListener, Method> lowMethods = new HashMap<>();
        Map<EventListener, Method> normalMethods = new HashMap<>();
        Map<EventListener, Method> highMethods = new HashMap<>();
        Map<EventListener, Method> highestMethods = new HashMap<>();
        Map<EventListener, Method> chainedMethods = new HashMap<>();

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
                            case CHAINED -> chainedMethods.put(listener,method);
                        }
                    }
                }
            }
        }
        chainedMethods.forEach(((eventListener, method) -> {
            invokeMethod(eventListener, method, event);
        }));
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
    }

    private void invokeMethod(EventListener listener, Method method, Event event) {
        try {
            if (event.isSync()) {
                method.invoke(listener, event);
            } else {
                Tasks.runAsync(() -> {
                    try {
                        method.invoke(listener, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        RenLogger.LOGGER.error("Could not invoke event method for '" + method.getName() + "'", e);
                        RenJava.writeStackTrace(e);
                    }
                });
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            RenLogger.LOGGER.error("Could not invoke event method for '" + method.getName() + "'", e);
            RenJava.writeStackTrace(e);
        }
    }
}
