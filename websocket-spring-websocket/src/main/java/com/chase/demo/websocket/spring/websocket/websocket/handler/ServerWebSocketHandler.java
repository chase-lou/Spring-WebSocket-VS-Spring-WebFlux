package com.chase.demo.websocket.spring.websocket.websocket.handler;

import com.chase.demo.websocket.spring.websocket.app.AppHeartbeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ServerWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 建立连接后
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionMap.put(session.getId(), session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Set<String> keys = stringRedisTemplate.keys(AppHeartbeat.serverCachePrefix + "*");
        for (String key : keys) {
            String serverAddress = stringRedisTemplate.opsForValue().get(key);
            restTemplate.postForObject(serverAddress + "/forward", message.getPayload(),String.class);
        }
        super.handleTextMessage(session, message);
    }

    /**
     * 处理异常信息
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen())
            session.close();
        super.handleTransportError(session, exception);
    }

    /**
     * 连接关闭后
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public Map<String, WebSocketSession> getSessionMap() {
        return sessionMap;
    }
}
