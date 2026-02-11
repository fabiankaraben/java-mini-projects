package com.example.whois;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WhoisIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ServerSocket mockWhoisServer;
    private ExecutorService executorService;
    private int mockWhoisPort;

    @BeforeEach
    void setUp() throws IOException {
        mockWhoisServer = new ServerSocket(0);
        mockWhoisPort = mockWhoisServer.getLocalPort();
        executorService = Executors.newSingleThreadExecutor();
        
        executorService.submit(() -> {
            try {
                while (!mockWhoisServer.isClosed()) {
                    Socket clientSocket = mockWhoisServer.accept();
                    handleClient(clientSocket);
                }
            } catch (IOException e) {
                // Server socket closed
            }
        });
    }

    @AfterEach
    void tearDown() throws IOException {
        if (mockWhoisServer != null && !mockWhoisServer.isClosed()) {
            mockWhoisServer.close();
        }
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String line = in.readLine(); // Read domain query
            if (line != null) {
                out.println("Mock WHOIS Response for: " + line.trim());
                out.println("Domain Name: " + line.trim());
                out.println("Registry Domain ID: MOCK-12345");
                out.println("Registrar: Mock Registrar");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWhoisLookup() {
        String domain = "example.com";
        String url = String.format("http://localhost:%d/api/whois?domain=%s&server=localhost&port=%d", 
                                   port, domain, mockWhoisPort);

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("domain")).isEqualTo(domain);
        assertThat(response.getBody().get("whois_server")).isEqualTo("localhost");
        String result = (String) response.getBody().get("result");
        assertThat(result).contains("Mock WHOIS Response for: example.com");
        assertThat(result).contains("Registrar: Mock Registrar");
    }
}
