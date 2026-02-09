# Captcha Generator

This mini-project is a simple backend in Java that generates image captchas. It provides an HTTP server with endpoints to generate a captcha image and validate user input.

## Requirements
- Java 17 or higher
- Maven 3.6 or higher

## Project Structure
- `src/main/java`: Contains the source code for the Captcha Generator and Server.
- `src/test/java`: Contains integration tests.
- `pom.xml`: Maven configuration file.

## Features
- Generates a random alphanumeric captcha image.
- Stores the captcha text in an in-memory session store (Map).
- Validates user input against the stored captcha text using a Session ID cookie.

## How to Use

### 1. Build and Run the Server
To start the server, you can compile and run the `CaptchaServer` class.

Using Maven:
```sh
mvn clean compile exec:java -Dexec.mainClass="com.example.captcha.CaptchaServer"
```
The server will start on port `8080`.

### 2. Generate a Captcha
Send a GET request to `/captcha` to receive a PNG image and a Session ID cookie.

```sh
curl -v -c cookies.txt http://localhost:8080/captcha > captcha.png
```
- `-v`: Verbose mode (to see headers).
- `-c cookies.txt`: Save the received cookies (Session ID) to a file.
- `> captcha.png`: Save the response body (image) to a file.

Open `captcha.png` to view the captcha text.

### 3. Validate the Captcha
Send a POST (or GET) request to `/validate` with the `code` query parameter and the session cookie.

```sh
# Replace 'CODE' with the text you see in the image
curl -v -b cookies.txt "http://localhost:8080/validate?code=CODE"
```
- `-b cookies.txt`: Send the cookies saved from the previous request.

**Response:**
- `200 OK` with body `Valid` if the code matches.
- `400 Bad Request` with body `Invalid` if the code does not match.
- `403 Forbidden` if the session is invalid or expired.

## Running Tests
To run the integration tests:

```sh
mvn clean test
```
The tests verify:
1. The `/captcha` endpoint returns a PNG image and sets a `SESSIONID` cookie.
2. The `/validate` endpoint correctly handles invalid codes and missing sessions.
