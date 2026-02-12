package com.example.reactivemicroservices.controller;

import com.example.reactivemicroservices.model.Message;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api")
public class WebRestController {

    private final RSocketRequester rSocketRequester;

    public WebRestController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @PostMapping("/request-response")
    public Mono<Message> requestResponse(@RequestBody Message message) {
        return rSocketRequester
                .route("request-response")
                .data(message)
                .retrieveMono(Message.class);
    }

    @PostMapping("/fire-and-forget")
    public Mono<Void> fireAndForget(@RequestBody Message message) {
        return rSocketRequester
                .route("fire-and-forget")
                .data(message)
                .send();
    }

    @PostMapping(value = "/request-stream", produces = "text/event-stream")
    public Flux<Message> requestStream(@RequestBody Message message) {
        return rSocketRequester
                .route("request-stream")
                .data(message)
                .retrieveFlux(Message.class);
    }

    @PostMapping(value = "/channel", produces = "text/event-stream")
    public Flux<Message> channel(@RequestBody Message message) {
        // Simulating a stream of messages from the client side
        Flux<Message> input = Flux.interval(Duration.ofMillis(500))
                .map(i -> new Message(message.getSender(), message.getContent() + " " + i))
                .take(5);

        return rSocketRequester
                .route("channel")
                .data(input)
                .retrieveFlux(Message.class);
    }
}
