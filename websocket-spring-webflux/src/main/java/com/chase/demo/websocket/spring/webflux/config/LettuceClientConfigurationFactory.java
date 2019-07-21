package com.chase.demo.websocket.spring.webflux.config;

import java.time.Duration;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.stereotype.Component;

public class LettuceClientConfigurationFactory implements FactoryBean<LettuceClientConfiguration> {

    @Autowired
    private ClientResources clientResources;

    @Override
    public LettuceClientConfiguration getObject() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxWaitMillis(2000);
        poolConfig.setTestOnBorrow(true);
        LettuceClientConfiguration lettuceClientConfiguration =
                LettucePoolingClientConfiguration
                        .builder()
                        .poolConfig(poolConfig)
                        .commandTimeout(Duration.ofMillis(3000))
                        .shutdownTimeout(Duration.ZERO)
                        .clientResources(clientResources)
                        .build();

        return lettuceClientConfiguration;
    }

    @Override
    public Class<?> getObjectType() {
        return LettuceClientConfiguration.class;
    }
}
