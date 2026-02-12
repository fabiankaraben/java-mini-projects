package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DemoApplicationTests {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

	@LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;

	private String baseUrl = "";
	private RestTemplate restTemplate = new RestTemplate();

	@BeforeEach
	void setUp() {
		baseUrl = "http://localhost:" + port + "/users";
		restTemplate = new RestTemplate();
		userRepository.deleteAll();
	}

	@Test
	void shouldCreateAndRetrieveUser() {
		User user = new User("John Doe", "john@example.com");
		User createdUser = restTemplate.postForObject(baseUrl, user, User.class);

		assertThat(createdUser).isNotNull();
		assertThat(Objects.requireNonNull(createdUser).getId()).isNotNull();
		assertThat(Objects.requireNonNull(createdUser).getName()).isEqualTo("John Doe");

		User retrievedUser = restTemplate.getForObject(baseUrl + "/" + Objects.requireNonNull(createdUser).getId(), User.class);
		assertThat(retrievedUser).isNotNull();
		assertThat(Objects.requireNonNull(retrievedUser).getEmail()).isEqualTo("john@example.com");
	}

	@Test
	void shouldGetAllUsers() {
		userRepository.save(new User("Alice", "alice@example.com"));
		userRepository.save(new User("Bob", "bob@example.com"));

		User[] users = restTemplate.getForObject(baseUrl, User[].class);
		assertThat(users).hasSize(2);
	}

	@Test
	void shouldUpdateUser() {
		User user = userRepository.save(new User("Charlie", "charlie@example.com"));

		user.setName("Charlie Updated");
		restTemplate.put(baseUrl + "/" + user.getId(), user);

		User updatedUser = userRepository.findById(Objects.requireNonNull(user.getId())).orElseThrow();
		assertThat(updatedUser.getName()).isEqualTo("Charlie Updated");
	}

	@Test
	void shouldDeleteUser() {
		User user = userRepository.save(new User("David", "david@example.com"));

		restTemplate.delete(baseUrl + "/" + user.getId());

		assertThat(userRepository.findById(Objects.requireNonNull(user.getId()))).isEmpty();
	}
}
