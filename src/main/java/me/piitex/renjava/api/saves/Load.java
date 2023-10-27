package me.piitex.renjava.api.saves;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.saves.exceptions.SaveFileNotFound;
import me.piitex.renjava.api.saves.file.SectionKeyValue;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;

import java.io.File;
import java.io.FileNotFoundException;
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

                // # Start of file
                // configuration:
                //    storyID: storyID
                //    sceneID: sceneID
                // me.piitex.test.TestClass
                //    fieldName: FieldValue
                // me.piitext.test.NextClass
                //    fieldName: FieldValue
                String data = scanner.nextLine();

                // Flags

                // Build configuration
                String currentStory = "";
                String currentScene = "";
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
                        sectionKeyValue = new SectionKeyValue(data.replace(": ", ""));
                    } else if (sectionKeyValue != null) {
                        String key = data.split(": ")[0];
                        String value = data.split(": ")[1];
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
            keyValues.stream().filter(sectionKeyValue -> data.getClass().getName().equalsIgnoreCase(sectionKeyValue.getSection())).forEach(sectionKeyValue -> {
                // Set the fields and values for the desired data
                sectionKeyValue.getKeyValueMap().forEach((s, s2) -> {
                    try {
                        data.getClass().getField(s).set(s2, data);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                });
            });
        }

        Story loadedStory = RenJava.getInstance().getPlayer().getStory(story);
        loadedStory.init(); // Add scenes and stuff
        for (String s : loadedStory.getScenes().keySet()) {
            logger.info("Map Key: " + s);
        }
        loadedStory.displayScene(scene);

        logger.info("Checking track info...");
        logger.info("Current Track: " + currentTrack);
        logger.info("Playing Track: " + isPlaying);
        logger.info("Looping: " + isLooping);

        if (isPlaying) {
            RenJava.getInstance().getTracks().play(currentTrack, isLooping);
        }
    }
}