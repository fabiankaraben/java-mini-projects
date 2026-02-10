# Background Job Processor

This mini-project demonstrates a Java backend using **JobRunr** and **Spring Boot** to handle background job processing, using **PostgreSQL** as the storage provider for job queues.

## Requirements

- Java 17+
- Maven
- Docker & Docker Compose (for PostgreSQL)

## Features

- **Job Enqueueing**: Fire-and-forget background jobs.
- **Job Scheduling**: Delayed jobs execution.
- **JobRunr Dashboard**: Visual interface to monitor jobs.
- **Integration Testing**: Using Testcontainers to spin up ephemeral PostgreSQL instances.

## Setup & Running

1. **Start the database and application with Docker Compose**:
   Since this project uses PostgreSQL for JobRunr storage, it's recommended to run everything via Docker Compose.

   ```bash
   docker compose up --build
   ```

   This will start:
   - PostgreSQL (port 5432)
   - Spring Boot Application (port 8080)

   The JobRunr Dashboard will be available at `http://localhost:8000/dashboard`.

   *Note: If you want to run the application locally without Docker Compose for the app itself, you still need a running PostgreSQL database. You can start just the db with `docker compose up db -d`.*

## Usage

### 1. Enqueue a Job (Fire-and-forget)

```bash
curl -X POST "http://localhost:8080/jobs/email?recipient=user@example.com"
```

Response:
```
Job enqueued for recipient: user@example.com
```

Check the logs or the dashboard to see the job execution.

### 2. Schedule a Delayed Job (10 seconds later)

```bash
curl -X POST "http://localhost:8080/jobs/email/delayed?recipient=delayed@example.com"
```

## Testing

This project uses **Testcontainers** to ensure reliable integration testing with a real PostgreSQL database.

To run the tests:

```bash
./mvnw clean test
```

(Or if you have Maven installed locally: `mvn clean test`)

The tests will:
1. Spin up a Docker container with PostgreSQL.
2. Verify the Spring Context loads.
3. Test the job enqueueing endpoint and verify that JobRunr successfully processes the job.

## Project Structure

- `src/main/java/com/example/backgroundjobprocessor`: Source code
  - `service/EmailService.java`: The service containing the `@Job` annotated method.
  - `controller/JobController.java`: REST endpoints to trigger jobs.
- `src/test/java`: Integration tests
- `docker-compose.yml`: Docker services definition.
- `Dockerfile`: Application container definition.
