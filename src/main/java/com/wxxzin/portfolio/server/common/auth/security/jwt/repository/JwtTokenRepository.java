package com.wxxzin.portfolio.server.common.auth.security.jwt.repository;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JwtTokenRepository {

    @Value("${spring.jwt.expiration.access}")
    private long ACCESS_TOKEN_TTL;

    @Value("${spring.jwt.expiration.refresh}")
    private long REFRESH_TOKEN_TTL;

    private final RedisTemplate<String, String> redisTemplate;

    public JwtTokenRepository(@Qualifier("jwtRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveAccessToken(Long userId, String deviceId, String accessToken) {
        String key = createKey(userId, deviceId, "access");

        redisTemplate.opsForValue()
                .set(key, accessToken, Duration.ofMinutes(ACCESS_TOKEN_TTL));
    }

    public void saveRefreshToken(Long userId, String deviceId, String refreshToken) {
        String key = createKey(userId, deviceId, "refresh");

        redisTemplate.opsForValue()
                .set(key, refreshToken, Duration.ofDays(REFRESH_TOKEN_TTL));
    }

    public Optional<String> getAccessToken(Long userId, String deviceId) {
        String key = createKey(userId, deviceId, "access");

        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public Optional<String> getRefreshToken(Long userId, String deviceId) {
        String key = createKey(userId, deviceId, "refresh");

        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void deleteAllTokens(Long userId) {
        Set<String> keys = redisTemplate.keys(userId + ":*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void deleteTokens(Long userId, String deviceId) {
        deleteAccessToken(userId, deviceId);
        deleteRefreshToken(userId, deviceId);
    }

    private void deleteAccessToken(Long userId, String deviceId) {
        String key = createKey(userId, deviceId, "access");

        redisTemplate.delete(key);
    }

    private void deleteRefreshToken(Long userId, String deviceId) {
        String key = createKey(userId, deviceId, "refresh");

        redisTemplate.delete(key);
    }

    private String createKey(Long userId, String deviceId, String tokenType) {
        if (tokenType.equals("access")) {
            return userId + ":" + deviceId + ":access_token";
        } else {
            return userId + ":" + deviceId + ":refresh_token";
        }
    }
}
