package com.example.fluxwebsocket.StompTester;

import com.example.fluxwebsocket.StompTester.results.StompResultHandler;
import com.example.fluxwebsocket.StompTester.results.StompResultMatcher;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ObjectAssert;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class StompResultActions<T>{
    private final CompletableFuture<T> future;
    private final Integer timeout;
    public StompResultActions(CompletableFuture<T> future, Integer timeout){
        this.future = future;
        this.timeout = timeout;
    }
    public StompResultActions<T> andDo(StompResultHandler handler) throws ExecutionException, InterruptedException, TimeoutException {
        handler.apply(this);
        return this;
    }

    public StompResultActions<T> andExpect(StompResultMatcher<T> handler) throws ExecutionException, InterruptedException, TimeoutException {
        handler.apply(this);
        return this;
    }
    public <U extends ObjectAssert<T>>
    StompResultActions<T> andAssert(
            Function<U,U> handler) throws ExecutionException, InterruptedException, TimeoutException {
        handler.apply((U) assertThat(this.andReturn()));
        return this;
    }
    public T andReturn() throws ExecutionException, InterruptedException, TimeoutException {
        return this.future.get(this.timeout, TimeUnit.SECONDS);
    }
    public T andReturn(Integer timeout) throws ExecutionException, InterruptedException, TimeoutException {
        return this.future.get(timeout, TimeUnit.SECONDS);
    }
//    public StompResultActions<T> andAssert(Function<>)
}

