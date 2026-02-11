# Rule Engine Service

This is a backend service implemented in Java using **Spring Boot** and **Drools** (Business Rules Management System). It executes complex business rules dynamically defined in DRL files to calculate discounts for orders.

## Requirements

- Java 17+
- Maven
- Docker (optional, for containerized execution)

## Features

- **Dynamic Rule Execution**: Uses Drools to apply business rules separate from the application logic.
- **REST API**: Exposes an endpoint to process orders and apply rules.
- **Unit Testing**: Verifies rule logic using JUnit and Spring Boot Test.

## API Usage

### Calculate Discount

**Endpoint**: `POST /api/rules/order-discount`

**Request Body**:
```json
{
  "customerType": "LOYAL",
  "amount": 5000.0
}
```

**Curl Example**:
```bash
curl -X POST http://localhost:8080/api/rules/order-discount \
  -H "Content-Type: application/json" \
  -d '{
    "customerType": "LOYAL",
    "amount": 5000.0
  }'
```

**Response**:
```json
{
  "orderId": null,
  "customerType": "LOYAL",
  "amount": 5000.0,
  "discount": 0.1,
  "ruleApplied": "Loyal Customer Order"
}
```

## Rules

The following rules are defined in `src/main/resources/rules/discount-rules.drl`:

1.  **High Value Order**: If amount > 10000, discount is 15%.
2.  **Loyal Customer Order**: If customer is "LOYAL" and amount is between 1000 and 10000, discount is 10%.
3.  **Standard Order**: If amount <= 1000 and customer is not "VIP", discount is 0%.
4.  **VIP Customer Flat Discount**: If customer is "VIP", discount is 20% (regardless of amount).

## Running the Application

### Locally
```bash
mvn spring-boot:run
```

### Using Docker
```bash
docker compose up --build
```

## Testing

Run unit tests to verify the rules:

```bash
mvn clean test
```

The tests assert that the facts (Order details) match the expected rule outcomes defined in the DRL file.
