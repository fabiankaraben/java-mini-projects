package com.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
public class SimpleHttpServerTest {

    @Container
    public static GenericContainer<?> server = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withFileFromPath(".", Paths.get("."))
                    .withDockerfile(Paths.get("Dockerfile")))
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/").forStatusCode(200));

    @Test
    public void testEndpoint() {
        String address = "http://" + server.getHost() + ":" + server.getMappedPort(8080);
        
        given()
            .baseUri(address)
        .when()
            .get("/")
        .then()
            .statusCode(200)
            .body(equalTo("Hello from Dockerized Java Server!"));
    }
}
