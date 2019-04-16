package com.chase.demo.websocket.spring.webflux.http.webclient;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ForwardMessageClient {
    private Map<String, WebClient> webClientMap = new ConcurrentHashMap<>();
    /**
     * 转发消息到指定服务
     * @return
     */
    public Mono<String> forwardMessage(String url, String message){
        WebClient webClient = webClientMap.getOrDefault(url, WebClient.create(url));
        webClientMap.putIfAbsent(url, webClient);
        return webClient
                .method(HttpMethod.POST)
                .uri("/forward")
                .contentType(MediaType.TEXT_PLAIN)
                .body(Mono.just(message), String.class)
                .retrieve()
                .bodyToMono(String.class);
    }
}
