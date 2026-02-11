package com.example.netty;

import com.example.netty.protocol.Message;
import com.example.netty.protocol.MessageDecoder;
import com.example.netty.protocol.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class NettyServerIntegrationTest {

    private static NettyServer server;
    private static final int PORT = 8081;

    @BeforeAll
    static void startServer() throws InterruptedException {
        server = new NettyServer(PORT);
        server.start();
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void testPingPong() throws InterruptedException {
        TestClient client = new TestClient();
        try {
            client.connect(PORT);
            Message response = client.send(new Message(Message.PING, new byte[0]));
            
            assertThat(response).isNotNull();
            assertThat(response.getType()).isEqualTo(Message.PONG);
            assertThat(response.getContent()).isEmpty();
        } finally {
            client.close();
        }
    }

    @Test
    void testEcho() throws InterruptedException {
        TestClient client = new TestClient();
        try {
            client.connect(PORT);
            String text = "Hello Netty!";
            Message response = client.send(new Message(Message.ECHO, text));
            
            assertThat(response).isNotNull();
            assertThat(response.getType()).isEqualTo(Message.ECHO_REPLY);
            assertThat(response.getContentAsString()).isEqualTo(text);
        } finally {
            client.close();
        }
    }

    @Test
    void testReverse() throws InterruptedException {
        TestClient client = new TestClient();
        try {
            client.connect(PORT);
            String text = "Netty";
            Message response = client.send(new Message(Message.REVERSE, text));
            
            assertThat(response).isNotNull();
            assertThat(response.getType()).isEqualTo(Message.REVERSE_REPLY);
            assertThat(response.getContentAsString()).isEqualTo("ytteN");
        } finally {
            client.close();
        }
    }

    static class TestClient {
        private EventLoopGroup group;
        private Channel channel;
        private final BlockingQueue<Message> responses = new LinkedBlockingQueue<>();

        void connect(int port) throws InterruptedException {
            group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) {
                     ch.pipeline().addLast(new MessageDecoder(), new MessageEncoder(), new SimpleChannelInboundHandler<Message>() {
                         @Override
                         protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
                             responses.offer(msg);
                         }
                     });
                 }
             });

            channel = b.connect("localhost", port).sync().channel();
        }

        Message send(Message msg) throws InterruptedException {
            channel.writeAndFlush(msg).sync();
            return responses.poll(5, TimeUnit.SECONDS);
        }

        void close() {
            if (group != null) {
                group.shutdownGracefully();
            }
        }
    }
}
