package me.piitex.renjava.api.saves.data;

import me.piitex.renjava.RenJava;

/**
 * The {@code PersistentData} interface is used to organize classes with persistent data that can be stored into the save file.
 * <p>
 * Classes that implement the {@code PersistentData} interface can have their data saved and loaded using the saving feature of the framework.
 * Any data that needs to be saved should be contained in classes that implement this interface.
 * <p>
 * To enable the saving feature for a data class, it must be registered using the {@link RenJava#registerData(PersistentData)} method.
 *
 * @see RenJava#registerData(PersistentData)
 */
public interface PersistentData {

}
