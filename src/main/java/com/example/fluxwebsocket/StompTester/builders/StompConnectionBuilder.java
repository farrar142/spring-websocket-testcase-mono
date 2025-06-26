package com.example.fluxwebsocket.StompTester.builders;

import java.util.Map;

public class StompConnectionBuilder<T>{
    private WebSocketScheme scheme = WebSocketScheme.WS;
    private String host;
    private Integer port;
    private String webSocketPath;
    private Map<String,String> handshakeHeaders;
    private StompSubscription<T> subscription;
    private StompSender sender;
    private Integer timeout;

    public StompConnectionBuilder(){
        this.timeout = 5;
        this.host = "localhost";
        this.webSocketPath = "/ws";
        this.handshakeHeaders = Map.of();
    }
    public StompConnectionBuilder<T> scheme(WebSocketScheme scheme) {
        this.scheme = scheme;
        return this;
    }
    public StompConnectionBuilder<T> host(String host) {
        this.host = host;
        return this;
    }
    public StompConnectionBuilder<T> port(Integer port) {
        this.port = port;
        return this;
    }
    public StompConnectionBuilder<T> webSocketPath(String webSocketPath) {
        this.webSocketPath = webSocketPath;
        return this;
    }
    public StompConnectionBuilder<T> handshakeHeaders(Map<String,String> handshakeHeaders) {
        this.handshakeHeaders = handshakeHeaders;
        return this;
    }
    public StompConnectionBuilder<T> timeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }
    public StompConnectionBuilder<T> subscribe(String topic, Class<T> type) {
        this.subscription = new StompSubscription<>(topic, type);
        return this;
    }
    public StompConnectionBuilder<T> send(String destination, Object payload) {
        this.sender = new StompSender(destination, payload);
        return this;
    }

    public WebSocketScheme getScheme() {
        return scheme;
    }
    public String getHost() {
        return host;
    }
    public Integer getPort(){
        return port;
    }
    public String getWebSocketPath() {
        return webSocketPath;
    }

    public Integer getTimeout() {
        return timeout;
    }
    public StompSubscription<T> getSubscription() {
        return subscription;
    }
    public StompSender getSender() {
        return sender;
    }

    public Map<String, String> getHandshakeHeaders() {
        return handshakeHeaders;
    }
}