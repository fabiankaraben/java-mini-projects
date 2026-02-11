package com.fabiankaraben.miniprojects.customdb.engine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class StorageEngineTest {

    private StorageEngine storageEngine;
    private File tempDbFile;

    @BeforeEach
    void setUp() throws IOException {
        storageEngine = new StorageEngine();
        tempDbFile = File.createTempFile("test-db", ".log");
        tempDbFile.delete(); // StorageEngine should create it
        ReflectionTestUtils.setField(storageEngine, "dbFilePath", tempDbFile.getAbsolutePath());
        storageEngine.init();
    }

    @AfterEach
    void tearDown() throws IOException {
        storageEngine.close();
        if (tempDbFile.exists()) {
            tempDbFile.delete();
        }
    }

    @Test
    void testSetAndGet() throws IOException {
        storageEngine.set("key1", "value1");
        assertEquals("value1", storageEngine.get("key1"));
    }

    @Test
    void testPersistence() throws IOException {
        storageEngine.set("key1", "value1");
        storageEngine.close();

        // Re-initialize to simulate restart
        StorageEngine newEngine = new StorageEngine();
        ReflectionTestUtils.setField(newEngine, "dbFilePath", tempDbFile.getAbsolutePath());
        newEngine.init();

        assertEquals("value1", newEngine.get("key1"));
        newEngine.close();
    }

    @Test
    void testUpdateKey() throws IOException {
        storageEngine.set("key1", "value1");
        storageEngine.set("key1", "value2");
        assertEquals("value2", storageEngine.get("key1"));
        
        // Verify persistence of update
        storageEngine.close();
        StorageEngine newEngine = new StorageEngine();
        ReflectionTestUtils.setField(newEngine, "dbFilePath", tempDbFile.getAbsolutePath());
        newEngine.init();
        
        assertEquals("value2", newEngine.get("key1"));
        newEngine.close();
    }

    @Test
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> storageEngine.set(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> storageEngine.set("key,with,comma", "value"));
        assertThrows(IllegalArgumentException.class, () -> storageEngine.set("key", null));
    }
    
    @Test
    void testSanitization() throws IOException {
        storageEngine.set("key1", "line1\nline2");
        String value = storageEngine.get("key1");
        assertEquals("line1 line2", value); // Newlines replaced by spaces
    }
}
