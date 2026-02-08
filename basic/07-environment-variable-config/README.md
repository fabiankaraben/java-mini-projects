# Environment Variable Config

This mini-project demonstrates how to read configuration from environment variables in a Java application using `System.getenv()`. It uses Maven for dependency management and JUnit 5 with `system-lambda` for testing environment variable interactions.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Project Structure

- `src/main/java/com/fabiankaraben/javaminiprojects/envconfig/AppConfig.java`: Configuration class that wraps `System.getenv()`.
- `src/main/java/com/fabiankaraben/javaminiprojects/envconfig/Main.java`: Main entry point demonstrating usage.
- `src/test/java/com/fabiankaraben/javaminiprojects/envconfig/AppConfigTest.java`: Unit tests mocking environment variables.

## How to Run

### 1. Build the project

```bash
mvn clean package
```

### 2. Run with default configuration

```bash
mvn exec:java
```

Output:
```
Loading configuration...
------------------------------------------------
Server Port: 8080
DB URL:      jdbc:mysql://localhost:3306/defaultdb
DB User:     root
DB Password: ********
API Key:     (Not set - API_KEY environment variable is missing)
------------------------------------------------
```

### 3. Run with custom environment variables

You can pass environment variables to the process.

**Linux/macOS:**
```bash
export SERVER_PORT=9090
export DB_USER=admin
export API_KEY=secret-key
mvn exec:java
```

Or inline:
```bash
SERVER_PORT=9090 DB_USER=admin API_KEY=secret-key mvn exec:java
```

**Windows (PowerShell):**
```powershell
$env:SERVER_PORT="9090"
$env:DB_USER="admin"
$env:API_KEY="secret-key"
mvn exec:java
```

## Running Tests

This project uses `system-lambda` to mock environment variables during testing, allowing us to verify configuration logic without modifying the actual system environment.

To run the tests:

```bash
mvn clean test
```

The output will show the results of the JUnit 5 tests.
