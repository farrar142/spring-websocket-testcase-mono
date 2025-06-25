package com.example.fluxwebsocket.StompTester.builders;

import java.util.Map;

public class StompUserProperties {
    private StompWebSocket stompWebSocket;
    private Map<String, Object> properties;
    public StompUserProperties(StompWebSocket stompWebSocket, Map<String,Object> properties){
        this.stompWebSocket = stompWebSocket;
        this.properties = properties;
    }
    public <T> StompSubscriber<T> subscribe(String topic, Class<T> type) {
        return new StompSubscriber<>(this,this.stompWebSocket,topic,type);
    }
    public StompWebSocket getStompWebSocket() {
        return stompWebSocket;
    }
    public Map<String, Object> getProperties() {
        return properties;
    }
}
