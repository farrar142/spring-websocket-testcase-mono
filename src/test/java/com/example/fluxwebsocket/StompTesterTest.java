package com.example.fluxwebsocket;


import com.example.fluxwebsocket.StompTester.StompTester;
import com.example.fluxwebsocket.StompTester.builders.StompConnectionBuilder;
import com.example.fluxwebsocket.StompTester.results.StompResult;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

//setPort
//setWebsocketPath
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StompTesterTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StompTester stompTester;

    /*
    * 하나의 요청과 하나의 응답만 받도록
    * 요청에 필요한 값들은 Builder패턴으로 설정 필수,옵셔널값 분리
    * timeout 내장
    * 람다로 어설션사용 하도록 (불가능)
    * */
    @Test
    public void test() throws Exception {
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization", "testUserId");
        stompTester.perform(
                    new StompConnectionBuilder<Greeting>()
                    .port(port) // 필수
                    .timeout(5) // 선택
                    .webSocketPath("/gs-guide-websocket") // 선택
                    .handshakeHeaders(headers) // 선택
                    .subscribe("/topic/greetings", Greeting.class) // 필수
                    .send("/app/hello", new HelloMessage("Test User"))
        )
                .andDo(StompResult.print())
                .andExpect(StompResult.isEqualTo(new Greeting("Hello, Test User!")))
                    .andAssert(
                            result->result.isEqualTo(new Greeting("Hello, Test User!"))
                    )
                .andReturn();
        assertThat(new Greeting()).isEqualTo(new Greeting());
//        jsonPath("$.content").value(2)
    }
}
