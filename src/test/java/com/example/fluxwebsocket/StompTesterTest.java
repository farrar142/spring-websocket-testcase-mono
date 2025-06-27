package com.example.fluxwebsocket;


import com.example.fluxwebsocket.StompTester.StompResultActions;
import com.example.fluxwebsocket.StompTester.StompTester;
import com.example.fluxwebsocket.StompTester.builders.StompConnectionBuilder;
import com.example.fluxwebsocket.StompTester.results.StompResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//setPort
//setWebsocketPath
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StompTesterTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StompTester stompTester;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

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

    @Test
    public void testRestAndSocketFailed() throws ExecutionException, InterruptedException, TimeoutException {

        StompResultActions<Greeting> action = stompTester.perform(
                new StompConnectionBuilder<Greeting>()
                        .port(port) // 필수
                        .timeout(5) // 선택
                        .webSocketPath("/gs-guide-websocket") // 선택
                        .subscribe("/topic/greetings", Greeting.class) // 필수
        );

        Assertions.assertThrows(TimeoutException.class,()->{
            action.andExpect(
                    StompResult.isReceived(1)
            );
        });
    }

    @Test
    public void testRestAndSocketSuccess() throws Exception {

        StompResultActions<Greeting> action = stompTester.perform(
                        new StompConnectionBuilder<Greeting>()
                                .port(port) // 필수
                                .timeout(5) // 선택
                                .webSocketPath("/gs-guide-websocket") // 선택
                                .subscribe("/topic/greetings", Greeting.class) // 필수
                );
        mockMvc.perform(
                MockMvcRequestBuilders.post("/app/hello")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsString(
                                        new HelloMessage("Test user")
                                )
                        )
        ).andExpect(
                status().isOk()
        );
        action.andExpect(
                StompResult.isReceived(3)
        ).andExpect(
                StompResult.isEqualTo(new Greeting("Hello, Test user!"))
        );
    }

}
