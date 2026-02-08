package com.example.validator;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<Map<String, String>> createUser(@Valid @RequestBody UserDto userDto) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User created successfully");
        response.put("username", userDto.getUsername());
        return ResponseEntity.ok(response);
    }
}
