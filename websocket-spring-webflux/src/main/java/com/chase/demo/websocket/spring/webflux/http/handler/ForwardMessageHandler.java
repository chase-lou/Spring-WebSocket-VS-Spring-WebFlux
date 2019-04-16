package com.chase.demo.websocket.spring.webflux.http.handler;

import com.chase.demo.websocket.spring.webflux.websocket.handler.ServerWebSocketHandler;
import com.chase.demo.websocket.spring.webflux.websocket.handler.WebSocketSessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class ForwardMessageHandler {

    final Logger log = LoggerFactory.getLogger(ForwardMessageHandler.class);

    @Autowired
    ServerWebSocketHandler serverWebSocketHandler;

    /**
     * 发送消息给当前服务的所有webSocket客户端
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> forwardMessage(ServerRequest serverRequest) {
        Mono<String> msgMono = serverRequest.bodyToMono(String.class);
        //需要使用flatMap创建一个新的流
        return msgMono.flatMap(msg -> {
            Flux.fromArray(serverWebSocketHandler.getSessionHandlerMap().values().toArray())
                    //切换到弹性线程池发送WebSocket数据
                    .publishOn(Schedulers.elastic())
                    .doOnNext(handler -> ((WebSocketSessionHandler) handler).send(msg))
                    //从flux中消费掉这个sessionHandler，每个sessionHandler自己去发送消息
                    .subscribe();
            return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Ok"), String.class);
        });
    }
}
