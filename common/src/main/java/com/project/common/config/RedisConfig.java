package com.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig{

    @Value("${redis.master.name}")
    private String masterName;
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    /*
    @Value("${redis.sentinel.port1}")
    private int sentinelPort1;
    @Value("${redis.sentinel.port2}")
    private int sentinelPort2;
    @Value("${redis.sentinel.port3}")
    private int sentinelPort3;*/

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        /*RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration()
                .master(masterName)
                .sentinel(redisHost,sentinelPort1)
                .sentinel(redisHost,sentinelPort2)
                .sentinel(redisHost,sentinelPort3);*/
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(/*redisSentinelConfiguration*/redisHost,redisPort);

        return lettuceConnectionFactory;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
}

