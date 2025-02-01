package com.wxxzin.portfolio.server.common.auth.security.jwt.service;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenExtractor {
    Optional<String> getAccessToken(HttpServletRequest request);
}
