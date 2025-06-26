package com.example.fluxwebsocket;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class SocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler webSocketHandler,
            Map<String,Object> attributes) throws Exception{
        HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest();
        String token = req.getHeader("Authorization");
        System.out.println(token);
        return true;
    }
    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler webSocketHandler,
            Exception exception) {
        // Handshake 후 처리할 로직이 있다면 여기에 작성
    }
}
