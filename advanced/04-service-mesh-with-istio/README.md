# Service Mesh with Istio

This mini-project demonstrates a microservices backend in Java, designed to be deployed in an Istio service mesh. It features two services, **Consumer Service** and **Provider Service**, communicating via HTTP.

## Requirements

- Java 17+ (Project uses Java 25 toolchain targeting Java 17 release)
- Gradle (Wrapper included)
- Docker (for local execution via Docker Compose)
- Kubernetes & Istio (for mesh deployment, optional)

## Structure

- **consumer-service**: Spring Boot application that calls the Provider Service.
- **provider-service**: Spring Boot application that returns a greeting.
- **k8s/**: Kubernetes and Istio manifest files.

## Testing

The project uses **Pact** for contract testing to ensure reliable communication between services.

### Running Tests

To run the tests, including Pact contract generation and verification:

```bash
./gradlew clean test
```

You should see output indicating tests passed, skipped, or failed.

- **Consumer Service Tests**: Generates the Pact contract (`consumer-service/build/pacts`).
- **Provider Service Tests**: Verifies the provider against the generated Pact contract.

## Running Locally with Docker Compose

To run the services locally without Kubernetes/Istio:

1.  Build the JARs:
    ```bash
    ./gradlew clean build -x test
    ```
2.  Start with Docker Compose:
    ```bash
    docker compose up --build
    ```
3.  Test the interaction:
    ```bash
    curl http://localhost:8080/consume
    # Output: Consumer received: Hello from Provider Service!
    ```

## Deploying to Kubernetes with Istio

1.  Ensure you have a Kubernetes cluster with Istio installed and sidecar injection enabled for your namespace.
2.  Build the Docker images (ensure they are available to your cluster, e.g., load them into Minikube or push to a registry):
    ```bash
    docker build -t consumer-service:latest ./consumer-service
    docker build -t provider-service:latest ./provider-service
    ```
3.  Apply the manifests:
    ```bash
    kubectl apply -f k8s/deployment.yaml
    ```
4.  Access the service via the Istio Ingress Gateway.

## API Endpoints

### Consumer Service
- `GET /consume`: Calls the Provider Service and returns the result.

### Provider Service
- `GET /greeting`: Returns a JSON greeting `{"message": "..."}`.
