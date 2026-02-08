package com.example.grpc.server;

import com.example.grpc.GreeterGrpc;
import com.example.grpc.HelloReply;
import com.example.grpc.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrpcServerTest {
    private Server server;
    private ManagedChannel channel;
    private String serverName;

    @BeforeEach
    void setUp() throws IOException {
        // Generate a unique in-process server name.
        serverName = InProcessServerBuilder.generateName();

        // Create a server, add service, start, and register for automatic graceful shutdown.
        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new GreeterImpl())
                .build()
                .start();

        // Create a client channel and register for automatic graceful shutdown.
        channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        if (channel != null) {
            channel.shutdown();
            channel.awaitTermination(5, TimeUnit.SECONDS);
        }
        if (server != null) {
            server.shutdown();
            server.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Test
    void testSayHello() {
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);
        String name = "Test User";
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();

        HelloReply reply = blockingStub.sayHello(request);

        assertEquals("Hello, " + name, reply.getMessage());
    }
}
