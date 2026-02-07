package com.fabiankaraben.basicrouter;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouterTest {

    private Router router;
    private StubHttpExchange exchange;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        router = new Router();
        outputStream = new ByteArrayOutputStream();
        exchange = new StubHttpExchange(outputStream);
    }

    @Test
    void testRouteFound() throws IOException {
        router.addRoute("GET", "/test", ex -> {
            ex.sendResponseHeaders(200, 0);
        });

        exchange.setRequestURI(URI.create("/test"));
        exchange.setRequestMethod("GET");

        router.handle(exchange);

        assertEquals(200, exchange.getResponseCode());
    }

    @Test
    void testNotFound() throws IOException {
        exchange.setRequestURI(URI.create("/unknown"));
        exchange.setRequestMethod("GET");

        router.handle(exchange);

        assertEquals(404, exchange.getResponseCode());
    }

    @Test
    void testMethodNotAllowed() throws IOException {
        router.addRoute("GET", "/test", ex -> {});

        exchange.setRequestURI(URI.create("/test"));
        exchange.setRequestMethod("POST");

        router.handle(exchange);

        assertEquals(405, exchange.getResponseCode());
    }

    // Manual Stub for HttpExchange to avoid Mockito/ByteBuddy issues with Java 25
    static class StubHttpExchange extends HttpExchange {
        private final OutputStream responseBody;
        private URI requestURI;
        private String requestMethod;
        private int responseCode = -1;

        public StubHttpExchange(OutputStream responseBody) {
            this.responseBody = responseBody;
        }

        public void setRequestURI(URI requestURI) {
            this.requestURI = requestURI;
        }

        public void setRequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
        }

        @Override
        public URI getRequestURI() {
            return requestURI;
        }

        @Override
        public String getRequestMethod() {
            return requestMethod;
        }

        @Override
        public OutputStream getResponseBody() {
            return responseBody;
        }

        @Override
        public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
            this.responseCode = rCode;
        }

        @Override
        public int getResponseCode() {
            return responseCode;
        }

        // Unused methods implementation
        @Override public Headers getRequestHeaders() { return null; }
        @Override public Headers getResponseHeaders() { return null; }
        @Override public void close() {}
        @Override public InputStream getRequestBody() { return null; }
        @Override public InetSocketAddress getRemoteAddress() { return null; }
        @Override public InetSocketAddress getLocalAddress() { return null; }
        @Override public String getProtocol() { return null; }
        @Override public Object getAttribute(String name) { return null; }
        @Override public void setAttribute(String name, Object value) {}
        @Override public void setStreams(InputStream i, OutputStream o) {}
        @Override public HttpPrincipal getPrincipal() { return null; }
        @Override public HttpContext getHttpContext() { return null; }
    }
}
