# Email Notification Service

A Spring Boot mini-project that demonstrates an asynchronous email notification system using RabbitMQ for queuing tasks and JavaMailSender for sending emails.

## 📋 Requirements

- Java 17+
- Maven
- Docker & Docker Compose (for running RabbitMQ and GreenMail)

## 🚀 Features

- **Asynchronous Processing**: Uses RabbitMQ to decouple email request acceptance from email sending.
- **Email Sending**: Uses `JavaMailSender` to send emails.
- **Dockerized Environment**: easy setup with `docker-compose`.
- **Integration Testing**: Uses **Testcontainers** (RabbitMQ) and **GreenMail** (SMTP) for robust integration tests.

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.4.1
- Spring AMQP (RabbitMQ)
- Spring Boot Starter Mail
- Docker & Docker Compose

## 🏃‍♂️ How to Run

### 1. Start the services (App + RabbitMQ + GreenMail)

This project uses Docker Compose to run the application along with its dependencies (RabbitMQ and GreenMail).

```bash
docker compose up --build
```

The application will be available at `http://localhost:8081`.
RabbitMQ Management Console: `http://localhost:15672` (guest/guest)
GreenMail API: `http://localhost:8080` (Check received emails)

### 2. Send an Email Notification

You can test the service using `curl`:

```bash
curl -X POST http://localhost:8081/api/email/send \
  -H "Content-Type: application/json" \
  -d '{
    "to": "user@example.com",
    "subject": "Hello from RabbitMQ",
    "body": "This is a test email sent via asynchronous queue!"
  }'
```

You should see a response:
```
Email notification queued successfully.
```

And in the application logs:
```
Email sent to: user@example.com
```

## 🧪 Running Tests

This project includes integration tests that spin up a RabbitMQ container (via Testcontainers) and an embedded GreenMail SMTP server.

To run the tests:

```bash
./mvnw clean test
```
*(Note: If you don't have the wrapper generated, use `mvn clean test`)*

The tests verify:
1.  Posting to the API queues the message in RabbitMQ.
2.  The consumer picks up the message.
3.  An email is actually "sent" and received by the GreenMail server.
