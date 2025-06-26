package com.example.fluxwebsocket.StompTester.builders;

public class StompSender {
    private String destination;
    private Object payload;

    public StompSender(String destination, Object payload) {
        this.destination = destination;
        this.payload = payload;
    }

    public String getDestination() {
        return destination;
    }

    public Object getPayload() {
        return payload;
    }
}