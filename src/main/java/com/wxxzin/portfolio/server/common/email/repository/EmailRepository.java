package com.wxxzin.portfolio.server.common.email.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmailRepository {
    
    private final RedisTemplate<String, String> redisTemplate;

    public EmailRepository(@Qualifier("mailRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveCertificationNumber(String email, String certificationNumber) {
        redisTemplate.opsForValue()
                .set(email, certificationNumber, Duration.ofMinutes(3));
    }

    public void deleteCertificationNumber(String email) {
        redisTemplate.delete(email);
    }

    public Optional<String> getCertificationNumber(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(email));
    }

    public Boolean hasKey(String email) {
        return redisTemplate.hasKey(email);
    }
}
