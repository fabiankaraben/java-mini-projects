package com.example.sandbox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SandboxIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CodeExecutionService codeExecutionService;

    @Test
    void shouldExecuteValidCode() {
        // Ensure image exists before test (optional, service handles it but good for stability)
        codeExecutionService.pullImage();

        CodeExecutionRequest request = new CodeExecutionRequest();
        request.setCode("print('Hello, Sandbox!')");
        request.setLanguage("python");

        ResponseEntity<CodeExecutionResponse> response = restTemplate.postForEntity(
                "/api/sandbox/execute", request, CodeExecutionResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStdout().trim()).isEqualTo("Hello, Sandbox!");
        assertThat(response.getBody().getStderr()).isEmpty();
        assertThat(response.getBody().getExitCode()).isEqualTo(0);
        assertThat(response.getBody().isTimeout()).isFalse();
    }

    @Test
    void shouldHandleInfiniteLoop() {
        codeExecutionService.pullImage();

        CodeExecutionRequest request = new CodeExecutionRequest();
        request.setCode("while True: pass");
        request.setLanguage("python");

        ResponseEntity<CodeExecutionResponse> response = restTemplate.postForEntity(
                "/api/sandbox/execute", request, CodeExecutionResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isTimeout()).isTrue();
    }

    @Test
    void shouldPreventNetworkAccess() {
        codeExecutionService.pullImage();

        CodeExecutionRequest request = new CodeExecutionRequest();
        // Try to connect to google.com
        request.setCode("import socket; s = socket.create_connection(('google.com', 80), timeout=2)");
        request.setLanguage("python");

        ResponseEntity<CodeExecutionResponse> response = restTemplate.postForEntity(
                "/api/sandbox/execute", request, CodeExecutionResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        // Should fail with an error (stderr) or exit code != 0
        assertThat(response.getBody().getExitCode()).isNotEqualTo(0);
        assertThat(response.getBody().getStderr()).isNotEmpty();
    }
}
