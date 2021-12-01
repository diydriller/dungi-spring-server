package com.project.redis.infrastrure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate redisTemplate;

    public void saveString(String k, String v,int time){
        redisTemplate.opsForValue().set(k, v, time, TimeUnit.SECONDS);
    }

    public Optional<String> getString(String k){
        return Optional.ofNullable(redisTemplate.opsForValue().get(k).toString());
    }
}
