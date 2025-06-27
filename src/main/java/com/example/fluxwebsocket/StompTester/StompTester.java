package com.example.fluxwebsocket.StompTester;

import com.example.fluxwebsocket.StompTester.builders.StompConnectionBuilder;
import com.example.fluxwebsocket.StompTester.builders.StompSender;
import com.example.fluxwebsocket.StompTester.builders.StompSubscription;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.util.UriBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Component
public class StompTester{
    private MessageConverter defaultConverter = new MappingJackson2MessageConverter();
    public StompTester() {}

    public StompTester setConverter(MessageConverter converter) {
        this.defaultConverter = converter;
        return this;
    }
    public <T> StompResultActions<T> perform(StompConnectionBuilder<T> builder) throws ExecutionException, InterruptedException {
        StandardWebSocketClient client = new StandardWebSocketClient();

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        if (builder.getHandshakeHeaders() != null) {
            for (String key : builder.getHandshakeHeaders().keySet())
                headers.add(key, builder.getHandshakeHeaders().get(key));
        }


        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(defaultConverter);

        CompletableFuture<T> future = new CompletableFuture<>();

        String url = builder.getScheme().getScheme()
                + "://"
                + builder.getHost()
                + ":"
                + builder.getPort()
                + builder.getWebSocketPath();


        StompSession stompSession = stompClient.connectAsync(
                url,
                headers,
                new StompSessionHandlerAdapter() {}
        ).get();

        StompSubscription<T> subscription = builder.getSubscription();
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription must be provided");
        }
        stompSession.subscribe(
                subscription.getTopic(),
                new TypedStompFrameHandler<>(subscription.getType(), future));

        StompSender stompSender = builder.getSender();
        if (stompSender != null){
            stompSession.send(
                    stompSender.getDestination(),
                    stompSender.getPayload()
            );
        }
        return new StompResultActions<T>(future,builder.getTimeout());
    }
}



