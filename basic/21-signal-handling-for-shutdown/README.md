# Signal Handling for Shutdown

This mini-project demonstrates how to implement a graceful shutdown in a Java backend application using **Shutdown Hooks**. It ensures that when the server receives a termination signal (like `SIGINT` or `SIGTERM`), it cleans up resources and completes pending requests before exiting.

## 📋 Requirements

- Java 17+
- Maven 3.6+
- Curl (for testing)

## 🚀 How to Use

### 1. Build the Project

```bash
mvn clean package
```

### 2. Run the Application

**Recommended:** Run the compiled JAR directly. This ensures that system signals (like `SIGINT`) are correctly passed to the application.

```bash
java -jar target/signal-handling-shutdown-1.0-SNAPSHOT.jar
```

Alternatively, you can run using Maven, but note that `mvn` might swallow the `SIGINT` signal, preventing the shutdown hook from triggering correctly in some environments:

```bash
mvn exec:java -Dexec.mainClass="com.example.shutdown.ShutdownDemo"
```

The server will start on port `8080`.

### 3. Test Endpoints

There are two endpoints available:

- `GET /`: Returns a simple hello message.
- `GET /long-process`: Simulates a task that takes 5 seconds to complete.

**Example using curl:**

```bash
curl http://localhost:8080/
curl http://localhost:8080/long-process
```

### 4. Testing Graceful Shutdown

To verify that the graceful shutdown works:

1.  Start the server.
2.  In a separate terminal, trigger a long-running request:
    ```bash
    curl http://localhost:8080/long-process
    ```
3.  Immediately go back to the server terminal and press `Ctrl+C` (which sends `SIGINT`).

**Expected Behavior:**
- The server will catch the `SIGINT`.
- It will print `[Shutdown Hook] Shutdown signal received...`.
- It will **wait** for the `/long-process` request to complete (up to the defined timeout).
- You should see the curl command finish successfully with "Long process finished!".
- Then the server will print `[Shutdown Hook] Server stopped gracefully.` and exit.

## 🧪 Automated Testing

A shell script is provided to automate the verification of the shutdown signal.

```bash
./test-shutdown.sh
```

This script will:
1. Start the server in the background.
2. Send a request to `/long-process`.
3. Send a `SIGTERM` to the server process while the request is processing. (Note: The script uses `SIGTERM` because `SIGINT` is often ignored by background processes in scripts, but the Shutdown Hook handles both identicaly).
4. Verify via logs if the request completed before the server shut down.

**Verification Checklist:**
- [ ] Log: `[Shutdown Hook] Shutdown signal received...`
- [ ] Log: `[Shutdown Hook] Draining active requests...`
- [ ] Log: `[Handler] Long process finished...` (Must appear before server stop)
- [ ] Log: `[Shutdown Hook] Server stopped gracefully.`
