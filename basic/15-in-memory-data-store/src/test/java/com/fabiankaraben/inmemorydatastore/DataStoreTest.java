package com.fabiankaraben.inmemorydatastore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DataStoreTest {

    private DataStore dataStore;

    @BeforeEach
    void setUp() {
        dataStore = new DataStore();
    }

    @Test
    void save_ShouldStoreItem() {
        Item item = new Item("1", "Content 1");
        dataStore.save(item);

        Optional<Item> retrieved = dataStore.findById("1");
        assertTrue(retrieved.isPresent());
        assertEquals("Content 1", retrieved.get().getContent());
    }

    @Test
    void save_ShouldThrowException_WhenIdIsNull() {
        Item item = new Item(null, "Content");
        assertThrows(IllegalArgumentException.class, () -> dataStore.save(item));
    }

    @Test
    void findAll_ShouldReturnAllItems() {
        dataStore.save(new Item("1", "A"));
        dataStore.save(new Item("2", "B"));

        List<Item> items = dataStore.findAll();
        assertEquals(2, items.size());
    }

    @Test
    void delete_ShouldRemoveItem() {
        dataStore.save(new Item("1", "A"));
        boolean deleted = dataStore.delete("1");
        
        assertTrue(deleted);
        assertTrue(dataStore.findById("1").isEmpty());
    }

    @Test
    void delete_ShouldReturnFalse_WhenItemDoesNotExist() {
        boolean deleted = dataStore.delete("999");
        assertFalse(deleted);
    }
}
