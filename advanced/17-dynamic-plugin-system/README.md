# Dynamic Plugin System

This project demonstrates a dynamic plugin system in Java using **PF4J** (Plugin Framework for Java) and **Spring Boot**. It allows loading and unloading plugins at runtime.

## Requirements

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (optional, for containerized execution)

## Project Structure

- **plugin-api**: Defines the `GreetingExtensionPoint` interface that plugins must implement.
- **welcome-plugin**: A sample plugin implementation that provides a "Welcome" greeting.
- **application**: The main Spring Boot application that manages plugins using PF4J.

## How to Build and Run

### Locally

1. **Build the project**:
   ```bash
   mvn clean package
   ```

2. **Prepare the plugins directory**:
   The application expects a `plugins` directory in the working directory.
   ```bash
   mkdir -p plugins
   cp welcome-plugin/target/welcome-plugin-*.jar plugins/
   ```

3. **Run the application**:
   ```bash
   java -jar application/target/application-*.jar
   ```

### Using Docker

The project includes a `Dockerfile` and `docker-compose.yml` that automatically sets up the environment and places the plugin in the correct directory.

```bash
docker compose up --build
```

## Usage

Once the application is running (default port 8080):

1. **List loaded plugins**:
   ```bash
   curl http://localhost:8080/api/plugins
   ```
   Response:
   ```json
   [{"id":"welcome-plugin","state":"STARTED","version":"0.0.1"}]
   ```

2. **Execute Plugin Extensions (Greetings)**:
   ```bash
   curl http://localhost:8080/api/plugins/greetings
   ```
   Response:
   ```json
   ["Welcome to the Dynamic Plugin System!"]
   ```

3. **Reload Plugins** (if you added new ones at runtime):
   ```bash
   curl http://localhost:8080/api/plugins/load
   ```

## Testing

The project includes integration tests that verify plugin loading logic by dynamically creating and loading a dummy plugin.

To run the tests:

```bash
mvn clean test
```

Or if using the wrapper (not included but common):
```bash
./mvnw clean test
```

Note: This project uses Maven, but if you were using Gradle, you would run `./gradlew clean test`.
