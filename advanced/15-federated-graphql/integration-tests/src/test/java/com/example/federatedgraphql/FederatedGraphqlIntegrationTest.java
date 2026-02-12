package com.example.federatedgraphql;

import com.example.authorservice.AuthorApplication;
import com.example.bookservice.BookApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class FederatedGraphqlIntegrationTest {

    private static ConfigurableApplicationContext authorContext;
    private static ConfigurableApplicationContext bookContext;
    private static GenericContainer<?> routerContainer;
    private static GenericContainer<?> roverContainer;

    private static final String ROVER_IMAGE = "ghcr.io/apollographql/rover:v0.26.0";
    private static final String ROUTER_IMAGE = "ghcr.io/apollographql/router:v1.57.0";

    @BeforeAll
    static void setUp() throws IOException, InterruptedException {
        // 1. Start Subgraphs (Spring Boot Apps)
        authorContext = new SpringApplicationBuilder(AuthorApplication.class)
                .properties("server.port=0", "spring.jmx.enabled=false")
                .run();
        int authorPort = authorContext.getEnvironment().getProperty("local.server.port", Integer.class);

        bookContext = new SpringApplicationBuilder(BookApplication.class)
                .properties("server.port=0", "spring.jmx.enabled=false")
                .run();
        int bookPort = bookContext.getEnvironment().getProperty("local.server.port", Integer.class);

        org.testcontainers.Testcontainers.exposeHostPorts(authorPort, bookPort);

        // 2. Generate Supergraph Config
        String configContent = String.format("""
                subgraphs:
                  author:
                    routing_url: http://host.testcontainers.internal:%d/graphql
                    schema:
                      subgraph_url: http://host.testcontainers.internal:%d/graphql
                  book:
                    routing_url: http://host.testcontainers.internal:%d/graphql
                    schema:
                      subgraph_url: http://host.testcontainers.internal:%d/graphql
                """, authorPort, authorPort, bookPort, bookPort);

        Path configPath = Files.createTempFile("supergraph", ".yaml");
        Files.writeString(configPath, configContent);

        // 3. Run Rover to compose schema
        // We mount the config and a place to write the output
        Path schemaPath = Files.createTempFile("supergraph", ".graphql");
        // Ensure the file exists and is writable
        Files.writeString(schemaPath, "");
        // On Mac/Linux, temp files might have restrictive permissions. 
        // For Docker to write to it, it needs to be accessible. 
        // Simpler: Just print to stdout and capture it.

        roverContainer = new GenericContainer<>(DockerImageName.parse(ROVER_IMAGE))
                .withCommand("supergraph", "compose", "--config", "/etc/config.yaml")
                .withFileSystemBind(configPath.toAbsolutePath().toString(), "/etc/config.yaml", BindMode.READ_ONLY)
                .withLogConsumer(outputFrame -> System.out.print(outputFrame.getUtf8String()))
                .withAccessToHost(true); 
        
        roverContainer.start();
        String supergraphSchema = roverContainer.getLogs();
        roverContainer.stop();

        // Simple validation that we got a schema
        assertThat(supergraphSchema).contains("type Query");
        
        // Save the schema to a file for the Router
        Files.writeString(schemaPath, supergraphSchema);

        // 4. Start Apollo Router
        routerContainer = new GenericContainer<>(DockerImageName.parse(ROUTER_IMAGE))
                .withFileSystemBind(schemaPath.toAbsolutePath().toString(), "/etc/supergraph.graphql", BindMode.READ_ONLY)
                .withEnv("APOLLO_ROUTER_SUPERGRAPH_PATH", "/etc/supergraph.graphql")
                .withEnv("APOLLO_ROUTER_HOT_RELOAD", "true")
                .withExposedPorts(4000)
                .withAccessToHost(true)
                .waitingFor(Wait.forHttp("/health").forStatusCode(200));

        routerContainer.start();
    }

    @AfterAll
    static void tearDown() {
        if (routerContainer != null) routerContainer.stop();
        if (authorContext != null) authorContext.close();
        if (bookContext != null) bookContext.close();
    }

    @Test
    void verifyFederatedQuery() {
        Integer routerPort = routerContainer.getMappedPort(4000);
        WebTestClient.Builder clientBuilder = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + routerPort + "/graphql");

        HttpGraphQlTester tester = HttpGraphQlTester.builder(clientBuilder).build();

        // Test: Get Book with Author (spanning two services)
        String query = """
                query {
                    bookById(id: "book-1") {
                        id
                        title
                        author {
                            id
                            name
                        }
                    }
                }
                """;

        tester.document(query)
                .execute()
                .path("bookById.id").entity(String.class).isEqualTo("book-1")
                .path("bookById.title").entity(String.class).isEqualTo("Effective Java")
                .path("bookById.author.id").entity(String.class).isEqualTo("author-1")
                .path("bookById.author.name").entity(String.class).isEqualTo("Joshua Bloch");
    }
}
