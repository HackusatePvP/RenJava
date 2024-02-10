package me.piitex.renjava.api.saves;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.saves.exceptions.SaveFileNotFound;
import me.piitex.renjava.api.saves.file.SectionKeyValue;
import me.piitex.renjava.api.stories.Story;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Loads a save file.
 */
public class Load {

    public Load(int slot) throws SaveFileNotFound {
        File directory = new File(System.getProperty("user.dir") + "/game/saves/" + slot + "/");
        File file = new File(directory,"/save-" + slot + ".dat");
        if (!file.exists()) {
            throw new SaveFileNotFound();
        }

        // Mapping
        Collection<SectionKeyValue> keyValues = new HashSet<>();

        // Story and Scene
        String story = "";
        String scene = "";

        // Music stuff
        String currentTrack = "";
        boolean isPlaying = false;
        boolean isLooping = false;

        Logger logger = RenJava.getInstance().getLogger();
        try {
            Scanner scanner = new Scanner(file);
            SectionKeyValue sectionKeyValue = null;

            // Flags
            boolean config = false;
            boolean musicTracker = false;

            while (scanner.hasNextLine()) {
                // classname@field1;value@field2;value@ect..@ect..;clasname2@field;value@field;value@field;value
                String data = scanner.nextLine();

                if (data.startsWith("configuration")) {
                    config = true;
                } else if (data.contains("me.piitex.renjava.api.music.Tracks")) {
                    musicTracker = true;
                } else {
                    if (!data.startsWith("   ")) {
                        data = data.trim();
                        if (sectionKeyValue != null) {
                            keyValues.add(sectionKeyValue);
                        }
                        sectionKeyValue = new SectionKeyValue(data.replace(":", "").trim());
                    } else if (sectionKeyValue != null) {
                        String key = data.split(": ")[0].trim();
                        String value = data.split(": ")[1].trim();
                        sectionKeyValue.addKeyValue(key, value);
                    }
                }

                if (data.startsWith("    ")) {
                    if (config) {
                        data = data.trim();
                        String[] pairs = data.split(": ");
                        String key = pairs[0];
                        String value = pairs[1];
                        if (key.equalsIgnoreCase("storyID")) {
                            story = value;
                        }
                        if (key.equalsIgnoreCase("sceneID")) {
                            logger.info("Key: " + key);
                            logger.info("Value: " + value);
                            scene = value;
                            // End config
                        }
                        if (!story.isEmpty() && !scene.isEmpty()) {
                            logger.info("Story: " + story);
                            logger.info("Scene: " + scene);
                            config = false;
                        }
                    }
                    if (musicTracker) {
                        data = data.trim();
                        String[] pairs = data.split(": ");
                        String key = pairs[0];
                        String value = pairs[1];

                        if (key.equalsIgnoreCase("currentTrack")) {
                            currentTrack = value;
                        }

                        if (key.equalsIgnoreCase("isPlaying")) {
                            isPlaying = Boolean.parseBoolean(value);
                        }

                        if (key.equalsIgnoreCase("loop")) {
                            isLooping = Boolean.parseBoolean(value);
                        }
                    }
                }
            }
            // Once the loop is done add the final section
            keyValues.add(sectionKeyValue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (PersistentData data : RenJava.getInstance().getRegisteredData()) {
            // Loop through data and check if it matches any of the section key data
            logger.info("Class: " + data.getClass().getName());
            for (SectionKeyValue sectionKeyValue : keyValues) {
                if (sectionKeyValue.getSection().equalsIgnoreCase(data.getClass().getName())) {
                    sectionKeyValue.getKeyValueMap().forEach((string, string2) -> {
                        logger.info("Setting fields...");

                        try {
                            Field field = data.getClass().getField(string);
                            setField(field, data, string2);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (NoSuchFieldException e) {
                            // If its no such field see if its a declared field
                            try {
                                Field field = data.getClass().getDeclaredField(string);
                                setField(field, data, string2);
                            } catch (IllegalAccessException | NoSuchFieldException ignored) {
                                // This is ignored because it will throw NoSuchFieldException as it loops through ALL saved data in the file.
                            }
                        }
                    });
                }

                keyValues.remove(sectionKeyValue); // this might error
            }
        }
        Story loadedStory = RenJava.getInstance().getPlayer().getStory(story);
        loadedStory.init(); // Add scenes and stuff
        loadedStory.displayScene(scene);
        if (isPlaying) {
            RenJava.getInstance().getTracks().play(currentTrack, isLooping);
        }
    }

    private void setField(Field field, PersistentData data, String string2) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        Type type = field.getGenericType();
        if (type.getTypeName().toLowerCase().contains("string")) {
            field.set(data, string2);
        } else if (type.getTypeName().toLowerCase().contains("int")) {
            field.set(data, Integer.parseInt(string2));
        } else if (type.getTypeName().toLowerCase().contains("boolean")) {
            field.set(data, Boolean.parseBoolean(string2));
        } else if (type.getTypeName().toLowerCase().contains("double")) {
            field.set(data, Double.parseDouble(string2));
        } else if (type.getTypeName().toLowerCase().contains("float")) {
            field.set(data, Float.parseFloat(string2));
        } else if (type.getTypeName().toLowerCase().contains("long")) {
            field.set(data, Long.parseLong(string2));
        } else if (type.getTypeName().toLowerCase().contains("short")) {
            field.set(data, Short.parseShort(string2));
        } else if (type.getTypeName().toLowerCase().contains("byte")) {
            field.set(data, Byte.parseByte(string2));
        }
    }
}