package com.chase.demo.websocket.spring.webflux.websocket.handler;

import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.*;
import reactor.netty.channel.AbortedException;

import java.nio.channels.ClosedChannelException;

public class WebSocketSessionHandler 
{
	private final EmitterProcessor<String> receiveProcessor;
	private final MonoProcessor<WebSocketSession> connectedProcessor;
	private final MonoProcessor<WebSocketSession> disconnectedProcessor;
	
	private boolean webSocketConnected;
	private WebSocketSession session;
	
	public WebSocketSessionHandler()
	{
		this(500);
	}
	
	public WebSocketSessionHandler(int historySize)
	{
		receiveProcessor = EmitterProcessor.create(historySize);
		connectedProcessor = MonoProcessor.create();
		disconnectedProcessor = MonoProcessor.create();
		
		webSocketConnected = false;
	}
	
	protected Mono<Void> handle(WebSocketSession session)
	{
		this.session = session;
		
		Flux<String> receive =
			session
				.receive()
				.map(message -> message.getPayloadAsText())
				.doOnNext(textMessage -> receiveProcessor.onNext(textMessage))
				.doOnComplete(() -> receiveProcessor.onComplete());
		
		Mono<Object> connected =
				Mono
					.fromRunnable(() -> 
					{
						webSocketConnected = true;
						connectedProcessor.onNext(session);
					});

		Mono<Object> disconnected =
				Mono
					.fromRunnable(() -> 
					{
						webSocketConnected = false;
						disconnectedProcessor.onNext(session);
					})
					.doOnNext(value -> receiveProcessor.onComplete());
			
		return connected.thenMany(receive).then(disconnected).then();
	}
	
	public Mono<WebSocketSession> connected()
	{
		return connectedProcessor;
	}
	
	public Mono<WebSocketSession> disconnected()
	{
		return disconnectedProcessor;
	}
	
	public boolean isConnected()
	{
		return webSocketConnected;
	}
	
	public Flux<String> receive()
	{
		return receiveProcessor;
	}
	
	public void send(String message)
	{
		if (webSocketConnected)
		{
			session
				.send(Mono.just(session.textMessage(message)))
				.doOnError(ClosedChannelException.class, t -> connectionClosed())
				.doOnError(AbortedException.class, t -> connectionClosed())
				.onErrorResume(ClosedChannelException.class, t -> Mono.empty())
				.onErrorResume(AbortedException.class, t -> Mono.empty())
				.subscribe();
		}	
	}
	
	private void connectionClosed()
	{
		if (webSocketConnected)
		{
			webSocketConnected = false;
			disconnectedProcessor.onNext(session);
		}
	}
}