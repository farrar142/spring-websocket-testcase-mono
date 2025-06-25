package com.example.fluxwebsocket.StompTester.results;

import com.example.fluxwebsocket.StompTester.StompResultActions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class StompResult {
    public static StompResultMatcher isEquals(Object expectedValue) {
        return new StompResultMatcher() {
            @Override
            public <T> void apply(StompResultActions<T> actions) throws ExecutionException, InterruptedException {
                if (actions == null) {
                    throw new AssertionError("StompResultActions cannot be null");
                }
                if (expectedValue == null) {
                    throw new AssertionError("Expected value cannot be null");
                }
                if (!actions.andReturn().equals(expectedValue)) {
                    throw new AssertionError("Expected: " + expectedValue + ", but got: " + actions.andReturn());
                }
            }
        };
    }
    public static StompResultMatcher isReceived(Integer timeout, TimeUnit timeUnit){
        return new StompResultMatcher() {
            @Override
            public <T> void apply(StompResultActions<T> actions) throws ExecutionException, InterruptedException, TimeoutException {
                if (actions == null) {
                    throw new AssertionError("StompResultActions cannot be null");
                }
                try{
                    if (actions.andReturn(timeout,timeUnit) == null) {
                        throw new AssertionError("Expected a non-null result, but got null");
                    }
                }catch (TimeoutException e){
                    throw new AssertionError("Expected a result within " + timeout + " " + timeUnit + ", but got a timeout");
                }
            }
        };
    }
    public static StompResultHandler print(){
        return new StompResultHandler() {
            @Override
            public <T> void apply(StompResultActions<T> actions) throws ExecutionException, InterruptedException {
                System.out.println(actions.andReturn());
            }
        };
    }
}
