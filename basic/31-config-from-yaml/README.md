# Config from YAML

This is a simple backend mini-project in Java that demonstrates how to load configuration from a YAML file using the Jackson library.

## Requirements

- Java 17 or higher
- Maven 3.6+

## Project Structure

- `src/main/java/com/example/configyaml/AppConfig.java`: Configuration POJO (Plain Old Java Object) mapping the YAML structure.
- `src/main/java/com/example/configyaml/ConfigLoader.java`: Utility class to load YAML files into the Java object.
- `src/main/java/com/example/configyaml/Main.java`: Main entry point demonstrating the loading process.
- `src/main/resources/config.yaml`: Sample configuration file.

## Dependencies

- **Jackson Dataformat YAML**: For parsing YAML files.
- **Jackson Databind**: For mapping YAML data to Java objects.
- **JUnit 5**: For unit testing.

## How to Run

1. **Compile and Package**:
   ```bash
   mvn clean package
   ```

2. **Run the Application**:
   ```bash
   java -jar target/config-from-yaml-1.0-SNAPSHOT.jar
   ```

   **Expected Output:**
   ```text
   Starting Config from YAML Loader...
   Configuration loaded successfully!
   App Name: My Awesome Service
   Port: 8080
   Server started on port 8080
   Try: curl http://localhost:8080/config
   ```

## How to Run Tests

Execute the following command to run unit tests:

```bash
mvn clean test
```

## How to Use

The application starts an HTTP server using the port defined in the YAML configuration. It exposes an endpoint to view the loaded configuration.

**Check the loaded configuration:**

```bash
curl http://localhost:8080/config
```

**Response:**
```json
{
  "appName" : "My Awesome Service",
  "port" : 8080,
  "database" : {
    "host" : "localhost",
    "port" : 5432,
    "username" : "admin",
    "password" : "securepassword123"
  },
  "features" : [ "rate-limiting", "authentication", "logging" ]
}
```

To modify the configuration (e.g., change the port), edit `src/main/resources/config.yaml` and re-run the application.
