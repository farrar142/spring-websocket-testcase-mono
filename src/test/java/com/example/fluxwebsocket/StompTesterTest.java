package com.example.fluxwebsocket;


import com.example.fluxwebsocket.StompTester.StompTester;
import com.example.fluxwebsocket.StompTester.results.StompResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StompTesterTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StompTester stompTester;

    @Test
    public void test() throws Exception {
        stompTester.perform(
            builder->
                    builder.connect("ws://localhost:" + port + "/gs-guide-websocket")
                    .userProperties(new HashMap<>())
                    .subscribe("/topic/greetings", Greeting.class)
                    .send("/app/hello", new HelloMessage("Test User"))
        )
            .andExpect(StompResult.isReceived(3, TimeUnit.SECONDS))
            .andDo(StompResult.print())
            .andExpect(StompResult.isEquals(new Greeting("Hello, Test User!")))
            .andReturn();

    }
}
