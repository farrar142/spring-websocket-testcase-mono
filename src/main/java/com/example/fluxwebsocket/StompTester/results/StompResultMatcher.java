package com.example.fluxwebsocket.StompTester.results;

import com.example.fluxwebsocket.StompTester.StompResultActions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface StompResultMatcher<T> {
    StompResultMatcher<T> apply(StompResultActions<T> actions) throws ExecutionException, InterruptedException, TimeoutException;
}
