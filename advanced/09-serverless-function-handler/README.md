# Serverless Function Handler

This mini-project demonstrates how to build a serverless function handler in Java using **Spring Cloud Function**, deployable to AWS Lambda.

## Requirements

- Java 17
- Maven
- Docker (for integration tests with LocalStack)

## Project Structure

- `src/main/java`: Contains the Spring Boot application and function definitions.
- `src/test/java`: Contains unit tests and integration tests using LocalStack.
- `Dockerfile`: Multi-stage Dockerfile for building the application.
- `docker-compose.yml`: Docker Compose file for running the application and LocalStack.

## Features

- **Spring Cloud Function**: Decouples business logic from the deployment platform.
- **AWS Lambda Adapter**: Adapts Spring Cloud Function to run on AWS Lambda.
- **LocalStack Integration**: Uses Testcontainers and LocalStack to simulate AWS Lambda for integration testing.

## Functions

The application exposes two functions:
1. `uppercase`: Converts input string to uppercase.
2. `reverse`: Reverses the input string.

## How to Use

### Running Locally with Docker Compose

You can run the application locally using Docker Compose, which will start the application and LocalStack.

```bash
docker compose up --build
```

The application will be accessible at `http://localhost:8080`. You can invoke the functions via HTTP:

**Invoke `uppercase` function:**

```bash
curl -H "Content-Type: text/plain" localhost:8080/uppercase -d "hello world"
# Output: HELLO WORLD
```

**Invoke `reverse` function:**

```bash
curl -H "Content-Type: text/plain" localhost:8080/reverse -d "hello world"
# Output: dlrow olleh
```

### Running Tests

This project includes unit tests and integration tests. The integration tests use Testcontainers to spin up a LocalStack container, simulating AWS Lambda environment.

To run the unit tests:

```bash
mvn clean test
```

To run all tests (including integration tests):

```bash
mvn clean verify
```

## Deployment to AWS Lambda

To deploy to AWS Lambda, build the shaded JAR:

```bash
mvn clean package
```

The resulting JAR file (`target/serverless-function-handler-0.0.1-SNAPSHOT-aws.jar`) can be uploaded to AWS Lambda.
- **Handler**: `org.springframework.cloud.function.adapter.aws.FunctionInvoker`
- **Environment Variable**: `SPRING_CLOUD_FUNCTION_DEFINITION=uppercase` (or `reverse`, or `uppercase|reverse` for multiple functions)
