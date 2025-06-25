package com.example.fluxwebsocket.StompTester;

import com.example.fluxwebsocket.StompTester.builders.StompConnectionBuilder;
import com.example.fluxwebsocket.StompTester.builders.StompInfo;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;


@Component
public class StompTester{
    private MessageConverter defaultConverter = new MappingJackson2MessageConverter();
    public StompTester() {}

    public StompTester setConverter(MessageConverter converter) {
        this.defaultConverter = converter;
        return this;
    }
    public <T> StompResultActions<T> perform(Function<StompConnectionBuilder, StompInfo<T>> builder) throws ExecutionException, InterruptedException {
        StompInfo<T> info = builder.apply(new StompConnectionBuilder());
        StandardWebSocketClient client = new StandardWebSocketClient();
        client.setUserProperties(info.getProperties().getProperties());
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(defaultConverter);
        CompletableFuture<T> future = new CompletableFuture<>();
        StompSession stompSession = stompClient.connectAsync(
                info.getConnection().getUrl(),
                new StompSessionHandlerAdapter() {}
        ).get();
        stompSession.subscribe(
                info.getSubscriber().getTopic(),
                new TypedStompFrameHandler<>(info.getSubscriber().getType(), future));
        stompSession.send(
                info.getSender().getDestination(),
                info.getSender().getPayload()
        );
        return new StompResultActions<>(future);
    }
}



