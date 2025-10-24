package org.example.dimollbackend.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void remove(String key,String value){
        redisTemplate.opsForSet().remove(key, value);
    }
    public void add(String key,String value){
        redisTemplate.opsForSet().remove(key,value);
    }
    public boolean isMember(String key,String value){
        return redisTemplate.opsForSet().isMember(key, value);
    }
    public Long size(String key){
        return redisTemplate.opsForSet().size(key);
    }



}
