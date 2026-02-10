# Microservices with Consul

This mini-project demonstrates a microservices backend in Java using **Spring Cloud Consul** for service discovery and configuration, along with **Spring Boot**.

## Requirements

- Java 17+
- Maven 3.6+
- Docker and Docker Compose (optional, for running the full stack)

## Features

- **Service Discovery**: Registers the application with Consul.
- **Health Checks**: Exposes health endpoints for Consul to monitor.
- **REST API**: Simple endpoints to demonstrate service status.

## Getting Started

### Running the Application (Local)

1.  Start Consul (required):
    ```bash
    docker run -d --name=dev-consul -p 8500:8500 hashicorp/consul:1.15
    ```

2.  Build and run the application:
    ```bash
    mvn spring-boot:run
    ```

### Running with Docker Compose

To run the entire stack (Consul + Microservice):

1.  Build the application:
    ```bash
    mvn clean package -DskipTests
    ```

2.  Start the services:
    ```bash
    docker compose up --build
    ```

This will start:
- Consul on port `8500`
- Microservice on port `8080`

## Usage

Once the application is running, you can interact with it using `curl`.

### Check Service Health
```bash
curl http://localhost:8080/actuator/health
```

### Hello Endpoint
```bash
curl http://localhost:8080/hello
```

### List Registered Services
```bash
curl http://localhost:8080/services
```

### List Instances of 'consul-microservice'
```bash
curl http://localhost:8080/instances
```

### Check Consul UI
Open [http://localhost:8500](http://localhost:8500) in your browser to see the registered services in the Consul dashboard.

## Testing

This project uses **Testcontainers** to spin up a Consul container for integration testing.

To run the tests:

```bash
mvn clean test
```

The tests verify:
- Application context loading.
- Successful registration with the Consul container.
- Health check status.
