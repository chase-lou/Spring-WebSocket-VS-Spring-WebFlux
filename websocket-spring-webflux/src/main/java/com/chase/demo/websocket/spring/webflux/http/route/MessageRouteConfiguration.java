package com.chase.demo.websocket.spring.webflux.http.route;


import com.chase.demo.websocket.spring.webflux.http.handler.ForwardMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MessageRouteConfiguration {
    @Bean
    public RouterFunction<ServerResponse> routeMessaage(ForwardMessageHandler forwardMessageHandler) {
        return RouterFunctions
                .route(RequestPredicates.POST("/forward")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        forwardMessageHandler::forwardMessage);
    }
}
