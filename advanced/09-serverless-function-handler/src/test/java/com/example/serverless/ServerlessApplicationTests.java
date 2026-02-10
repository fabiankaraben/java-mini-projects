package com.example.serverless;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerlessApplicationTests {

    @Autowired
    private Function<String, String> uppercase;

    @Autowired
    private Function<String, String> reverse;

    @Test
    void testUppercase() {
        assertThat(uppercase.apply("hello")).isEqualTo("HELLO");
    }

    @Test
    void testReverse() {
        assertThat(reverse.apply("world")).isEqualTo("dlrow");
    }
}
