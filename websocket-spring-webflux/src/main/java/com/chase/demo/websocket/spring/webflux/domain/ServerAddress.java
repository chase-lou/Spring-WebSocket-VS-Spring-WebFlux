package com.chase.demo.websocket.spring.webflux.domain;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class ServerAddress implements Serializable {

    @Id
    private String id;
    private String address;//ip:port

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
