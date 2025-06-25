package com.example.fluxwebsocket.StompTester.builders;

public class StompInfo<T>{
    protected final StompConnection connection;
    protected final StompSender sender;
    protected final StompSubscriber<T> subscriber;
    protected StompInfo(StompConnection connection,StompSubscriber<T> subscriber,StompSender sender) {
        this.connection = connection;
        this.sender = sender;
        this.subscriber = subscriber;
    }
    public StompConnection getConnection() {
        return connection;
    }
    public StompSender getSender() {
        return sender;
    }
    public StompSubscriber<T> getSubscriber() {
        return subscriber;
    }

}