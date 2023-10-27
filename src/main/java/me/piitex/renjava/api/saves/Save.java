package me.piitex.renjava.api.saves;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a save slot. All data is stored via a .dat file.
 */
public class Save {
    private final Story story;
    private final RenScene scene;

    /**
     * Creates a current save for the desired slot.
     * @param slot Slot to be saved, there is no limit on slots.
     */
    public Save(int slot, String storyID, String sceneID) {
        this.story = RenJava.getInstance().getPlayer().getStory(storyID);
        this.scene = story.getScene(sceneID);
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
        appendString.append("configuration").append("@@@@").append("storyID").append("!!!!").append(storyID).append("@@@@").append("sceneID").append("!!!!").append(sceneID).append(";;;;");
        for (PersistentData data : RenJava.getInstance().getRegisteredData()) {
            Class<?> claz = data.getClass();
            appendString.append(claz.getName());
            List<Field> fields = new ArrayList<>(List.of(claz.getDeclaredFields()));
            fields.addAll(List.of(claz.getFields()));
            for (Field field : fields) {
                if (field.isAnnotationPresent(Data.class)) {
                    appendString.append("@@@@");
                    appendString.append(field.getName());
                    appendString.append("!!!!");
                    field.setAccessible(true);
                    try {
                        Object object = field.get(data);
                        appendString.append(object.toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            appendString.append(";;;;");
        }
        // The appendString won't be suitable for the save file. We can still use it to format our data but its best if we do a file format. YAML is a good example
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

    public Story getStory() {
        return story;
    }

    public RenScene getScene() {
        return scene;
    }
}