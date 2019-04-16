package com.chase.demo.websocket.spring.websocket.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class AppHeartbeat implements ApplicationRunner {

    Logger log = LoggerFactory.getLogger(AppHeartbeat.class);

    private String serverId = UUID.randomUUID().toString();

    public static final String serverCachePrefix = "server_";
    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ServerConfig serverConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        每3S心跳一次
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() ->
                Mono.just(serverConfig.getUrl()).subscribe(this::refreshServerIp),
                1,3, TimeUnit.SECONDS);
        Mono.just(serverConfig.getUrl()).subscribe(this::refreshServerIp);
    }

    private void refreshServerIp(String url){
        redisTemplate
                .opsForValue()
                .set(String.format("%s%s", serverCachePrefix, serverId), url, Duration.ofSeconds(5));
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
