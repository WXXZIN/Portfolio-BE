package com.wxxzin.portfolio.server.common.auth.service;

import org.springframework.http.ResponseCookie;

import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    void setAuthentication(String accessToken);
    UserAuthResponseDTO handleAuthenticationSuccess(UserResponseDTO userResponseDTO, String deviceId, HttpServletRequest request);
    ResponseCookie createRefreshTokenCookie(String refreshToken);
    ResponseCookie createDeviceIdCookie(String deviceId);
    void updateLastLoginAt(String deviceId);
}
