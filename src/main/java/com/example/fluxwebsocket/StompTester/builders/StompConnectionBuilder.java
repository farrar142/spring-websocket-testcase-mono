package com.example.fluxwebsocket.StompTester.builders;

public class StompConnectionBuilder{
    public StompConnectionBuilder(){

    }
    public StompConnection connect(String url){
        return new StompConnection(url);
    }

}