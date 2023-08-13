package me.piitex.renjava.api.saves;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import java.util.logging.Logger;

/**
 * Represents a save slot. All data is stored via a .dat file.
 */
public class Save {

    /**
     * Creates a current save for the desired slot.
     * @param slot Slot to be saved, there is no limit on slots.
     * @param currentSceneImage The current image of the scene, this is used as a preview for the current save.
     */
    public Save(int slot) {
        // Slot is needed for the save slot.
        File directory = new File(System.getProperty("user.dir") + "/game/saves/" + slot + "/");
        directory.mkdir();
        File file = new File(directory,"/save-" + slot + ".dat");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        StringBuilder appendString = new StringBuilder();
        Logger logger = RenJava.getInstance().getLogger();
        // String data = "classname@field!value1@field2!value;clasname2@field!value@field!value@field!value";

        for (PersistentData data : RenJava.getInstance().getRegisteredData()) {
            Class<?> claz = data.getClass();
            appendString.append(claz.getName());
            for (Field field : claz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Data.class)) {
                    appendString.append("@");
                    appendString.append(field.getName());
                    appendString.append("!");
                    field.setAccessible(true);
                    try {
                        Object object = field.get(data);
                        appendString.append(object.toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            appendString.append(";");
        }

        // The appendString won't be sutiable for the save file. We can still use it to format our data but its best if we do a file format. YAML is a good example
        String toWriteToFile = SaveFileUtils.toFormat(appendString.toString());
        logger.info("File: " + toWriteToFile);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(toWriteToFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
