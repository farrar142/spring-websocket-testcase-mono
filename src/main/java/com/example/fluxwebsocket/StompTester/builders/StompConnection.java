package com.example.fluxwebsocket.StompTester.builders;

public class StompConnection{
    protected final String url;
    protected StompConnection(String url) {
        this.url = url;
    }
    public <T> StompSubscriber<T> subscribe(String topic,Class<T> type){
        return new StompSubscriber<>(this,topic,type);
    }

    public String getUrl() {
        return url;
    }
}