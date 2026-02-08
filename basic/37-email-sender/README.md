# Email Sender

This is a simple Java backend application that sends plain text emails using JavaMail (Jakarta Mail) via an SMTP server. It exposes a REST API using Javalin to trigger email sending.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher
- Docker & Docker Compose (optional, for running with a local SMTP server)

## Features

- REST API to send emails.
- Integration with an SMTP server (defaults to localhost:1025).
- Docker support for easy deployment and local testing with Mailpit.
- Unit testing with GreenMail to mock the SMTP server.

## How to Run

### Using Maven (Local Development)

1.  **Build the project:**

    ```bash
    mvn clean package
    ```

2.  **Run the application:**

    You need an SMTP server running. You can use Mailpit via Docker (see below) or any other SMTP server.

    ```bash
    # Set environment variables if needed (defaults: localhost:1025, no auth)
    export SMTP_HOST=localhost
    export SMTP_PORT=1025
    
    java -jar target/email-sender-1.0-SNAPSHOT.jar
    ```

### Using Docker Compose

This is the recommended way to run the application as it sets up both the application and a local SMTP server (Mailpit) with a web interface to view sent emails.

1.  **Start the services:**

    ```bash
    docker compose up --build
    ```

    This will start:
    - The **Email Sender** application on port `7070`.
    - **Mailpit** (SMTP server) on port `1025` (SMTP) and `8025` (Web UI).

## How to Use

### Send an Email

You can send an email by making a POST request to `/send-email` with a JSON body.

**cURL Example:**

```bash
curl -X POST http://localhost:7070/send-email \
     -H "Content-Type: application/json" \
     -d '{
           "to": "user@example.com",
           "subject": "Hello from Java!",
           "body": "This is a test email sent via JavaMail."
         }'
```

### View Sent Emails (Docker Compose)

If you are running with Docker Compose, open your browser and go to:

[http://localhost:8025](http://localhost:8025)

You will see the emails sent by the application in the Mailpit interface.

## Testing

This project uses **GreenMail** to mock the SMTP server for integration testing, ensuring that emails are correctly sent without needing a real mail server.

To run the tests:

```bash
mvn test
```

The tests will:
1. Start an embedded GreenMail SMTP server.
2. Send an email using the `EmailService`.
3. Verify that the email was received by GreenMail with the correct content.
