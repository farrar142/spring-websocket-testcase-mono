package com.example.fluxwebsocket.StompTester.builders;

public class StompSender{
    protected final String destination;
    protected final Object payload;

    protected StompSender(String destination, Object payload) {
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