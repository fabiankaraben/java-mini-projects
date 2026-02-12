package com.example.reactivemicroservices.controller;

import com.example.reactivemicroservices.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
public class RSocketController {

    private static final Logger log = LoggerFactory.getLogger(RSocketController.class);

    // Request-Response
    @MessageMapping("request-response")
    public Mono<Message> requestResponse(@Payload Message request) {
        log.info("Received request-response: {}", request.getContent());
        return Mono.just(new Message("Server", "Echo: " + request.getContent()));
    }

    // Fire-and-Forget
    @MessageMapping("fire-and-forget")
    public Mono<Void> fireAndForget(@Payload Message request) {
        log.info("Received fire-and-forget: {}", request.getContent());
        return Mono.empty();
    }

    // Request-Stream
    @MessageMapping("request-stream")
    public Flux<Message> requestStream(@Payload Message request) {
        log.info("Received request-stream: {}", request.getContent());
        return Flux.interval(Duration.ofSeconds(1))
                .map(index -> new Message("Server", "Stream Response #" + index + " to " + request.getContent()))
                .take(10)
                .doOnNext(msg -> log.info("Sending: {}", msg.getContent()));
    }

    // Channel (Bi-directional stream)
    @MessageMapping("channel")
    public Flux<Message> channel(Flux<Message> requests) {
        return requests
                .doOnNext(msg -> log.info("Received channel msg: {}", msg.getContent()))
                .map(msg -> new Message("Server", "Channel Echo: " + msg.getContent()));
    }
}
