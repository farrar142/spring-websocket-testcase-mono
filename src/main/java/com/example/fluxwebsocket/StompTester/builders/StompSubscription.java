package com.example.fluxwebsocket.StompTester.builders;

public class StompSubscription<T> {
    private String topic;
    private Class<T> type;

    public StompSubscription(String topic, Class<T> type) {
        this.topic = topic;
        this.type = type;
    }
    public String getTopic() {
        return topic;
    }
    public Class<T> getType() {
        return type;
    }
}
