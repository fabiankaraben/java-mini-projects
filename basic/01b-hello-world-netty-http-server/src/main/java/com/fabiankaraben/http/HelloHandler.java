package com.fabiankaraben.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

public class HelloHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = Logger.getLogger(HelloHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (req.method().equals(HttpMethod.GET)) {
            logger.info(String.format("Received GET request for %s", req.uri()));
            sendResponse(ctx, HttpResponseStatus.OK, "Hello World");
        } else if (req.method().equals(HttpMethod.POST)) {
            logger.info(String.format("Received POST request for %s", req.uri()));
            sendResponse(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, "Method Not Allowed");
        } else {
            logger.info(String.format("Received %s request for %s", req.method().name(), req.uri()));
            sendResponse(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, "Method Not Allowed");
        }
    }

    private void sendResponse(ChannelHandlerContext ctx, HttpResponseStatus status, String content) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
