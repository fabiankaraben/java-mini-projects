package com.example.grpc.service;

import com.example.grpc.lib.HelloReply;
import com.example.grpc.lib.HelloRequest;
import com.example.grpc.lib.SimpleServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcServerService extends SimpleServiceGrpc.SimpleServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String greeting = "Hello, " + request.getName();
        HelloReply reply = HelloReply.newBuilder().setMessage(greeting).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
