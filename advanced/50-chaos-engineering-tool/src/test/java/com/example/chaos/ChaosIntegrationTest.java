package com.example.chaos;

import com.example.chaos.agent.ChaosAgent;
import com.example.chaos.config.ChaosConfig;
import com.example.chaos.dummy.DummyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChaosIntegrationTest {

    private static final String CONFIG_FILE = "chaos-config.json";
    private DummyService dummyService;

    @BeforeEach
    void setUp() {
        dummyService = new DummyService();
    }

    @AfterEach
    void tearDown() {
        new File(CONFIG_FILE).delete();
    }

    @Test
    void testLatencyInjection() throws IOException, InterruptedException {
        // Create config for Latency
        ChaosConfig config = new ChaosConfig();
        ChaosConfig.Target target = new ChaosConfig.Target();
        target.setClassName("com.example.chaos.dummy.DummyService");
        target.setMethodName("sayHello");
        target.setFailureType("LATENCY");
        target.setLatencyMs(500); // 500ms latency
        target.setRate(1.0); // 100% probability
        config.setTargets(Collections.singletonList(target));

        writeConfig(config);

        // Install Agent
        Instrumentation instrumentation = ByteBuddyAgent.install();
        ChaosAgent.premain(null, instrumentation);

        long start = System.currentTimeMillis();
        dummyService.sayHello();
        long duration = System.currentTimeMillis() - start;

        assertTrue(duration >= 500, "Latency should be at least 500ms, but was " + duration);
    }

    @Test
    void testExceptionInjection() throws IOException {
        // Create config for Exception
        ChaosConfig config = new ChaosConfig();
        ChaosConfig.Target target = new ChaosConfig.Target();
        target.setClassName("com.example.chaos.dummy.DummyService");
        target.setMethodName("processData");
        target.setFailureType("EXCEPTION");
        target.setExceptionClass("java.lang.RuntimeException");
        target.setRate(1.0); // 100% probability
        config.setTargets(Collections.singletonList(target));

        writeConfig(config);

        // Install Agent
        Instrumentation instrumentation = ByteBuddyAgent.install();
        ChaosAgent.premain(null, instrumentation);

        assertThrows(RuntimeException.class, () -> dummyService.processData());
    }

    private void writeConfig(ChaosConfig config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(CONFIG_FILE), config);
    }
}
