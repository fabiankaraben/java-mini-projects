package com.fabiankaraben.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelloHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private HelloHandler handler;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        handler = new HelloHandler();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    void doGet_ShouldReturnHelloWorld() throws IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/");
        when(response.getWriter()).thenReturn(printWriter);

        // Act
        handler.doGet(request, response);

        // Assert
        verify(response).setContentType("text/plain; charset=UTF-8");
        assertEquals("Hello World", stringWriter.toString());
    }

    @Test
    void doPost_ShouldReturnMethodNotAllowed() throws IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/");
        when(response.getWriter()).thenReturn(printWriter);

        // Act
        handler.doPost(request, response);

        // Assert
        verify(response).setStatus(405);
        verify(response).setContentType("text/plain; charset=UTF-8");
        assertEquals("Method Not Allowed", stringWriter.toString());
    }
}
