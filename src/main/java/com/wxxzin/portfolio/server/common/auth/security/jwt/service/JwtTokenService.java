package com.wxxzin.portfolio.server.common.auth.security.jwt.service;

import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.enums.Role;

public interface JwtTokenService {
    String createAccessToken(Long userId, Provider provider, Role role);
    String createRefreshToken(Long userId);
    String reissueAccessToken(String refreshToken, String deviceId);
    Long getUserIdFromToken(String token);
}
