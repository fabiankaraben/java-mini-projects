package com.example.loadbalancer;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class LoadBalancerIntegrationTest {

    @Test
    void testRoundRobinDistribution() throws Exception {
        int lbPort = 9090;
        int backend1Port = 9091;
        int backend2Port = 9092;

        // Start Mock Backend Servers
        MockBackendServer server1 = new MockBackendServer(backend1Port, "Server1");
        MockBackendServer server2 = new MockBackendServer(backend2Port, "Server2");
        
        new Thread(server1::start).start();
        new Thread(server2::start).start();

        // Give backends a moment to start
        TimeUnit.SECONDS.sleep(1);

        // Start Load Balancer
        List<String> backends = Arrays.asList("localhost:" + backend1Port, "localhost:" + backend2Port);
        LoadBalancerApplication lb = new LoadBalancerApplication(lbPort, backends);
        
        Thread lbThread = new Thread(() -> {
            try {
                lb.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        lbThread.start();

        // Give LB a moment to start
        TimeUnit.SECONDS.sleep(1);

        // Send requests
        int totalRequests = 10;
        AtomicInteger server1Hits = new AtomicInteger(0);
        AtomicInteger server2Hits = new AtomicInteger(0);

        for (int i = 0; i < totalRequests; i++) {
            String response = sendRequest("localhost", lbPort);
            if (response.contains("Server1")) {
                server1Hits.incrementAndGet();
            } else if (response.contains("Server2")) {
                server2Hits.incrementAndGet();
            }
        }

        System.out.println("Server 1 Hits: " + server1Hits.get());
        System.out.println("Server 2 Hits: " + server2Hits.get());

        // Verify distribution (exact 50/50 for RR with even requests)
        assertThat(server1Hits.get()).isEqualTo(5);
        assertThat(server2Hits.get()).isEqualTo(5);

        // Cleanup (simplification: interrupting threads or groups would be cleaner but this suffices for a mini-project test)
        // In a real app we'd expose shutdown methods.
    }

    private String sendRequest(String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        final StringBuilder responseContent = new StringBuilder();
        CountDownLatch latch = new CountDownLatch(1);

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) {
                     ch.pipeline().addLast(new HttpClientCodec());
                     ch.pipeline().addLast(new HttpObjectAggregator(1048576));
                     ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
                         @Override
                         protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {
                             responseContent.append(msg.content().toString(CharsetUtil.UTF_8));
                             latch.countDown();
                             ctx.close();
                         }

                         @Override
                         public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                             cause.printStackTrace();
                             ctx.close();
                             latch.countDown();
                         }
                     });
                 }
             });

            Channel ch = b.connect(host, port).sync().channel();
            
            HttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            
            ch.writeAndFlush(request);
            
            latch.await(5, TimeUnit.SECONDS);
        } finally {
            group.shutdownGracefully();
        }
        return responseContent.toString();
    }

    static class MockBackendServer {
        private final int port;
        private final String responseBody;

        MockBackendServer(int port, String responseBody) {
            this.port = port;
            this.responseBody = responseBody;
        }

        void start() {
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     public void initChannel(SocketChannel ch) {
                         ch.pipeline().addLast(new HttpServerCodec());
                         ch.pipeline().addLast(new HttpObjectAggregator(1048576));
                         ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                             @Override
                             protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
                                 FullHttpResponse response = new DefaultFullHttpResponse(
                                     HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                                     Unpooled.copiedBuffer(responseBody, CharsetUtil.UTF_8));
                                 response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                 response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                                 
                                 ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                             }
                         });
                     }
                 });

                b.bind(port).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    }
}
