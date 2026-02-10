# Payment Gateway Integration

This mini-project demonstrates how to integrate a Java Spring Boot application with the Stripe Payment Gateway. It handles creating Payment Intents and processing Webhook events.

## Features

- **Create Payment Intent**: API to initiate a payment flow.
- **Webhook Handling**: Endpoint to receive and verify Stripe webhook events.
- **Integration Testing**: Uses **Stripe-mock** via **Testcontainers** to simulate Stripe API interactions.

## Requirements

- Java 17
- Maven
- Docker (for running tests and docker-compose)

## Project Structure

- `src/main/java`: Source code for the Spring Boot application.
- `src/test/java`: Integration tests using Testcontainers.
- `Dockerfile`: Docker image definition for the application.
- `docker-compose.yml`: Compose file to run the application alongside `stripe-mock`.

## Getting Started

### 1. Build the Project

```bash
./mvnw clean package
```

### 2. Run Tests

To run the integration tests (requires Docker):

```bash
./mvnw clean test
```

### 3. Run with Docker Compose

You can run the application locally with a mocked Stripe instance:

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`.

## Usage Examples

### Create a Payment Intent

Use `curl` to create a payment intent:

```bash
curl -X POST http://localhost:8080/api/payment/create-payment-intent \
     -H "Content-Type: application/json" \
     -d '{"amount": 2000, "currency": "usd"}'
```

**Response:**

```json
{
  "clientSecret": "pi_..."
}
```

### Simulate a Webhook Event (Manual)

If running with `stripe-mock` (via docker-compose), you can trigger webhooks using the `stripe-mock` control port or by sending a POST request to the application's webhook endpoint directly (though signature verification will fail if not correctly signed).

Since `stripe-mock` is running on port 12111 (API) and 12112 (Control), you can interact with it.

To manually test the webhook endpoint of *this application* with a valid signature is complex without the Stripe CLI or a real Stripe account. However, the integration tests cover this scenario by manually generating a valid signature.

## Configuration

The application is configured in `src/main/resources/application.properties`. You can override these with environment variables:

- `STRIPE_API_KEY`: Your Stripe Secret Key.
- `STRIPE_WEBHOOK_SECRET`: Your Stripe Webhook Signing Secret.
- `STRIPE_API_BASE`: (Optional) Base URL for Stripe API (used for pointing to `stripe-mock`).

## Dependencies

- **Spring Boot Web**: Web framework.
- **Stripe Java SDK**: Client library for Stripe API.
- **Testcontainers**: For running `stripe-mock` in tests.
- **JUnit 5**: Testing framework.
