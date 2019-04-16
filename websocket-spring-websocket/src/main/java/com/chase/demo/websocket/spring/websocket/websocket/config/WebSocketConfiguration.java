package com.chase.demo.websocket.spring.websocket.websocket.config;

import com.chase.demo.websocket.spring.websocket.websocket.handler.ServerWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfiguration  implements WebSocketConfigurer {

    @Autowired
    ServerWebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/account").setAllowedOrigins("*");
    }
}
