package com.example.fluxwebsocket;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void testStompSubscribeAndSend() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();

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
                    blockingQueue.offer(greeting.getContent());
                } else {
                    throw new IllegalArgumentException("Unexpected payload type: " + payload.getClass());
                }
            }
        });

        session.send("/app/hello", new HelloMessage("Test User"));
        System.out.println("done");
        StepVerifier.create(Mono.fromCallable(blockingQueue::take))
            .expectNext("Hello, Test User!")
            .expectComplete()
            .verify();
    }
}
