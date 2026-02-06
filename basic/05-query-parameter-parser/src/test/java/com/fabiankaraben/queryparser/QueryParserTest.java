package com.fabiankaraben.queryparser;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryParserTest {

    private final QueryParser parser = new QueryParser();

    @Test
    void testParseSingleParameter() {
        Map<String, String> result = parser.parse("name=John");
        assertEquals(1, result.size());
        assertEquals("John", result.get("name"));
    }

    @Test
    void testParseMultipleParameters() {
        Map<String, String> result = parser.parse("name=John&age=30&city=New%20York");
        assertEquals(3, result.size());
        assertEquals("John", result.get("name"));
        assertEquals("30", result.get("age"));
        assertEquals("New York", result.get("city"));
    }

    @Test
    void testParseEmptyQuery() {
        Map<String, String> result = parser.parse("");
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseNullQuery() {
        Map<String, String> result = parser.parse(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseParameterWithoutValue() {
        Map<String, String> result = parser.parse("flag");
        assertEquals(1, result.size());
        assertEquals("", result.get("flag"));
    }
    
    @Test
    void testParseParameterWithEmptyValue() {
        Map<String, String> result = parser.parse("key=");
        assertEquals(1, result.size());
        assertEquals("", result.get("key"));
    }
}
