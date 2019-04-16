package com.chase.demo.websocket.spring.webflux.websocket.component;

import com.chase.demo.websocket.spring.webflux.websocket.handler.ServerWebSocketHandler;
import com.chase.demo.websocket.spring.webflux.websocket.logic.ServerLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ServerComponent implements ApplicationListener<ApplicationReadyEvent>
{	
	@Autowired
	private ServerWebSocketHandler serverWebSocketHandler;

	@Autowired
	ServerLogic serverLogic;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event)
	{
		serverLogic.start(serverWebSocketHandler);
	}
}