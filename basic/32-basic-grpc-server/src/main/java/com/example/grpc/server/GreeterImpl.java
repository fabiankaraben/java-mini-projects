package com.example.grpc.server;

import com.example.grpc.GreeterGrpc;
import com.example.grpc.HelloReply;
import com.example.grpc.HelloRequest;
import io.grpc.stub.StreamObserver;

public class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String message = "Hello, " + request.getName();
        HelloReply reply = HelloReply.newBuilder().setMessage(message).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
