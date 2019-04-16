package com.chase.demo.websocket.spring.webflux.websocket.logic;

import com.chase.demo.websocket.spring.webflux.app.AppHeartbeat;
import com.chase.demo.websocket.spring.webflux.http.webclient.ForwardMessageClient;
import com.chase.demo.websocket.spring.webflux.websocket.handler.ServerWebSocketHandler;
import com.chase.demo.websocket.spring.webflux.websocket.handler.WebSocketSessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
public class ServerLogic
{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ServerWebSocketHandler serverWebSocketHandler;

	@Autowired
	ReactiveStringRedisTemplate reactiveStringRedisTemplate;
	@Autowired
	ForwardMessageClient forwardMessageClient;

	public void start(ServerWebSocketHandler serverWebSocketHandler)
	{
		this.serverWebSocketHandler = serverWebSocketHandler;
		serverWebSocketHandler
			.connected()
			.subscribe(sessionHandler -> doLogic(sessionHandler));
	}
	
	private void doLogic(WebSocketSessionHandler sessionHandler)
	{
//		sessionHandler
//			.connected()
//			.subscribe(session -> logger.info("Server Connected [{}]", session.getId()));
//
//		sessionHandler
//			.disconnected()
//			.subscribe(session -> logger.info("Server Disconnected [{}]", session.getId()));
//
//		Flux<String> receiveAll =
//			sessionHandler
//				.receive()
//				.subscribeOn(Schedulers.elastic())
//				.doOnNext(message -> logger.info("Server Received: [{}]", message));

		Flux<String> receiveAndForward =
				sessionHandler
						.receive()
						.subscribeOn(Schedulers.elastic())
						.doOnNext(message ->
								reactiveStringRedisTemplate
										.keys(AppHeartbeat.serverCachePrefix + "*")
										.doOnNext(key -> reactiveStringRedisTemplate
												.opsForValue()
												.get(key).doOnNext(serverHost -> {
													forwardMessageClient.forwardMessage(serverHost, message).subscribe();
												}).subscribe()).subscribe());

//		Flux<String> send =
//			Flux
//				.interval(Duration.ofMillis(interval))
//				.subscribeOn(Schedulers.elastic())
//				.takeUntil(value -> !sessionHandler.isConnected())
//				.map(value -> Long.toString(value))
//				.doOnNext(message -> sessionHandler.send(message))
//				.doOnNext(message -> logger.info("Server Sent: [{}]", message));
		
//		receiveAll.subscribe();
		receiveAndForward.subscribe();
//		receiveFirst.thenMany(send).subscribe();
	}
}
