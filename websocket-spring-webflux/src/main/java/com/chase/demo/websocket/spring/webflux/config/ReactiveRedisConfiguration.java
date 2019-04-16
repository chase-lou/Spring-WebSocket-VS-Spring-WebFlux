package com.chase.demo.websocket.spring.webflux.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

@Configuration
public class ReactiveRedisConfiguration {
    @Value("${spring.redis.database}")
    int database;
    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    int port;

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return redisStandaloneConfiguration;
    }

    @Bean
    public LettuceClientConfigurationFactory lettuceClientConfigurationFactory() {
        return new LettuceClientConfigurationFactory();
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration,
                                                             LettuceClientConfiguration lettuceClientConfiguration){
        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
    }

    @Bean
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        return new ReactiveStringRedisTemplate(lettuceConnectionFactory);
    }
}
