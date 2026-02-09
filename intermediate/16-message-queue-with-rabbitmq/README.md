# Message Queue with RabbitMQ

This mini-project demonstrates a Java backend using **Spring AMQP** (Spring Boot) to interact with **RabbitMQ** for pub/sub messaging.

## Requirements

- Java 17+
- Docker and Docker Compose (for running RabbitMQ and the application)
- Gradle (optional if using the provided wrapper)

## Project Structure

- **Spring Boot**: Backend framework.
- **Spring AMQP**: For RabbitMQ integration.
- **Testcontainers**: For integration testing with a real RabbitMQ instance.
- **Docker Compose**: For running the application and RabbitMQ together.

## Usage

### Running with Docker Compose

To start the application and RabbitMQ:

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`.
RabbitMQ Management UI will be available at `http://localhost:15672` (User: `guest`, Pass: `guest`).

### Sending Messages

You can send a message to the queue using `curl`:

```bash
curl -X POST -H "Content-Type: text/plain" -d "Hello RabbitMQ" http://localhost:8080/api/messages
```

Check the application logs to see the message being received and processed by the listener.

## Testing

This project uses **Testcontainers** to spin up a RabbitMQ Docker container for integration tests.

To run the tests:

```bash
./gradlew clean test
```

The test output will show passed/skipped/failed events.

## Features

- **Publisher**: Sends messages to a Topic Exchange.
- **Listener**: Listens to a Queue bound to the Exchange and processes messages.
- **Integration Tests**: Verifies that messages sent are correctly received using a real RabbitMQ instance.
