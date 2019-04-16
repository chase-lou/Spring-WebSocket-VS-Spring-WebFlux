package com.chase.demo.websocket.spring.webflux.config;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

public class LettuceClientConfigurationFactory implements FactoryBean<LettuceClientConfiguration> {
    @Override
    public LettuceClientConfiguration getObject() throws Exception {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxWaitMillis(2000);
        poolConfig.setTestOnBorrow(true);
        LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig).commandTimeout(Duration.ofMillis(3000)).shutdownTimeout(Duration.ZERO).build();

        return lettuceClientConfiguration;
    }

    @Override
    public Class<?> getObjectType() {
        return LettuceClientConfiguration.class;
    }
}
