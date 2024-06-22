package me.piitex.renjava.api.saves;

import javafx.scene.image.WritableImage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.addons.Addon;
import me.piitex.renjava.gui.overlay.ImageOverlay;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.saves.file.SectionKeyValue;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

// Class which represents a save file.
public class Save {
    private final File file;
    private int slot;

    // Some ghetto code
    // Map the scene when the load function is called. Easier way to get preview.
    private SectionKeyValue sceneSection;

    public Save(File file) {
        this.file = file;
    }

    public Save(int slot) {
        this.slot = slot;
        File directory = new File(System.getProperty("user.dir") + "/game/saves/");
        this.file = new File(directory,"save-" + slot + ".dat");
    }

    public boolean exists() {
        return file.exists();
    }

    // Writes save file
    public void write() {
        // First get all PersistentData to write.
        // me.piitex.MyDataClass:
        //    field1: data1
        //    map:
        //        key1: value1
        //        key2: value2

        StringBuilder appendString = new StringBuilder();
        Collection<PersistentData> allData = RenJava.getInstance().getRegisteredData();
        for (Addon addon : RenJava.getInstance().getAddonLoader().getAddons()) {
            allData.addAll(addon.getRegisteredData());
        }

        for (PersistentData data : allData) {
            SectionKeyValue rootSection = new SectionKeyValue(data.getClass().getName());
            Class<?> claz = data.getClass();
            if (appendString.toString().contains(claz.getName())) {
                continue;
            }
            List<Field> fields = new ArrayList<>(List.of(claz.getDeclaredFields()));
            fields.addAll(List.of(claz.getFields()));
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Data.class)) {
                    SectionKeyValue mapSection = handleMap(data, field);
                    if (mapSection != null) {
                        rootSection.addSubSection(mapSection);

                        continue;
                    }
                    try {
                        Object object = field.get(data);
                        if (object == null) {
                            RenLogger.LOGGER.warn("Value for '" + field.getName() + "' is null. Will not process data.");
                            continue;
                        }
                        rootSection.addKeyValue(field.getName(), object.toString());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalArgumentException ignored) {
                        rootSection.addKeyValue(field.getName(), "null");
                    }
                }
            }
            appendString.append(rootSection);
        }

        FileWriter fileWriter = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                RenLogger.LOGGER.error("Could not create save file: {}", e.getMessage());
            }
        }
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(appendString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private SectionKeyValue handleMap(PersistentData data, Field field) {
        if (field.getGenericType().getTypeName().contains("Map<")) {
            try {
                SectionKeyValue sectionKeyValue = new SectionKeyValue(field.getName());
                reconstructMap(sectionKeyValue, data, field);
                return sectionKeyValue;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private void reconstructMap(SectionKeyValue sectionKeyValue, PersistentData data, Field field) throws IllegalAccessException {
        Map<Object, Object> map = (Map<Object, Object>) field.get(data);
        map.entrySet().forEach(objectObjectEntry -> {
            sectionKeyValue.addKeyValue(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue().toString());
        });
    }


    // Loads save file
    public void load(boolean process) {
        String fullData = null;
        try {
            fullData = new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            // Only error if they are trying to process the loading function.
            if (process) {
                RenLogger.LOGGER.error(e.getMessage());
            }
        }

        if (fullData == null) {
            RenLogger.LOGGER.error("Save file does not exist.");
            return;
        }

        Collection<PersistentData> allData = RenJava.getInstance().getRegisteredData();
        for (Addon addon : RenJava.getInstance().getAddonLoader().getAddons()) {
            allData.addAll(addon.getRegisteredData());
        }

        for (PersistentData persistentData : allData) {
            String clazzName = persistentData.getClass().getName();
            SectionKeyValue rootSection = new SectionKeyValue(clazzName);
            SectionKeyValue mapSection = null;

            String[] classSplit = fullData.split(clazzName + ":");
            String fields = classSplit[1];
            String[] fieldSplit = fields.split("\n");
            for (String field : fieldSplit) {
                if (field.trim().isEmpty()) continue;
                String[] keyValueSplit = field.split(":");
                String key = keyValueSplit[0];
                if (keyValueSplit.length == 1) {
                    if (key.startsWith("    ") || key.startsWith("\t")) {
                        mapSection = new SectionKeyValue(key.trim());
                        rootSection.addSubSection(mapSection);
                        continue;
                    } else {
                        break;
                    }
                }
                String value = keyValueSplit[1];
                if ((key.startsWith("    ") || key.startsWith("\t")) && value.trim().isEmpty()) {
                    mapSection = new SectionKeyValue(key.trim());
                    rootSection.addSubSection(mapSection);
                } else if (value.trim().isEmpty()) {
                    break;
                } else if (key.startsWith("\t\t") || key.startsWith("        ")) {
                    if (mapSection != null) {
                        mapSection.addKeyValue(key.trim(), value.trim());
                    }
                } else if (key.startsWith("\t") || key.startsWith("    ")){
                    if (value.contains(",") || value.contains("[")) {
                        if (value.contains("[")) {
                            value = value.replace("[", "").replace("]", "");
                        }
                        String[] arraySplit = value.trim().split(",");
                        rootSection.addArray(key.trim(), arraySplit);
                        continue;
                    }
                    rootSection.addKeyValue(key.trim(), value.trim());
                }
            }
            if (process) {
                processSection(persistentData, rootSection);
            } else {
                if (rootSection.getSection().contains("me.piitex.renjava.api.player.Player")) {
                    sceneSection = rootSection;
                }
            }
        }
    }


    public void processSection(PersistentData persistentData, SectionKeyValue rootSection) {
        List<Field> fields = new ArrayList<>(List.of(persistentData.getClass().getDeclaredFields()));
        fields.addAll(List.of(persistentData.getClass().getFields()));
        for (Field field : fields) {
            if (field.isAnnotationPresent(Data.class)) {
                field.setAccessible(true);
                if (field.getGenericType().getTypeName().contains("Map<")) {
                    // This is a map load. Scan for subsections if the field name matches. Add all data
                    for (SectionKeyValue subSection : rootSection.getSubSections()) {
                        if (subSection.getSection().equalsIgnoreCase(field.getName())) {
                            // Convert Map<String, String> to whatever the map of the object is
                            // Set the current values to whatever the load file is
                            deconstructMap(subSection, persistentData, field);
                        }
                    }
                }

                String keyToSet = (String) rootSection.getKeyValueMap().keySet().stream().filter(key -> key.toString().equalsIgnoreCase(field.getName())).findAny().orElse(null);
                if (keyToSet != null) {
                    try {
                        // Casting string might not be a good idea.
                        String value = (String) rootSection.getKeyValueMap().get(keyToSet);
                        setField(field, persistentData, value.strip());
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        RenLogger.LOGGER.trace(e.getMessage());
                    }
                } else {
                    RenLogger.LOGGER.error("Could not set key '" + field.getName() + "'");
                }
            }
        }
    }

    private void deconstructMap(SectionKeyValue subSection, PersistentData data, Field field) {
        Map<Object, Object> objectMap;
        try {
             objectMap = (Map<Object, Object>) field.get(data);
        } catch (IllegalAccessException e) {
            objectMap = new HashMap<>();
        }

        if (objectMap == null) {
            objectMap = new HashMap<>();
        }


        // TODO: 3/3/2024 Convert generic map type to actual type using the Mapper class
        Map<Object, Object> finalObjectMap = objectMap;
        subSection.getKeyValueMap().forEach((key, value) -> {
            finalObjectMap.put(key, value);
        });
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

    public ImageOverlay buildPreview(int page) {
        ImageOverlay saveImage;
        if (file.exists()) {
            System.out.println("Save file " + page + " exists.");
            Story story = RenJava.getInstance().getPlayer().getStory((String) sceneSection.get("currentStory"));

            //FIXME: This will produce a lot of programming debt. This is an extremely cheap unoptimized hack.

            // Set the player to the current story (which is off to a horrible start)
            RenJava.getInstance().getPlayer().setCurrentStory(story.getId());

            // Initialize the story to process the scenes. (Used to execute the `addScene` functions which maps the scenes to the story.)
            story.init();

            // Render the scene to the load button.
            RenScene currentScene = story.getScene((String) sceneSection.get("currentScene"));
            if (currentScene == null) {
                RenLogger.LOGGER.error("Save slot '" + slot + "' appears to be corrupt or has missing information. Unable to render save preview for the file. '" + sceneSection.get("currentScene") + "'");
                return new ImageOverlay("gui/button/slot_idle_background.png");
            }

            // When the render function is called, the stage type will be set to scene type. This will cause issues as the player is technically in the save/load screen.
            currentScene.render(currentScene.build(true), false);

            WritableImage snapshot = currentScene.getStage().getScene().snapshot(null);
            saveImage = new ImageOverlay(snapshot);

            saveImage.setWidth(384);
            saveImage.setHeight(216);

            // Since the Renpy assets account for Text they made a transparency space.
            // To circumvent this extra space we need to add the length of the space so everything is properly aligned.
            saveImage.setX(15); // Position inside the button
            saveImage.setY(15); // Position inside the button

             // Get whatever page they are on
            return saveImage;
        }
        saveImage = new ImageOverlay("gui/button/slot_idle_background.png");
        return saveImage;
    }

    public File getFile() {
        return file;
    }
}
