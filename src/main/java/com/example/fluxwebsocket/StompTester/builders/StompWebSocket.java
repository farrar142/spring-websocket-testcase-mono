package com.example.fluxwebsocket.StompTester.builders;

import java.util.HashMap;
import java.util.Map;

public class StompWebSocket {
    protected final String url;
    protected StompWebSocket(String url) {
        this.url = url;
    }
    public <T> StompSubscriber<T> subscribe(String topic,Class<T> type){
        return new StompUserProperties(this,new HashMap<>()).subscribe(topic,type);
    }

    public StompUserProperties userProperties(Map<String,Object> properties) {
        return new StompUserProperties(this,properties);
    }

    public String getUrl() {
        return url;
    }
}