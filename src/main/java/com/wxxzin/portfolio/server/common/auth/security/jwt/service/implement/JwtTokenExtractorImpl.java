package com.wxxzin.portfolio.server.common.auth.security.jwt.service.implement;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenExtractor;

@Component
public class JwtTokenExtractorImpl implements JwtTokenExtractor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";

    @Override
    public Optional<String> getAccessToken(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION_HEADER);
        
        if (bearer != null && bearer.startsWith(ACCESS_TOKEN_PREFIX)) {
            return Optional.of(bearer.substring(ACCESS_TOKEN_PREFIX.length()));
        }

        return Optional.empty();
    }
}
