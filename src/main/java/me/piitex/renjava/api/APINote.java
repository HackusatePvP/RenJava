package me.piitex.renjava.api;

/**
 * This annotation is used to provide additional notes or comments about an API feature or function.
 * It serves as a temporary documentation placeholder for changes that are being tested or ideas that are being explored.
 * If the feature or function is finalized and stays in the codebase, these notes will be converted into proper Java documentation.
 */
public @interface APINote {
    String description();
}
