package com.wxxzin.portfolio.server.common.auth.service;

import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;

public interface BaseUserAuthService {
    void logoutCurrentDevice(String refreshToken, String deviceId);
    void logoutTargetDevice(Long userId, String targetDeviceId);
    void deleteUser(Long userId, String clientType);
    String reissueAccessToken(String refreshToken, String deviceId);
    UserAuthResponseDTO reissueAccessTokenForSocialLogin(String refreshToken, String deviceId);
}
