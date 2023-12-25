package me.piitex.renjava.events;

/**
 * Priorities are used to order event executions. A high-event priority will make the event execute first while a low priority will be executed last.
 */
public enum Priority {
    HIGHEST,
    HIGH,
    NORMAL,
    LOW,
    LOWEST
}
