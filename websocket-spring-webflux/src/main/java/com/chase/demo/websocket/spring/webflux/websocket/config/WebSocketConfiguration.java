package com.chase.demo.websocket.spring.webflux.websocket.config;

import com.chase.demo.websocket.spring.webflux.websocket.handler.ServerWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfiguration {

    @Bean
    public ServerWebSocketHandler serverWebSocketHandler()
    {
        return new ServerWebSocketHandler();
    }

    @Bean
    public HandlerMapping webSocketMapping(ServerWebSocketHandler serverWebSocketHandler) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/account", serverWebSocketHandler);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(-1);
        mapping.setUrlMap(map);
        return mapping;
    }
    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
//    @Bean
//    public WebSocketService webSocketService() {
//        ReactorNettyRequestUpgradeStrategy strategy = new ReactorNettyRequestUpgradeStrategy();
//        HandshakeWebSocketService handshakeWebSocketService = new HandshakeWebSocketService(strategy);
//        return handshakeWebSocketService;
//    }
}
