package me.piitex.renjava.api.saves.data;

import java.util.HashMap;
import java.util.Map;

public class Mapper {

    public static Map<String, String> toStringMap(Map<Object, Object> map) {
        Map<String, String> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue().toString());
        });
        return toReturn;
    }

    public static Map<String, Integer> toStringIntegerMap(Map<Object, Object> map) {
        System.out.println("Reconstructing map...");
        Map<String, Integer> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put(objectObjectEntry.getKey().toString(), (Integer) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<String, Boolean> toStringBooleanMap(Map<Object, Object> map) {
        Map<String, Boolean> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put(objectObjectEntry.getKey().toString(), (Boolean) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<String, Double> toStringDoubleMap(Map<Object, Object> map) {
        Map<String, Double> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put(objectObjectEntry.getKey().toString(), (Double) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<String, Long> toStringLongMap(Map<Object, Object> map) {
        Map<String, Long> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put(objectObjectEntry.getKey().toString(), (Long) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<String, Float> toStringFloatMap(Map<Object, Object> map) {
        Map<String, Float> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put(objectObjectEntry.getKey().toString(), (Float) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<String, Short> toStringShortMap(Map<Object, Object> map) {
        Map<String, Short> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put(objectObjectEntry.getKey().toString(), (Short) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<String, Byte> toStringByteMap(Map<Object, Object> map) {
        Map<String, Byte> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put(objectObjectEntry.getKey().toString(), (Byte) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Integer, String> toIntergerStringMap(Map<Object, Object> map) {
        Map<Integer, String> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Integer) objectObjectEntry.getKey(), objectObjectEntry.getValue().toString());
        });
        return toReturn;
    }

    public static Map<Integer, Integer> toInterMap(Map<Object, Object> map) {
        Map<Integer, Integer> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Integer) objectObjectEntry.getKey(), (Integer) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Integer, Boolean> toIntergerBooleanMap(Map<Object, Object> map) {
        Map<Integer, Boolean> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Integer) objectObjectEntry.getKey(), (Boolean) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Integer, Double> toIntegerDoubleMap(Map<Object, Object> map) {
        Map<Integer, Double> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Integer) objectObjectEntry.getKey(), (Double) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Integer, Long> toIntegerLong(Map<Object, Object> map) {
        Map<Integer, Long> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Integer) objectObjectEntry.getKey(), (Long) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Integer, Float> toIntegerFloatMap(Map<Object, Object> map) {
        Map<Integer, Float> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Integer) objectObjectEntry.getKey(), (Float) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Integer, Short> toIntegerShortMap(Map<Object, Object> map) {
        Map<Integer, Short> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Integer) objectObjectEntry.getKey(), (Short) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Integer, Byte> toIntegerByteMap(Map<Object, Object> map) {
        Map<Integer, Byte> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Integer) objectObjectEntry.getKey(), (Byte) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Double, String> toDoubleStringMap(Map<Object, Object> map) {
        Map<Double, String> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Double) objectObjectEntry.getKey(), objectObjectEntry.getValue().toString());
        });
        return toReturn;
    }

    public static Map<Double, Integer> toDoubleIntergerMap(Map<Object, Object> map) {
        Map<Double, Integer> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Double) objectObjectEntry.getKey(), (Integer) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Double, Boolean> toDoubleBooleanMap(Map<Object, Object> map) {
        Map<Double, Boolean> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Double) objectObjectEntry.getKey(), (Boolean) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Double, Double> toDoubleMap(Map<Object, Object> map) {
        Map<Double, Double> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Double) objectObjectEntry.getKey(), (Double) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Double, Long> toDoubleLongMap(Map<Object, Object> map) {
        Map<Double, Long> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Double) objectObjectEntry.getKey(), (Long) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Double, Float> toDoubleFloatMap(Map<Object, Object> map) {
        Map<Double, Float> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Double) objectObjectEntry.getKey(), (Float) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Double, Short> toDoubleShortMap(Map<Object, Object> map) {
        Map<Double, Short> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Double) objectObjectEntry.getKey(), (Short) objectObjectEntry.getValue());
        });
        return toReturn;
    }

    public static Map<Double, Byte> toDoubleByteMap(Map<Object, Object> map) {
        Map<Double, Byte> toReturn = new HashMap<>();
        map.entrySet().forEach(objectObjectEntry -> {
            toReturn.put((Double) objectObjectEntry.getKey(), (Byte) objectObjectEntry.getValue());
        });
        return toReturn;
    }
}
