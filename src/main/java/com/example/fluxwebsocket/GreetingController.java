package com.example.fluxwebsocket;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.HtmlUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "OnlyWebsocket")
@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(
        @Parameter(description = "사용자의 이름이 담긴 메시지", required = true)
        HelloMessage message
    ) throws Exception {
        Thread.sleep(1000); // simulated delay
        System.out.println("Received message: " + message.getName());
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @Operation(summary = "OpenAPI 문서용 WebSocket 엔드포인트", description = "HTTP 요청 시 에러 발생")
    @GetMapping("/app/hello")
    public Greeting mockGreeting(
            @Parameter(description = "사용자의 이름이 담긴 메시지", required = true)
            HelloMessage message) {
        throw new UnsupportedOperationException("이 엔드포인트는 STOMP/WebSocket 통신 전용이므로 HTTP로는 사용할 수 없습니다.");
    }
}
