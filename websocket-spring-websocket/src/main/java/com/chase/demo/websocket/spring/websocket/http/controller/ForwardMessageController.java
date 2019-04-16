package com.chase.demo.websocket.spring.websocket.http.controller;

import com.chase.demo.websocket.spring.websocket.websocket.handler.ServerWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@RestController
public class ForwardMessageController {

    @Autowired
    ServerWebSocketHandler serverWebSocketHandler;

    @PostMapping("/forward")
    public String forwardMessage(@RequestBody String message) throws IOException {
        for (WebSocketSession session : serverWebSocketHandler.getSessionMap().values()) {
            session.sendMessage(new TextMessage(message));
        }
        return "Ok";
    }
}
