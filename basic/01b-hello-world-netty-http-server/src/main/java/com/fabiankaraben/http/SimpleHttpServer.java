package com.fabiankaraben.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

public class SimpleHttpServer {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(SimpleHttpServer.class.getName());
    private static final int PORT = 8080;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;
    private int actualPort;

    public void start() throws Exception {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new HttpObjectAggregator(65536));
                        ch.pipeline().addLast(new HelloHandler());
                    }
                });

        channel = b.bind(PORT).sync().channel();
        actualPort = ((InetSocketAddress) channel.localAddress()).getPort();
        logger.info("Server started on port " + actualPort);
    }

    public void stop() throws Exception {
        if (channel != null) {
            channel.close().sync();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        logger.info("Server stopped");
    }

    public static void main(String[] args) {
        SimpleHttpServer httpServer = new SimpleHttpServer();
        try {
            httpServer.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Stopping server...");
                try {
                    httpServer.stop();
                } catch (Exception e) {
                    logger.severe("Error stopping server: " + e.getMessage());
                }
            }));

            httpServer.channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("Server interrupted");
        } catch (Exception e) {
            logger.severe("Failed to start server: " + e.getMessage());
        } finally {
            try {
                httpServer.stop();
            } catch (Exception e) {
                logger.severe("Error stopping server during finally block: " + e.getMessage());
            }
        }
    }

    public int getPort() {
        return actualPort > 0 ? actualPort : PORT;
    }
}
