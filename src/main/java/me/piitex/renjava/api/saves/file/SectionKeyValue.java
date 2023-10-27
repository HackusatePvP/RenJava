package me.piitex.renjava.api.saves.file;

import java.util.HashMap;
import java.util.Map;

public class SectionKeyValue {
    private final String section;
    private final Map<String, String> keyValueMap = new HashMap<>();

    public SectionKeyValue(String section) {
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public void addKeyValue(String key, String value) {
        keyValueMap.put(key, value);
    }

    public Map<String, String> getKeyValueMap() {
        return keyValueMap;
    }
}
