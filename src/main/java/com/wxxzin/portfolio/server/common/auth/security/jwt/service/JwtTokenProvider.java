package com.wxxzin.portfolio.server.common.auth.security.jwt.service;

import java.util.Date;

import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.enums.Role;

public interface JwtTokenProvider {
   String createAccessToken(Long userId, Provider provider, Role role);
   String createRefreshToken(Long userId);
   
   Long getUserId(String token);
   String getProvider(String token);
   String getRole(String token);
   Date getExpirationTime(String token);
   void isExpired(String token);
   UserResponseDTO getUserFromAccessToken(String accessToken);
}
