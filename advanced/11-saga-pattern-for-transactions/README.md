# Saga Pattern for Transactions

This mini-project demonstrates the **Saga Pattern** for distributed transactions in a microservices architecture using **Axon Framework** and **Spring Boot**.

The Saga orchestrates a sequence of transactions across multiple aggregates (Order, Payment, Shipping) to ensure data consistency. It implements compensation logic (rollback) in case of failures at any stage.

## Requirements

- Java 17+
- Maven
- Docker & Docker Compose (for running with Axon Server)

## Project Structure

- **Core API**: Defines Commands and Events.
- **Aggregates**:
  - `OrderAggregate`: Handles order creation, confirmation, and cancellation.
  - `PaymentAggregate`: Handles payment processing and refunds.
  - `ShippingAggregate`: Handles shipping preparation.
- **Saga**:
  - `OrderManagementSaga`: Orchestrates the flow: `Create Order` -> `Process Payment` -> `Prepare Shipping` -> `Confirm Order`. Handles compensations if Payment or Shipping fails.

## Getting Started

### 1. Build the Project

```bash
mvn clean install
```

### 2. Run Tests

The project includes integration tests that verify the happy path and compensation logic using an in-memory configuration (no external dependencies required for tests).

```bash
mvn clean test
```

### 3. Run with Docker Compose

To run the application with **Axon Server**:

1. Build the Docker image:
   ```bash
   mvn clean package
   docker compose build
   ```

2. Start the services:
   ```bash
   docker compose up -d
   ```

   This will start:
   - `axon-server`: The event store and message router.
   - `saga-app`: The Spring Boot application (port 8080).

   Access Axon Server Dashboard at: http://localhost:8024

### 4. API Usage (Curl Examples)

**Happy Path (Successful Order)**
```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "itemType": "Laptop",
    "price": 800.0,
    "currency": "USD"
  }'
```
*Result*: Order Created -> Payment Processed -> Shipping Prepared -> Order Confirmed.

**Payment Failure (Compensation)**
Triggered when price > 1000.
```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "itemType": "Gaming PC",
    "price": 1500.0,
    "currency": "USD"
  }'
```
*Result*: Order Created -> Payment Failed -> Order Cancelled.

**Shipping Failure (Compensation)**
Triggered when simulating a specific scenario (requires modification of logic or data to force failure, or handled via test cases). In the current implementation, shipping failure logic is mainly demonstrated in tests or can be triggered if configured.
(Note: The `ShippingAggregate` logic checks if `orderId` contains "fail-shipping").

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "itemType": "Bad Item",
    "price": 500.0,
    "currency": "USD"
  }'
```
(You would need to control the generated UUID to test this via API, which is tricky. Rely on integration tests for this scenario).

## Observability

- **Axon Server Dashboard**: Visualizes the flow of messages (Commands, Events) and the connected applications.
