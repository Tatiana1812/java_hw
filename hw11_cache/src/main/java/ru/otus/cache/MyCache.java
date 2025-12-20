package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        notify(key, value, "put");
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        V oldValue = cache.get(key);
        if (oldValue != null) {
            notify(key, oldValue, "remove");
            cache.remove(key);
        }
    }

    @Override
    public V get(K key) {
        notify(key, cache.get(key), "get");
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notify(K key, V value, String action) {
        for (var listener : listeners) {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                logger.warn("Listener can't send notification `{}`", e.getMessage());
            }
        }
    }
}
