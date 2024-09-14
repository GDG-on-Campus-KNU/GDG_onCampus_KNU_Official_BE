package com.gdsc_knu.official_homepage.authentication.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "refreshToken")
public class RedisToken {
    private String email;
    @Id
    private String refreshToken;
    @TimeToLive
    private long ttl;
}
