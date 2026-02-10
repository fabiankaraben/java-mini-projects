package com.example.serverless;

import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Functions {

    @Bean
    public Function<String, String> uppercase() {
        return value -> {
            if (value == null) {
                return "NULL";
            }
            return value.toUpperCase();
        };
    }

    @Bean
    public Function<String, String> reverse() {
        return value -> {
             if (value == null) {
                return "NULL";
            }
            return new StringBuilder(value).reverse().toString();
        };
    }
}
