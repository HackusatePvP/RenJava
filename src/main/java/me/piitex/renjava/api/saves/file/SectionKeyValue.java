package me.piitex.renjava.api.saves.file;

import java.util.*;

public class SectionKeyValue {
    private final String section;
    private final Map<Object, Object> keyValueMap = new HashMap<>();
    private final Collection<SectionKeyValue> subSection = new HashSet<>();

    private final Map<String, ArrayList<?>> arrayMap = new HashMap<>();

    public SectionKeyValue(String section) {
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public void addKeyValue(String key, Object value) {
        keyValueMap.put(key, value);
    }

    public void addSubSection(SectionKeyValue sectionKeyValue) {
        subSection.add(sectionKeyValue);
    }

    public void addArray(String field, Object... values) {
        ArrayList<?> array = new ArrayList<>(List.of(values));
        arrayMap.put(field, array);
    }

    public ArrayList<?> getArray(String key) {
        return arrayMap.get(key);
    }

    public Collection<SectionKeyValue> getSubSections() {
        return subSection;
    }

    public Map<String, ArrayList<?>> getArrayMap() {
        return arrayMap;
    }

    public Map<Object, Object> getKeyValueMap() {
        return keyValueMap;
    }

    public Object get(String key) {
        return keyValueMap.get(key);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(section).append(":");
        keyValueMap.entrySet().forEach(stringStringEntry -> {
            stringBuilder.append("\n").append("\t");
            stringBuilder.append(stringStringEntry.getKey()).append(": ").append(stringStringEntry.getValue());
        });
        arrayMap.entrySet().forEach(stringArrayListEntry -> {
            stringBuilder.append("\n\t");
            stringBuilder.append(stringArrayListEntry.getKey()).append(": ");
            for (int i = 0; i < stringArrayListEntry.getValue().size(); i++) {
                stringBuilder.append(stringArrayListEntry.getValue().get(i).toString());
                if (i != stringArrayListEntry.getValue().size()) {
                    stringBuilder.append(",");
                }
            }
        });
        if (!subSection.isEmpty()) {
            for (SectionKeyValue sectionKeyValue : subSection) {
                stringBuilder.append("\n\t");
                stringBuilder.append(sectionKeyValue.toString().replace("\t", "\t\t"));
            }
        } else {
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}