# Payment Reconciliation System

A Java-based backend application that processes large datasets to match internal records with bank statements. It detects matched transactions, missing transactions (in either dataset), and discrepancies (e.g., amount mismatches).

## Requirements

- Java 17+
- Maven
- Docker & Docker Compose (optional, for containerized execution)

## Features

- **Upload Interface**: REST endpoint to upload internal and bank CSV files.
- **Reconciliation Logic**:
  - Matches transactions by ID.
  - Identifies transactions missing in the bank statement.
  - Identifies transactions missing in internal records.
  - Detects amount mismatches for matched IDs.
- **CSV Parsing**: Efficient parsing of CSV files using Apache Commons CSV.

## Project Structure

```
src/
  main/
    java/com/example/paymentreconciliationsystem/
      controller/       # REST Controller
      model/            # Domain models (Transaction, ReconciliationResult)
      service/          # Business logic for reconciliation and CSV parsing
  test/
    java/com/example/paymentreconciliationsystem/service/
                        # Unit tests
Dockerfile
docker-compose.yml
pom.xml
```

## Getting Started

### Running Locally with Maven

1.  Clone the repository and navigate to the project directory:
    ```bash
    cd advanced/43-payment-reconciliation-system
    ```

2.  Build the project:
    ```bash
    mvn clean package
    ```

3.  Run the application:
    ```bash
    java -jar target/payment-reconciliation-system-0.0.1-SNAPSHOT.jar
    ```

### Running with Docker Compose

1.  Build and start the container:
    ```bash
    docker compose up --build
    ```

   The application will be accessible at `http://localhost:8080`.

## API Usage

### Reconcile Transactions

**Endpoint**: `POST /api/reconciliation/upload`

**Parameters**:
- `internal`: CSV file containing internal transaction records.
- `bank`: CSV file containing bank statement records.

**CSV Format**:
The CSV files must have the following header: `TransactionID,Date,Amount,Description`

Example content:
```csv
TransactionID,Date,Amount,Description
T1,2023-10-01,100.00,Payment 1
T2,2023-10-02,200.00,Payment 2
```

**Curl Example**:

Create two sample files: `internal.csv` and `bank.csv`.

`internal.csv`:
```csv
TransactionID,Date,Amount,Description
T1,2023-10-01,100.00,Payment 1
T2,2023-10-02,200.00,Payment 2
T3,2023-10-03,300.00,Payment 3
```

`bank.csv`:
```csv
TransactionID,Date,Amount,Description
T1,2023-10-01,100.00,Payment 1
T2,2023-10-02,205.00,Payment 2
T4,2023-10-04,400.00,Payment 4
```

Send the request:
```bash
curl -F "internal=@internal.csv" -F "bank=@bank.csv" http://localhost:8080/api/reconciliation/upload
```

**Response**:
```json
{
  "matchedTransactions": [
    {
      "transactionId": "T1",
      "date": "2023-10-01",
      "amount": 100.00,
      "description": "Payment 1",
      "source": "INTERNAL"
    }
  ],
  "missingInBank": [
    {
      "transactionId": "T3",
      "date": "2023-10-03",
      "amount": 300.00,
      "description": "Payment 3",
      "source": "INTERNAL"
    }
  ],
  "missingInInternal": [
    {
      "transactionId": "T4",
      "date": "2023-10-04",
      "amount": 400.00,
      "description": "Payment 4",
      "source": "BANK"
    }
  ],
  "discrepancies": [
    {
      "internal": {
        "transactionId": "T2",
        "date": "2023-10-02",
        "amount": 200.00,
        "description": "Payment 2",
        "source": "INTERNAL"
      },
      "bank": {
        "transactionId": "T2",
        "date": "2023-10-02",
        "amount": 205.00,
        "description": "Payment 2",
        "source": "BANK"
      },
      "reason": "Amount mismatch: Internal=200.00, Bank=205.00"
    }
  ]
}
```

## Testing

The project includes unit tests that verify the reconciliation logic using mismatched datasets.

Run the tests using Maven:
```bash
mvn clean test
```
