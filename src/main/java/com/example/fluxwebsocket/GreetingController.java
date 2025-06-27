package com.example.fluxwebsocket;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "OnlyWebsocket")
@RestController
public class GreetingController {

    @Autowired
    private SimpMessagingTemplate webSocket;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(
        HelloMessage message
    ) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @PostMapping("/app/hello")
    public Greeting mockGreeting(

            @RequestBody  HelloMessage message) {
        Greeting greeting = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
        webSocket.convertAndSend("/topic/greetings", greeting);
        return greeting;
    }

    @MessageMapping("/sendtouser")
    @SendToUser("/topic/greetings")
    public Greeting sendToUser(
        HelloMessage message
    ) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
