package com.example.reactivemicroservices;

import com.example.reactivemicroservices.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rsocket.server.LocalRSocketServerPort;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.rsocket.server.port=0")
class ReactiveMicroservicesApplicationTests {

    @Autowired
    private RSocketRequester.Builder builder;

    @LocalRSocketServerPort
    private int port;

    private RSocketRequester requester;

    @BeforeEach
    void setup() {
        requester = builder
                .tcp("localhost", port);
    }

    @Test
    void testRequestResponse() {
        Mono<Message> result = requester.route("request-response")
                .data(new Message("Client", "Hello Request-Response"))
                .retrieveMono(Message.class);

        StepVerifier.create(result)
                .assertNext(message -> {
                    assertThat(message.getSender()).isEqualTo("Server");
                    assertThat(message.getContent()).isEqualTo("Echo: Hello Request-Response");
                })
                .verifyComplete();
    }

    @Test
    void testFireAndForget() {
        Mono<Void> result = requester.route("fire-and-forget")
                .data(new Message("Client", "Hello Fire-and-Forget"))
                .send();

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testRequestStream() {
        Flux<Message> result = requester.route("request-stream")
                .data(new Message("Client", "Stream Me"))
                .retrieveFlux(Message.class);

        StepVerifier.create(result)
                .expectNextMatches(msg -> msg.getContent().contains("Stream Response #0"))
                .expectNextMatches(msg -> msg.getContent().contains("Stream Response #1"))
                .expectNextMatches(msg -> msg.getContent().contains("Stream Response #2"))
                .thenCancel()
                .verify();
    }

    @Test
    void testChannel() {
        Flux<Message> input = Flux.interval(Duration.ofMillis(100))
                .map(i -> new Message("Client", "Channel Request #" + i))
                .take(3);

        Flux<Message> result = requester.route("channel")
                .data(input)
                .retrieveFlux(Message.class);

        StepVerifier.create(result)
                .expectNextMatches(msg -> msg.getContent().contains("Channel Echo: Channel Request #0"))
                .expectNextMatches(msg -> msg.getContent().contains("Channel Echo: Channel Request #1"))
                .expectNextMatches(msg -> msg.getContent().contains("Channel Echo: Channel Request #2"))
                .verifyComplete();
    }
}
