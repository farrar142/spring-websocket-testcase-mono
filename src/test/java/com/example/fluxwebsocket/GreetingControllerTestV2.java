package com.example.fluxwebsocket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreetingControllerTestV2 {

    @LocalServerPort
    private int port;


    @Test
    public void testStompSubscribeAndSend() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        CompletableFuture<Greeting> greetingFuture = new CompletableFuture<>();

        StompSession session = stompClient.connectAsync(
                "ws://localhost:" + port + "/gs-guide-websocket",
                new StompSessionHandlerAdapter() {}
        ).get();

        session.subscribe("/topic/greetings", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Greeting.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload instanceof Greeting) {
                    Greeting greeting = (Greeting) payload;
                    greetingFuture.complete(greeting);
                } else {
                    throw new IllegalArgumentException("Unexpected payload type: " + payload.getClass());
                }
            }
        });

        session.send("/app/hello", new HelloMessage("Test User"));

        Greeting greeting = greetingFuture.get(3, TimeUnit.SECONDS);
        Assertions.assertEquals(greeting.getContent(),"Hello, Test User!");

    }
}
