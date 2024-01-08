package com.zerobase.stockservice.config;

import com.zerobase.stockservice.config.properties.JwtProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Import(CacheConfig.class)
@EnableConfigurationProperties(JwtProps.class)
public class TestCacheConfig {
    @MockBean private RedisConnectionFactory redisConnectionFactory;
}
