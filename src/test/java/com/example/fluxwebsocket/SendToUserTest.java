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

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.*;
//인증 정보(헤더,세션, 값)
//메서드 체이닝
//
class CustomWebSocketStompClient<T>{
    StompSession session;
    BlockingQueue<Greeting> blockingQueue;
    public CustomWebSocketStompClient(int port) throws ExecutionException, InterruptedException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        blockingQueue = new LinkedBlockingQueue<>();

        session = stompClient.connectAsync(
                "ws://localhost:" + port + "/gs-guide-websocket",
                new StompSessionHandlerAdapter() {}
        ).get();
    }

    public CustomWebSocketStompClient subscribe(String topic){
        session.subscribe(topic, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Greeting.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload instanceof Greeting) {
                    Greeting greeting = (Greeting) payload;
                    blockingQueue.offer(greeting);
                } else {
                    throw new IllegalArgumentException("Unexpected payload type: " + payload.getClass());
                }
            }
        });
        return this;
    }
    public CustomWebSocketStompClient sendMessage(String destination, HelloMessage message) {
        session.send(destination, message);
        return this;
    }
    public Optional<Greeting> receiveMessage() throws InterruptedException {
        return Optional.ofNullable(blockingQueue.poll(1, TimeUnit.SECONDS));
    }
}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendToUserTest {

    @LocalServerPort
    private int port;


    @Test
    public void testStompSubscribeAndSend() throws Exception {
        CustomWebSocketStompClient client1 = new CustomWebSocketStompClient(port);
        CustomWebSocketStompClient client2 = new CustomWebSocketStompClient(port);
        client1.subscribe("/user/topic/greetings");
        client2.subscribe("/user/topic/greetings");
        client1.sendMessage("/app/sendtouser", new HelloMessage("User1"));
        Assertions.assertTrue(client2.receiveMessage().isEmpty());
        Assertions.assertFalse(client1.receiveMessage().isEmpty());
        client2.sendMessage("/app/sendtouser", new HelloMessage("User2"));
        Assertions.assertTrue(client1.receiveMessage().isEmpty());
        Assertions.assertFalse(client2.receiveMessage().isEmpty());

    }
}
