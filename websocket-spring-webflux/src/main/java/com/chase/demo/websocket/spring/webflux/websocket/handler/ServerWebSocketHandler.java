package com.chase.demo.websocket.spring.webflux.websocket.handler;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket连接处理器
 */
public class ServerWebSocketHandler implements WebSocketHandler
{
	private final DirectProcessor<WebSocketSessionHandler> connectedProcessor;
	private final Map<String, WebSocketSessionHandler> sessionHandlerMap = new ConcurrentHashMap<>();

	public ServerWebSocketHandler()
	{
		connectedProcessor = DirectProcessor.create();
	}

	@Override
	public Mono<Void> handle(WebSocketSession session)
	{
		WebSocketSessionHandler sessionHandler = new WebSocketSessionHandler();

		sessionHandler
			.connected()
			.subscribe(value -> sessionHandlerMap.put(session.getId(), sessionHandler));

		sessionHandler
			.disconnected()
			.subscribe(value -> sessionHandlerMap.remove(session.getId()));

		connectedProcessor.sink().next(sessionHandler);

		return sessionHandler.handle(session);
	}

	public Map<String, WebSocketSessionHandler> getSessionHandlerMap() {
		return sessionHandlerMap;
	}

	public Flux<WebSocketSessionHandler> connected()
	{
		return connectedProcessor;
	}
}