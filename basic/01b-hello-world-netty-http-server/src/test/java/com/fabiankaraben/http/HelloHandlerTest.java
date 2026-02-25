package com.fabiankaraben.http;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloHandlerTest {

    private EmbeddedChannel channel;

    @BeforeEach
    void setUp() {
        channel = new EmbeddedChannel(new HelloHandler());
    }

    @Test
    void doGet_ShouldReturnHelloWorld() {
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "/");

        channel.writeInbound(request);

        FullHttpResponse response = channel.readOutbound();

        assertEquals(HttpResponseStatus.OK, response.status());
        assertEquals("text/plain; charset=UTF-8", response.headers().get("Content-Type"));
        assertEquals("Hello World", response.content().toString(CharsetUtil.UTF_8));
    }

    @Test
    void doPost_ShouldReturnMethodNotAllowed() {
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.POST, "/");

        channel.writeInbound(request);

        FullHttpResponse response = channel.readOutbound();

        assertEquals(HttpResponseStatus.METHOD_NOT_ALLOWED, response.status());
        assertEquals("text/plain; charset=UTF-8", response.headers().get("Content-Type"));
        assertEquals("Method Not Allowed", response.content().toString(CharsetUtil.UTF_8));
    }
}
