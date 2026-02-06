package com.fabiankaraben.http;

import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelloHandlerTest {

    @Mock
    private HttpExchange exchange;

    private HelloHandler handler;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        handler = new HelloHandler();
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void handle_ShouldReturnHelloWorld_WhenMethodIsGet() throws IOException {
        // Arrange
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(URI.create("/"));
        when(exchange.getResponseBody()).thenReturn(outputStream);

        // Act
        handler.handle(exchange);

        // Assert
        verify(exchange).sendResponseHeaders(200, "Hello World".length());
        assertEquals("Hello World", outputStream.toString());
    }

    @Test
    void handle_ShouldReturnMethodNotAllowed_WhenMethodIsNotGet() throws IOException {
        // Arrange
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestURI()).thenReturn(URI.create("/"));
        when(exchange.getResponseBody()).thenReturn(outputStream);

        // Act
        handler.handle(exchange);

        // Assert
        verify(exchange).sendResponseHeaders(405, "Method Not Allowed".length());
        assertEquals("Method Not Allowed", outputStream.toString());
    }
}
