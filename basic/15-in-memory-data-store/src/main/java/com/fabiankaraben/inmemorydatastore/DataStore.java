package com.fabiankaraben.inmemorydatastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    private final Map<String, Item> store = new ConcurrentHashMap<>();

    public Item save(Item item) {
        if (item.getId() == null || item.getId().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        store.put(item.getId(), item);
        return item;
    }

    public Optional<Item> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean delete(String id) {
        return store.remove(id) != null;
    }

    public void clear() {
        store.clear();
    }
}
