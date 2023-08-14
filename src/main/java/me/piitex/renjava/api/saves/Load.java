package me.piitex.renjava.api.saves;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.api.saves.exceptions.SaveFileNotFound;
import me.piitex.renjava.api.saves.file.SectionKeyValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Loads a save file.
 */
public class Load {

    public Load(int slot) throws SaveFileNotFound {
        File file = new File(System.getProperty("user.dir") + "/game/saves/" + slot + "/" + slot + "/save-" + slot + ".dat");
        if (!file.exists()) {
            throw new SaveFileNotFound();
        }
        Collection<SectionKeyValue> keyValues = new HashSet<>();
        try {
            Scanner scanner = new Scanner(file);
            SectionKeyValue sectionKeyValue = null;
            while (scanner.hasNextLine()) {
                // classname@field1;value@field2;value@ect..@ect..;clasname2@field;value@field;value@field;value

                // me.piitex.test.TestClass
                //    fieldName: FieldValue
                // me.piitext.test.NextClass
                //    fieldName: FieldValue
                String data = scanner.nextLine();
                if (!data.startsWith("   ")) {
                    if (sectionKeyValue != null) {
                        keyValues.add(sectionKeyValue);
                    }
                    sectionKeyValue = new SectionKeyValue(data.replace(":", ""));
                } else if (sectionKeyValue != null) {
                    String key = data.split(":")[0];
                    String value = data.split(":")[1];
                    sectionKeyValue.addKeyValue(key, value);
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
                        data.getClass().getDeclaredField(s).set(s2, data);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                });
            });
        }
    }
}
