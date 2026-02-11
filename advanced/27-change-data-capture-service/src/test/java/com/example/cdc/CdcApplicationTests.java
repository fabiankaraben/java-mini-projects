package com.example.cdc;

import com.example.cdc.model.CdcEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
class CdcApplicationTests {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("inventory")
            .withUsername("root")
            .withPassword("debezium")
            .withCommand(
                    "--default-authentication-plugin=mysql_native_password",
                    "--server-id=1",
                    "--log-bin=mysql-bin",
                    "--binlog-format=row",
                    "--binlog-row-image=full",
                    "--gtid-mode=ON",
                    "--enforce-gtid-consistency=ON"
            );

    @DynamicPropertySource
    static void configureProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("mysql.host", mysql::getHost);
        registry.add("mysql.port", mysql::getFirstMappedPort);
        registry.add("mysql.user", mysql::getUsername);
        registry.add("mysql.password", mysql::getPassword);
        // Use unique files for testing to avoid conflicts
        registry.add("debezium.offset.storage.file", () -> "target/debezium-offsets-test.dat");
        registry.add("debezium.history.storage.file", () -> "target/debezium-db-history-test.dat");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEventHandler eventHandler;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TestEventHandler testEventHandler() {
            return new TestEventHandler();
        }
    }

    static class TestEventHandler {
        private final List<CdcEvent> events = new CopyOnWriteArrayList<>();

        @EventListener
        public void onEvent(CdcEvent event) {
            events.add(event);
        }

        public List<CdcEvent> getEvents() {
            return events;
        }
        
        public void clear() {
            events.clear();
        }
    }

    @BeforeAll
    static void setup(@Autowired JdbcTemplate jdbcTemplate) {
        // Create table
        jdbcTemplate.execute("CREATE TABLE products (id INT PRIMARY KEY, name VARCHAR(255), price DECIMAL(10,2))");
    }

    @Test
    void testCdcEvents() {
        // 1. Insert
        jdbcTemplate.update("INSERT INTO products (id, name, price) VALUES (?, ?, ?)", 1, "Laptop", 999.99);

        // Wait for CREATE event
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            List<CdcEvent> events = eventHandler.getEvents();
            assertThat(events).anySatisfy(event -> {
                assertThat(event.getOperation()).isEqualTo("CREATE");
                assertThat(event.getTable()).isEqualTo("products");
                assertThat(event.getAfter()).containsEntry("id", 1);
                assertThat(event.getAfter()).containsEntry("name", "Laptop");
            });
        });

        eventHandler.clear();

        // 2. Update
        jdbcTemplate.update("UPDATE products SET price = ? WHERE id = ?", 899.99, 1);

        // Wait for UPDATE event
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            List<CdcEvent> events = eventHandler.getEvents();
            assertThat(events).anySatisfy(event -> {
                assertThat(event.getOperation()).isEqualTo("UPDATE");
                assertThat(event.getTable()).isEqualTo("products");
                assertThat(event.getBefore()).containsEntry("price", 999.99); // Debezium provides before state if configured? 
                // Note: Standard MySQL connector with default config usually provides before state if binlog_row_image=FULL
                assertThat(event.getAfter()).containsEntry("price", 899.99);
            });
        });

        eventHandler.clear();

        // 3. Delete
        jdbcTemplate.update("DELETE FROM products WHERE id = ?", 1);

        // Wait for DELETE event
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            List<CdcEvent> events = eventHandler.getEvents();
            assertThat(events).anySatisfy(event -> {
                assertThat(event.getOperation()).isEqualTo("DELETE");
                assertThat(event.getTable()).isEqualTo("products");
                assertThat(event.getBefore()).containsEntry("id", 1);
            });
        });
    }
}
