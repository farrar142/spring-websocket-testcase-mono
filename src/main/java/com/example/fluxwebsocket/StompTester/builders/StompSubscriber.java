package com.example.fluxwebsocket.StompTester.builders;


public class StompSubscriber<T>{
    protected final StompConnection connection;
    protected final String topic;
    protected final Class<T> type;

    public StompSubscriber(StompConnection connection,String topic, Class<T> type) {
        this.connection = connection;
        this.topic = topic;
        this.type = type;
    }
    public StompInfo<T> send(String destination, Object payload) {
        StompSender sender = new StompSender(destination, payload);
        return new StompInfo<T>(this.connection,this,sender);
    }
    public StompConnection getConnection() {
        return connection;
    }
    public String getTopic() {
        return topic;
    }
    public Class<T> getType() {
        return type;
    }
}