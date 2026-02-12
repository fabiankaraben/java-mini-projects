package com.fabiankaraben.miniprojects.customdb;

import com.fabiankaraben.miniprojects.customdb.engine.StorageEngine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomDbIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StorageEngine storageEngine;

    private static File tempDbFile;

    static {
        try {
            tempDbFile = File.createTempFile("integration-test-db", ".log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("db.data.file", tempDbFile::getAbsolutePath);
    }
    
    @AfterEach
    void tearDown() {
        // We don't delete the file here to allow inspection if needed, 
        // but in a real scenario we might. 
        // For test isolation, we might want to clear it, but StorageEngine appends.
        // We'll trust unique keys for isolation or just let it grow.
        // Actually, let's delete it to be clean.
        if(tempDbFile.exists()) {
             tempDbFile.delete();
        }
    }

    @Test
    void testHeavyReadWriteOperations() throws Exception {
        int threadCount = 10;
        int operationsPerThread = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // Concurrent Writes
        executorService.submit(() -> {
            IntStream.range(0, threadCount * operationsPerThread).parallel().forEach(i -> {
                try {
                    mockMvc.perform(post("/api/db/key" + i)
                            .content("value" + i))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }).get(30, TimeUnit.SECONDS);

        // Verify Writes
        for (int i = 0; i < threadCount * operationsPerThread; i++) {
             mockMvc.perform(get("/api/db/key" + i))
                    .andExpect(status().isOk())
                    .andExpect(content().string("value" + i));
        }
        
        executorService.shutdown();
    }
}
