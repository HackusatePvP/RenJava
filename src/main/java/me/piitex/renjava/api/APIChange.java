package me.piitex.renjava.api;

/**
 * This annotation is used to label and describe API changes for future RenJava updates.
 * It provides information about the version in which the change was made and a description of the change.
 * This helps developers understand the impact of the change and make necessary adjustments to their code.
 */
public @interface APIChange {
    String changedVersion();
    String description();
}