# Change Data Capture Service

A Java-based Change Data Capture (CDC) service using [Debezium](https://debezium.io/) Embedded to tail MySQL binary logs and emit events for database changes (CREATE, UPDATE, DELETE).

## 📋 Requirements
- Java 17+
- Maven
- Docker & Docker Compose (for running MySQL)

## 🚀 How to Run

### 1. Start the Infrastructure
This project requires a MySQL database configured with binary logging enabled. Use the provided `docker-compose.yml`.

```bash
docker compose up -d
```

This starts:
- **MySQL** (Port 3306): Pre-configured with binlog enabled.
- **Service** (Port 8080): The CDC application.

### 2. Run the Application (Locally without Docker)
If you want to run the application locally (but still using the Docker MySQL):

```bash
./mvnw clean package
java -jar target/change-data-capture-service-0.0.1-SNAPSHOT.jar
```

Or simply:
```bash
./mvnw spring-boot:run
```

Ensure `application.properties` points to the correct MySQL host/port. By default, it expects `localhost:3306` (which works if you mapped the ports in docker-compose).

### 3. Usage & Verification

The service logs CDC events to the console. You can verify it by modifying the database.

**Step 1: Connect to MySQL**
```bash
docker compose exec mysql mysql -u root -pdebezium inventory
```

**Step 2: Create a Table**
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);
```

**Step 3: Insert Data (Trigger CREATE Event)**
```sql
INSERT INTO users (name, email) VALUES ('Alice', 'alice@example.com');
```
*Check the application logs:*
```
CDC Event: CdcEvent(database=inventory, table=users, operation=CREATE, before=null, after={id=1, name=Alice, email=alice@example.com}, ...)
```

**Step 4: Update Data (Trigger UPDATE Event)**
```sql
UPDATE users SET email = 'alice.new@example.com' WHERE name = 'Alice';
```
*Check the application logs:*
```
CDC Event: CdcEvent(database=inventory, table=users, operation=UPDATE, before={...}, after={id=1, name=Alice, email=alice.new@example.com}, ...)
```

**Step 5: Delete Data (Trigger DELETE Event)**
```sql
DELETE FROM users WHERE name = 'Alice';
```
*Check the application logs:*
```
CDC Event: CdcEvent(database=inventory, table=users, operation=DELETE, before={id=1, ...}, after=null, ...)
```

### 4. Fetch Captured Events via API

You can also retrieve the last 100 captured events via the REST API.

```bash
curl http://localhost:8080/api/events
```

Example response:
```json
[
  {
    "database": "inventory",
    "table": "users",
    "operation": "CREATE",
    "before": null,
    "after": {
      "id": 1,
      "name": "Alice",
      "email": "alice@example.com"
    },
    "timestamp": 1709228400000
  }
]
```

## 🧪 Running Tests

The project includes integration tests using **Testcontainers** to spin up an ephemeral MySQL instance and verify CDC events.

```bash
./mvnw clean test
```

## 🛠 Project Structure

- `src/main/java/com/example/cdc/service/DebeziumService.java`: Configures and runs the embedded Debezium engine.
- `src/main/java/com/example/cdc/model/CdcEvent.java`: POJO representing a change event.
- `docker-compose.yml`: Deploys MySQL with necessary configuration for Debezium.
