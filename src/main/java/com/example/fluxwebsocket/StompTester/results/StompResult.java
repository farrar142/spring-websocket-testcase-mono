package com.example.fluxwebsocket.StompTester.results;

import com.example.fluxwebsocket.StompTester.StompResultActions;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ObjectAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class StompResult {
    public static <T> StompResultMatcher<T> isEqualTo(T expectedValue) {
        return new StompResultMatcher<T>() {
            @Override
            public StompResultMatcher<T> apply(StompResultActions<T> actions) throws ExecutionException, InterruptedException, TimeoutException {
                if (actions == null) {
                    throw new AssertionError("StompResultActions cannot be null");
                }
                if (expectedValue == null) {
                    throw new AssertionError("Expected value cannot be null");
                }
                assertThat(actions.andReturn()).isEqualTo(expectedValue);
                return this;
            }
        };
    }
    public static StompResultHandler print(){
        return new StompResultHandler() {
            @Override
            public <T> void apply(StompResultActions<T> actions) throws ExecutionException, InterruptedException, TimeoutException {
                System.out.println(actions.andReturn());
            }
        };
    }

    public static <T> StompResultMatcher<T> isReceived(Integer timeout){
        return new StompResultMatcher<T>() {
            @Override
            public StompResultMatcher<T> apply(StompResultActions<T> actions) throws ExecutionException, InterruptedException, TimeoutException {
                if (actions == null) {
                    throw new AssertionError("StompResultActions cannot be null");
                }
                if (actions.andReturn(timeout) == null) {
                    throw new AssertionError("Expected a non-null value, but got null");
                }
                return this;
            }
        };
    }

}

