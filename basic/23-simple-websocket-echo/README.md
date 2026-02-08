# Simple WebSocket Echo

🔹 This is a simple backend in Java using the WebSocket API (JSR 356), echoing messages back to the client.

## Requirements
- Java 17 or later
- Maven 3.6 or later

## How to Run
1. Navigate to the project directory:
   ```bash
   cd basic/23-simple-websocket-echo
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the server:
   ```bash
   mvn exec:java
   ```
   The server will start at `ws://localhost:8025/ws/echo`.

## How to Use
You can connect to the WebSocket server using any WebSocket client.

### Curl Example (Handshake Verification)
Since `curl` is primarily an HTTP tool, you can use it to verify the WebSocket handshake is working:

```bash
curl -i -N \
  -H "Connection: Upgrade" \
  -H "Upgrade: websocket" \
  -H "Host: localhost:8025" \
  -H "Origin: http://localhost:8025" \
  -H "Sec-WebSocket-Key: SGVsbG8sIHdvcmxkIQ==" \
  -H "Sec-WebSocket-Version: 13" \
  http://localhost:8025/ws/echo
```

You should see a response with `HTTP/1.1 101 Switching Protocols`, indicating a successful upgrade.

For a full interactive experience, we recommend using a tool like `websocat`:
```bash
websocat ws://localhost:8025/ws/echo
```
Type a message and press Enter; the server will echo it back.

## How to Run Tests
This project includes integration tests that verify the WebSocket echo functionality using a Java client.

Run the tests using Maven:
```bash
mvn clean test
```
