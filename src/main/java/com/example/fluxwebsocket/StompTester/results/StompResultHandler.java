package com.example.fluxwebsocket.StompTester.results;

import com.example.fluxwebsocket.StompTester.StompResultActions;

import java.util.concurrent.ExecutionException;

public interface StompResultHandler {
    <T> void apply(StompResultActions<T> actions) throws ExecutionException, InterruptedException;
}
