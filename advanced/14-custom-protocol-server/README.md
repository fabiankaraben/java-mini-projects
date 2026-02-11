# Custom Protocol Server

This project implements a backend server in Java using **Netty** that defines a custom binary protocol over TCP.

## Protocol Specification

The protocol uses a simple binary format:

```
[Type (1 byte)] [Length (4 bytes, Big Endian)] [Content (variable bytes)]
```

### Message Types

| Type | Hex  | Description | Response |
|------|------|-------------|----------|
| PING | 0x01 | Keep-alive  | PONG (0x02) |
| ECHO | 0x03 | Echo back   | ECHO_REPLY (0x04) with same content |
| REVERSE | 0x05 | Reverse string | REVERSE_REPLY (0x06) with reversed content |

## Requirements

- Java 17+
- Docker & Docker Compose (optional, for containerized run)

## How to Run

### Local (Gradle)

```bash
./gradlew run
```
The server will start on port 8080.

### Docker

This project uses `docker compose` to run the application.

```bash
docker compose up --build
```

## Usage

Since this is a custom binary protocol, standard HTTP tools like `curl` are not suitable. Instead, we use `netcat` (`nc`) to send raw binary data.

### 1. PING Request
Type: `0x01`, Length: `0`
Hex: `01 00 00 00 00`

```bash
printf "\x01\x00\x00\x00\x00" | nc localhost 8080 | hexdump -C
```
*Expected Output (PONG):* `02 00 00 00 00`

### 2. ECHO Request "Hello"
Type: `0x03`, Length: `5`, Content: `Hello`
Hex: `03 00 00 00 05 48 65 6c 6c 6f`

```bash
printf "\x03\x00\x00\x00\x05Hello" | nc localhost 8080 | hexdump -C
```
*Expected Output (ECHO_REPLY):* `04 00 00 00 05 48 65 6c 6c 6f` ("Hello")

### 3. REVERSE Request "Netty"
Type: `0x05`, Length: `5`, Content: `Netty`
Hex: `05 00 00 00 05 4e 65 74 74 79`

```bash
printf "\x05\x00\x00\x00\x05Netty" | nc localhost 8080 | hexdump -C
```
*Expected Output (REVERSE_REPLY):* `06 00 00 00 05 79 74 74 65 4e` ("ytteN")

## Testing

Integration tests verify the protocol encoding/decoding and server logic using a Netty client.

```bash
./gradlew clean test
```

Test logging is configured to show passed, skipped, and failed events.
