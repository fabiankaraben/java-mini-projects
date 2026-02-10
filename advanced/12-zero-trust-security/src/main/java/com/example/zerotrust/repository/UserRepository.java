package com.example.zerotrust.repository;

import com.example.zerotrust.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public UserRepository() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // Pre-populate with some users
        users.put("admin", new User("admin", encoder.encode("admin123"), Set.of("ROLE_ADMIN", "ROLE_USER")));
        users.put("user", new User("user", encoder.encode("user123"), Set.of("ROLE_USER")));
        users.put("guest", new User("guest", encoder.encode("guest123"), Set.of("ROLE_GUEST")));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}
