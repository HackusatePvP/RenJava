package me.piitex.renjava.api.saves;


import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.saves.file.SectionKeyValue;

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

    public Save(File file) {
        this.file = file;
    }

    public Save(int slot) {
        this.slot = slot;
        this.file = new File(System.getProperty("user.dir") + "/game/saves/save-" + slot + ".dat");
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
        for (PersistentData data : RenJava.getInstance().getRegisteredData()) {
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
                        System.out.println("Map Section: ");

                        System.out.println(mapSection.toString());
                        rootSection.addSubSection(mapSection);

                        continue;
                    }
                    // Handle fields
                    try {
                        Object object = field.get(data);
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
                System.out.println("Map type: " + field.getGenericType().getTypeName());
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
        // I don't think I need to actually re-create the map. Just in case, I will keep the Mapper class for now.
        //String typeName = field.getGenericType().getTypeName();
        //typeName = typeName.replace("java.util.Map<", "");

        //<java.lang.String, java.lang.Integer>

        //String[] typeSplit = typeName.replace("<", "").replace(">", "").split(",");
        //java.lang.String
        //java.lang.Integer

        // Conversion is required as the map needs to be set to the actual type in order to save it.
        //String key = typeSplit[0];
        //String value = typeSplit[1];



        Map<Object, Object> map = (Map<Object, Object>) field.get(data);
        map.entrySet().forEach(objectObjectEntry -> {
            System.out.println("Reconstructing entry...");
            sectionKeyValue.addKeyValue(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue().toString());
        });
    }


    // Loads save file
    public void load() {
        // Collect and set string data to class data.
        // First loop the registered data and find the string data which corresponds with the data.

        String fullData = null;
        try {
            fullData = new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (fullData == null) {
                RenJava.getInstance().getLogger().severe("Save file does not exist.");
                return;
            }
        }

        for (PersistentData persistentData : RenJava.getInstance().getRegisteredData()) {
            String clazzName = persistentData.getClass().getName();
            SectionKeyValue rootSection = new SectionKeyValue(clazzName);
            SectionKeyValue mapSection = null;

            String[] classSplit = fullData.split(clazzName + ":");
            String fields = classSplit[1];
            System.out.println("Class Split: " + classSplit[1]);
            String[] fieldSplit = fields.split("\n");
            for (String field : fieldSplit) {
                System.out.println("Field: " + field);
                if (field.trim().isEmpty()) continue;
                String[] keyValueSplit = field.split(":");
                String key = keyValueSplit[0];
                if (keyValueSplit.length == 1) {
                    if (key.startsWith("    ") || key.startsWith("\t")) {
                        System.out.println("Mapping found: " + key.trim());
                        mapSection = new SectionKeyValue(key.trim());
                        rootSection.addSubSection(mapSection);
                        continue;
                    } else {
                        break;
                    }
                }
                String value = keyValueSplit[1];
                if ((key.startsWith("    ") || key.startsWith("\t")) && value.trim().isEmpty()) {
                    System.out.println("Mapping found: " + key.trim());
                    mapSection = new SectionKeyValue(key.trim());
                    rootSection.addSubSection(mapSection);
                } else if (value.trim().isEmpty()) {
                    break;
                } else if (key.startsWith("\t\t") || key.startsWith("        ")) {
                    if (mapSection != null) {
                        mapSection.addKeyValue(key.trim(), value.trim());
                    }
                    System.out.println("Mapping entry found: " + key.trim() + "," + value.trim());
                } else if (key.startsWith("\t") || key.startsWith("    ")){
                    // Handle generic entry

                    // Check if entry is an array
                    if (value.contains(",")) {
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
            processSection(persistentData, rootSection);
        }
    }


    public void processSection(PersistentData persistentData, SectionKeyValue rootSection) {
        // Data is the full data string
        // me.piitex.SomeData:
        //    field1: value1
        //    map:
        //        key: value
        //

        System.out.println("================================================");
        System.out.println();
        System.out.println(rootSection.toString());
        System.out.println();
        System.out.println("================================================");

        // Next set the mapping to the fields
        List<Field> fields = new ArrayList<>(List.of(persistentData.getClass().getDeclaredFields()));
        fields.addAll(List.of(persistentData.getClass().getFields()));
        for (Field field : fields) {
            if (field.isAnnotationPresent(Data.class)) {
                System.out.println("Processing Field: " + field.getName());
                field.setAccessible(true);
                if (field.getGenericType().getTypeName().contains("Map<")) {
                    // This is a map load. Scan for subsections if the field name matches. Add all data
                    for (SectionKeyValue subSection : rootSection.getSubSections()) {
                        if (subSection.getSection().equalsIgnoreCase(field.getName())) {
                            // Convert Map<String, String> to whatever the map of the object is

                            // Set the current values to whatever the load file is
                            deconstructMap(subSection, persistentData, field);

                            try {
                                System.out.println("Map: " + field.get(persistentData).toString());
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                }

                String keyToSet = (String) rootSection.getKeyValueMap().keySet().stream().filter(key -> key.toString().equalsIgnoreCase(field.getName())).findAny().orElse(null);
                if (keyToSet != null) {
                    System.out.println("Key To Set: " + keyToSet);
                    try {

                        // Casting string might not be a good idea.
                        String value = (String) rootSection.getKeyValueMap().get(keyToSet);

                        System.out.println("Setting values for: " + field.getName());
                        setField(field, persistentData, value.strip());

                        // Testing checks
                        if (field.getName().equalsIgnoreCase("currentStory")) {
                            System.out.println("Current Story: " + field.get(persistentData));
                        }
                        if (field.getName().equalsIgnoreCase("currentScene")) {
                            System.out.println("Current Scene: " + field.get(persistentData));
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.err.println("Could not set field: " + field.getName());
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
            System.out.println("Map was null. Defaulting...");
            objectMap = new HashMap<>();
        }

        Map<Object, Object> finalObjectMap = objectMap;
        subSection.getKeyValueMap().forEach((key, value) -> {
            System.out.println("Setting map...");
            System.out.println(key + ": " + value);
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
}
