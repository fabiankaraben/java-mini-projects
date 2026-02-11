package com.example.loadbalancer;

import com.example.loadbalancer.strategy.LoadBalancingStrategy;
import com.example.loadbalancer.strategy.RoundRobinStrategy;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class LoadBalancerApplication {

    private static final Logger logger = LoggerFactory.getLogger(LoadBalancerApplication.class);

    private final int port;
    private final List<String> backendServers;

    public LoadBalancerApplication(int port, List<String> backendServers) {
        this.port = port;
        this.backendServers = backendServers;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        LoadBalancingStrategy strategy = new RoundRobinStrategy();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) {
                     ch.pipeline().addLast(
                         new LoggingHandler(LogLevel.INFO),
                         new FrontendHandler(backendServers, strategy)
                     );
                 }
             })
             .childOption(ChannelOption.AUTO_READ, false);

            ChannelFuture f = b.bind(port).sync();
            logger.info("Load Balancer started on port {}", port);
            logger.info("Forwarding to backends: {}", backendServers);

            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        // Example backends, in a real scenario these might be configurable
        List<String> backends = Arrays.asList("localhost:8081", "localhost:8082");
        
        // Allow overriding via env vars
        String portEnv = System.getenv("LB_PORT");
        if (portEnv != null) {
            port = Integer.parseInt(portEnv);
        }
        
        String backendsEnv = System.getenv("BACKEND_SERVERS");
        if (backendsEnv != null) {
            backends = Arrays.asList(backendsEnv.split(","));
        }

        new LoadBalancerApplication(port, backends).start();
    }
}
