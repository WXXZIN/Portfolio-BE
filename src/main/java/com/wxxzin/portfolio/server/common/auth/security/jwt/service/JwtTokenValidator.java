package com.wxxzin.portfolio.server.common.auth.security.jwt.service;

public interface JwtTokenValidator {
    void validateAccessToken(String accessToken, String deviceId);
    void validateRefreshToken(String refreshToken, String deviceId);
}
