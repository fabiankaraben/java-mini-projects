package com.fabiankaraben.jsonapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testUserSerialization() throws JsonProcessingException {
        User user = new User(1, "Alice", "alice@example.com");

        String json = objectMapper.writeValueAsString(user);

        // Verify JSON contains expected fields
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"name\":\"Alice\""));
        assertTrue(json.contains("\"email\":\"alice@example.com\""));
    }

    @Test
    void testUserDeserialization() throws JsonProcessingException {
        String json = "{\"id\":2,\"name\":\"Bob\",\"email\":\"bob@example.com\"}";

        User user = objectMapper.readValue(json, User.class);

        assertEquals(2, user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("bob@example.com", user.getEmail());
    }
}
