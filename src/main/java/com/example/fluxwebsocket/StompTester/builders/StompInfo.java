package com.example.fluxwebsocket.StompTester.builders;

public class StompInfo<T>{
    protected final StompWebSocket connection;
    protected final StompSender sender;
    protected final StompSubscriber<T> subscriber;
    protected final StompUserProperties properties;

    protected StompInfo(StompUserProperties properties,StompWebSocket connection, StompSubscriber<T> subscriber, StompSender sender) {
        this.properties = properties;
        this.connection = connection;
        this.sender = sender;
        this.subscriber = subscriber;
    }
    public  StompUserProperties getProperties() {
        return properties;
    }
    public StompWebSocket getConnection() {
        return connection;
    }
    public StompSender getSender() {
        return sender;
    }
    public StompSubscriber<T> getSubscriber() {
        return subscriber;
    }

}