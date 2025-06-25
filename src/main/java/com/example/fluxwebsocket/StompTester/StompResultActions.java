package com.example.fluxwebsocket.StompTester;

import com.example.fluxwebsocket.StompTester.results.StompResultHandler;
import com.example.fluxwebsocket.StompTester.results.StompResultMatcher;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class StompResultActions<T>{
    private final CompletableFuture<T> future;
    public StompResultActions(CompletableFuture<T> future){
        this.future = future;
    }
    public StompResultActions<T> andDo(StompResultHandler handler) throws ExecutionException, InterruptedException {
        handler.apply(this);
        return this;
    }

    public StompResultActions<T> andExpect(StompResultMatcher handler) throws ExecutionException, InterruptedException, TimeoutException {
        handler.apply(this);
        return this;
    }
    public T andReturn() throws ExecutionException, InterruptedException {
        return this.future.get();
    }
    public T andReturn(Integer timeout, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.future.get(timeout,timeUnit);
    }
}

