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

    public void incrementRequestCount(String email) {
        String key = "email:request:count:" + email;
        redisTemplate.opsForValue().increment(key, 1);
        redisTemplate.expire(key, Duration.ofMinutes(5));
    }

    public Optional<String> getRequestCount(String email) {
        String key = "email:request:count:" + email;
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void saveLastRequestTime(String key) {
        long currentTime = System.currentTimeMillis();
        redisTemplate.opsForValue().set(key, String.valueOf(currentTime), Duration.ofMinutes(1));
    }

    public Optional<String> getLastRequestTime(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void saveBlockStatus(String key, boolean status, Duration duration) {
        redisTemplate.opsForValue().set(key, String.valueOf(status), duration);
    }

    public Optional<Boolean> getBlockStatus(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value).map(Boolean::parseBoolean);
    }
}
