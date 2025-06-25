package com.example.fluxwebsocket.StompTester.builders;


public class StompSubscriber<T>{
    protected final StompWebSocket connection;
    protected final String topic;
    protected final Class<T> type;
    protected final StompUserProperties userProperties;

    public StompSubscriber(StompUserProperties userProperties, StompWebSocket connection, String topic, Class<T> type) {
        this.connection = connection;
        this.topic = topic;
        this.type = type;
        this.userProperties = userProperties;
    }
    public StompInfo<T> send(String destination, Object payload) {
        StompSender sender = new StompSender(destination, payload);
        return new StompInfo<T>(this.userProperties,this.connection,this,sender);
    }
    public StompWebSocket getConnection() {
        return connection;
    }
    public String getTopic() {
        return topic;
    }
    public Class<T> getType() {
        return type;
    }
}