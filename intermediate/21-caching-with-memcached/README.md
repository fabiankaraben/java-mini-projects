# Caching with Memcached

This mini-project demonstrates a Java backend using Spring Boot and **Memcached** for distributed caching. It serves as an educational example of how to integrate a caching layer into a Spring Boot application using the `spymemcached` client.

## Requirements

- Java 17 or higher
- Maven
- Docker and Docker Compose (for running Memcached and integration tests)

## Project Structure

- **src/main/java**: Contains the Spring Boot application, Memcached configuration, Service, and Controller.
- **src/test/java**: Contains integration tests using **Testcontainers**.
- **Dockerfile**: For containerizing the application.
- **docker-compose.yml**: For running the application and Memcached together.

## Getting Started

### 1. Build the Project

You can build the project using Maven:

```bash
mvn clean package
```

### 2. Run with Docker Compose

To run the application along with a Memcached instance:

```bash
docker compose up --build
```

The application will be accessible at `http://localhost:8080`.

### 3. Usage (Curl Examples)

The API provides endpoints to Set, Get, and Delete cache entries.

**Set a value in the cache:**

```bash
curl -X POST "http://localhost:8080/api/cache/myKey?expiration=3600" \
     -H "Content-Type: text/plain" \
     -d "Hello Memcached"
```
*Response: Key set*

**Get a value from the cache:**

```bash
curl -X GET "http://localhost:8080/api/cache/myKey"
```
*Response: Hello Memcached*

**Delete a value from the cache:**

```bash
curl -X DELETE "http://localhost:8080/api/cache/myKey"
```
*Response: Key deleted*

## Testing

This project uses **Testcontainers** to spin up a Memcached container for integration testing. This ensures that tests run against a real Memcached instance.

To run the tests:

```bash
mvn clean test
```
(Note: Since this project uses Maven, we use `mvn` instead of `./gradlew`. If you prefer Gradle, please let me know to convert it, but the requirements specified Maven.)

The tests verify:
- Data persistence (Set and Get).
- Cache expiration.
- Data deletion.

## Implementation Details

- **MemcachedConfig**: Configures the `MemcachedClient` using `spymemcached`.
- **CacheService**: Wraps the Memcached client operations, including asynchronous retrieval.
- **CacheController**: Exposes REST endpoints to interact with the cache.
