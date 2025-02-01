package com.wxxzin.portfolio.server.common.auth.security.jwt.service.implement;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenProvider;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.enums.Role;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final SecretKey secretKey;

    @Value("${spring.jwt.expiration.access}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${spring.jwt.expiration.refresh}")
    private long REFRESH_TOKEN_EXPIRATION;

    private static final String CLAIM_PROVIDER = "provider";
    private static final String CLAIM_ROLE = "role";

    public JwtTokenProviderImpl(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    @Override
    public String createAccessToken(Long userId, Provider provider, Role role) {
        long accessTokenExpirationMillis = ACCESS_TOKEN_EXPIRATION * 60 * 1000;

        return Jwts.builder()
                .subject(userId.toString())
                .claim(CLAIM_PROVIDER, provider.name())
                .claim(CLAIM_ROLE, role.name())
                .signWith(secretKey)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMillis))
                .compact();
    }

    @Override
    public String createRefreshToken(Long userId) {
        long refreshTokenExpirationMillis = REFRESH_TOKEN_EXPIRATION * 24 * 60 * 60 * 1000;

        return Jwts.builder()
                .subject(userId.toString())
                .signWith(secretKey)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMillis))
                .compact();
    }

    @Override
    public Long getUserId(String token) {
        try {
            return Long.parseLong(parseClaims(token).getSubject());
        } catch (ExpiredJwtException e) {
            return Long.parseLong(e.getClaims().getSubject());
        }
    }

    @Override
    public String getProvider(String token) {
        return parseClaims(token).get(CLAIM_PROVIDER, String.class);
    }

    @Override
    public String getRole(String token) {
        return parseClaims(token).get(CLAIM_ROLE, String.class);
    }

    @Override
    public Date getExpirationTime(String token) {
        return parseClaims(token).getExpiration();
    }

    @Override
    public void isExpired(String token) {
        getExpirationTime(token);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    @Override
    public UserResponseDTO getUserFromAccessToken(String token) {
        Claims claims = parseClaims(token);
        Long userId = Long.parseLong(claims.getSubject());
        String provider = getProvider(token);
        String role = getRole(token);

        return UserResponseDTO.of(userId, Provider.valueOf(provider), Role.valueOf(role));
    }
}
