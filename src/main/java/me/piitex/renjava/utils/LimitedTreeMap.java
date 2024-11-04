package me.piitex.renjava.utils;

import java.util.TreeMap;

public class LimitedTreeMap<K, V> extends TreeMap<K, V> {
    private final int limit;

    public LimitedTreeMap(int limit) {
        this.limit = limit;
    }

    @Override
    public V put(K key, V value) {
        if (size() > limit) {
            // Remove first entry not last
            remove(firstEntry().getKey());
        }
        super.put(key, value);
        System.out.println("Insertion Results: " + size());
        return value;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (size() > limit) {
            // Remove first entry not last
            remove(firstEntry().getKey());
        }
        super.putIfAbsent(key, value);
        return value;
    }
}
