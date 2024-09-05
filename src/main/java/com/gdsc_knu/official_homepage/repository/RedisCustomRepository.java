package com.gdsc_knu.official_homepage.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisCustomRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public void addData(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<Object> getData(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public void deleteData(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }
}
