package com.project.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.error.BaseException;
import com.project.common.model.User;
import com.project.common.cipher.Aes128;
import com.project.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.project.common.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final Aes128 aes128;
    private final RedisTemplate redisTemplate;

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void setStringOpsTtl(String k, String v, long ttl, TimeUnit unit){
        redisTemplate.opsForValue().set(k, v, ttl, unit);
    }

    public Optional<Object> getStringOps(String k){
        return Optional.ofNullable(redisTemplate.opsForValue().get(k));
    }


    public User getUser(String k) throws Exception {
        String userJson = getStringOps(k)
                .map(Object::toString)
                .orElseThrow(()->{throw new BaseException(NOT_USER_LOGIN);});

        ObjectMapper objectMapper=new ObjectMapper();
        User user=objectMapper.readValue(userJson,User.class);
        return aes128.decryptUser(user);
    }


    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveUser(User user) throws Exception {
        aes128.encryptUser(user);
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);
        setStringOpsTtl(StringUtil.redisLogin(user.getId()),userJson,1,TimeUnit.HOURS);
    }





}
