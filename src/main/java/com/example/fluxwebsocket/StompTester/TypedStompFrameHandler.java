package com.example.fluxwebsocket.StompTester;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class TypedStompFrameHandler<T> implements StompFrameHandler {
    private final Class<T> type;
    private final CompletableFuture<T> future;
    public TypedStompFrameHandler(Class<T> type, CompletableFuture<T> future) {
        this.type = type;
        this.future = future;
    }
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return this.type;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("Received payload: " + payload);
        if (payload.getClass().equals(this.type)) {
            T data = (T) payload;
            future.complete(data);
        } else {
            throw new IllegalArgumentException("Unexpected payload type: " + payload.getClass());
        }
    }
}
