package com.wxxzin.portfolio.server.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.jwt-index}")
    private int jwtIndex;

    @Value("${spring.data.redis.mail-index}")
    private int mailIndex;

    @Value("${spring.data.redis.sentinel.master}")
    private String masterName;

    @Value("${spring.data.redis.sentinel.password}")
    private String sentinelPassword;

    private RedisSentinelConfiguration createSentinelConfig() {
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration()
                .master(masterName);

        redisSentinelConfiguration.sentinel("192.168.1.21", 26379);
        redisSentinelConfiguration.sentinel("192.168.1.21", 26380);
        redisSentinelConfiguration.sentinel("192.168.1.21", 26381);
        redisSentinelConfiguration.setSentinelPassword(sentinelPassword);
        redisSentinelConfiguration.setPassword(sentinelPassword);

        return redisSentinelConfiguration;
    }

    @Bean
    @Primary
    public RedisConnectionFactory jwtRedisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(createSentinelConfig());
        connectionFactory.setDatabase(jwtIndex);

        return connectionFactory;
    }

    @Bean
    @Qualifier("mailRedisConnectionFactory")
    public RedisConnectionFactory mailRedisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(createSentinelConfig());
        connectionFactory.setDatabase(mailIndex);

        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> jwtRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(jwtRedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, String> mailRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(mailRedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}