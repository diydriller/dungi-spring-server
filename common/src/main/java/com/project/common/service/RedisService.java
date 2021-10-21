package com.project.common.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.error.BaseException;
import com.project.common.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.project.common.response.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void setStringOpsTtl(String k, String v, long ttl, TimeUnit unit){
        redisTemplate.opsForValue().set(k, v, ttl, unit);
    }

    public Optional<Object> getStringOps(String k){
        return Optional.ofNullable(redisTemplate.opsForValue().get(k));
    }


    public User getUser(String k) throws JsonProcessingException {
        String userJson = getStringOps(k)
                .map(Object::toString)
                .orElseThrow(()->{throw new BaseException(NOT_USER_LOGIN);});

        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(userJson,User.class);

    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveUser(User user) throws JsonProcessingException {

        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);

        redisTemplate.opsForValue().set("login_"+user.getId(),userJson);
    }





}
