package com.example.fluxwebsocket.StompTester.builders;

public class StompConnectionBuilder{
    public StompConnectionBuilder(){

    }
    public StompWebSocket connect(String url){
        return new StompWebSocket(url);
    }

}