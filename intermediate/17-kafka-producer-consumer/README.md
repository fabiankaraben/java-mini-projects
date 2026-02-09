# Kafka Producer/Consumer

This mini-project demonstrates a simple **Spring Boot** application that uses **Spring for Apache Kafka** to produce and consume messages. It includes a REST API to trigger message sending and integration tests using **Testcontainers**.

## Requirements

*   Java 17 or higher
*   Docker (for running the application via Docker Compose and for integration tests)
*   Maven

## Project Structure

*   `src/main/java`: Source code for Producer, Consumer, and Controller.
*   `src/test/java`: Integration tests using EmbeddedKafka.
*   `docker-compose.yml`: Defines the Kafka, Zookeeper, and Application services.
*   `Dockerfile`: Multi-stage Docker build for the application.

## How to Use

### Running with Docker Compose

The easiest way to run the application is using Docker Compose. This will start Zookeeper, Kafka, and the Spring Boot application.

```bash
docker compose up --build
```

The application will be accessible at `http://localhost:8080`.

### sending a Message

You can send a message to the Kafka topic `test-topic` using the exposed REST endpoint:

```bash
curl -X POST "http://localhost:8080/api/kafka/send?message=HelloKafka"
```

You should see the response:
`Message sent to the Kafka Topic test-topic successfully`

And in the application logs (or docker compose output), you should see the consumer receiving the message:
`Received Message in group my-group: HelloKafka`

### Running Tests

The integration tests use **EmbeddedKafka** to spin up an in-memory Kafka broker. No Docker daemon is required for running tests.

To run the tests:

```bash
./mvnw clean test
```

## Description of Components

*   **KafkaProducerService**: Wraps `KafkaTemplate` to send messages to a specific topic.
*   **KafkaConsumerService**: Uses `@KafkaListener` to consume messages from the topic.
*   **KafkaController**: Provides a REST endpoint to trigger the producer.
*   **KafkaTopicConfig**: Configures the Kafka topic programmatically.
*   **KafkaIntegrationTest**: Verifies that the application can send and receive messages using `EmbeddedKafka`.
