package com.example.grpc;

import com.example.grpc.lib.HelloReply;
import com.example.grpc.lib.HelloRequest;
import com.example.grpc.lib.SimpleServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
    "grpc.server.in-process-name=test",
    "grpc.server.port=-1",
    "grpc.client.testClient.address=in-process:test"
})
@DirtiesContext
class GrpcDemoApplicationTests {

    @GrpcClient("testClient")
    private SimpleServiceGrpc.SimpleServiceBlockingStub simpleServiceStub;

    @Test
    void testSayHello() {
        assertNotNull(simpleServiceStub, "gRPC Stub should not be null");
        
        HelloRequest request = HelloRequest.newBuilder().setName("World").build();
        HelloReply reply = simpleServiceStub.sayHello(request);
        
        assertEquals("Hello, World", reply.getMessage());
    }
}
