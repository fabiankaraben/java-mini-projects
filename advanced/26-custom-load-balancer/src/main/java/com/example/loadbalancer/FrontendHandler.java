package com.example.loadbalancer;

import com.example.loadbalancer.strategy.LoadBalancingStrategy;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

public class FrontendHandler extends ChannelInboundHandlerAdapter {

    private final List<String> backendUrls;
    private final LoadBalancingStrategy strategy;
    private Channel outboundChannel;

    public FrontendHandler(List<String> backendUrls, LoadBalancingStrategy strategy) {
        this.backendUrls = backendUrls;
        this.strategy = strategy;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel inboundChannel = ctx.channel();

        // Select backend
        String backend = strategy.getNextBackend(backendUrls);
        // Assuming backend format host:port
        String[] parts = backend.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        // Start the connection attempt.
        Bootstrap b = new Bootstrap();
        b.group(inboundChannel.eventLoop())
         .channel(NioSocketChannel.class)
         .handler(new BackendHandler(inboundChannel))
         .option(ChannelOption.AUTO_READ, false);

        ChannelFuture f = b.connect(host, port);
        outboundChannel = f.channel();
        f.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // Connection complete start to read first data
                inboundChannel.read();
            } else {
                // Close the connection if the connection attempt has failed.
                inboundChannel.close();
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (outboundChannel.isActive()) {
            outboundChannel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    // was able to flush out data, start to read the next chunk
                    ctx.channel().read();
                } else {
                    future.channel().close();
                }
            });
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (outboundChannel != null) {
            BackendHandler.closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        BackendHandler.closeOnFlush(ctx.channel());
    }
}
