# E2E Encrypted Chat

This mini-project implements a backend for an End-to-End (E2E) Encrypted Chat application using Java and Spring Boot. It facilitates secure communication by acting as a registry for public keys (Diffie-Hellman key exchange) and a relay for encrypted messages. The server **never** sees the plain text messages; it only stores and forwards encrypted blobs.

## Features
- **Key Registry**: Users can register their public Diffie-Hellman keys.
- **Key Exchange**: Users can retrieve other users' public keys to compute a shared secret.
- **Message Relay**: Users send encrypted messages to the server, which stores them for the recipient.
- **Zero Knowledge**: The server does not have access to private keys or shared secrets, ensuring it cannot decrypt messages.

## Requirements
- Java 17+
- Maven
- Docker & Docker Compose (optional, for containerized run)

## API Usage

### 1. Register a Public Key
Alice registers her public key (Base64 encoded).
```bash
curl -X POST http://localhost:8080/api/keys/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "publicKey": "MIIWDjCC... (Base64 Encoded Key)"
  }'
```

### 2. Retrieve a Public Key
Bob retrieves Alice's public key to generate the shared secret.
```bash
curl -X GET http://localhost:8080/api/keys/alice
```

### 3. Send an Encrypted Message
Alice sends a message to Bob. The content must be encrypted on the client side using the shared secret.
```bash
curl -X POST http://localhost:8080/api/chat/send \
  -H "Content-Type: application/json" \
  -d '{
    "sender": "alice",
    "recipient": "bob",
    "content": "U2FsdGVkX1... (Base64 Encoded Encrypted Content)",
    "timestamp": 1678900000000
  }'
```

### 4. Retrieve Messages
Bob checks for new messages.
```bash
curl -X GET "http://localhost:8080/api/chat/messages?username=bob"
```

## Running the Application

### Using Maven
```bash
mvn spring-boot:run
```

### Using Docker Compose
This project is fully containerized. To run it using Docker Compose:
```bash
docker compose up --build
```
The application will be available at `http://localhost:8080`.

## Testing
This project includes integration tests that simulate the full End-to-End encryption flow. The test acts as both "Alice" and "Bob", generating real Diffie-Hellman key pairs, performing the handshake via the server, and verifying that the message sent by Alice can be decrypted by Bob, while the server remains oblivious.

To run the tests:
```bash
mvn clean test
```
