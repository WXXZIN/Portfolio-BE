package com.wxxzin.portfolio.server.common.auth.security.jwt.service.implement;

import org.springframework.stereotype.Service;

import com.wxxzin.portfolio.server.common.auth.security.jwt.repository.JwtTokenRepository;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenProvider;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenService;
import com.wxxzin.portfolio.server.common.exception.ModificationException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.enums.Role;
import com.wxxzin.portfolio.server.domain.user.repository.BaseUserRepository;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BaseUserRepository baseUserRepository;

    public JwtTokenServiceImpl(
        JwtTokenRepository jwtTokenRepository, 
        JwtTokenProvider jwtTokenProvider,
        BaseUserRepository baseUserRepository
    ) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.baseUserRepository = baseUserRepository;
    }

    @Override
    public String createAccessToken(Long userId, Provider provider, Role role) {
        return jwtTokenProvider.createAccessToken(userId, provider, role);
    }

    @Override
    public String createRefreshToken(Long userId) {
        return jwtTokenProvider.createRefreshToken(userId);
    }

    @Override
    public String reissueAccessToken(String refreshToken, String deviceId) {

        Long userId = jwtTokenProvider .getUserId(refreshToken);
        
        BaseUserEntity baseUserEntity = baseUserRepository.findById(userId).orElseThrow(
            () -> new ModificationException(ErrorMessage.USER_NOT_FOUND)
        );

        Provider provider = baseUserEntity.getProvider();
        Role role = baseUserEntity.getRole();

        String newAccessToken = jwtTokenProvider.createAccessToken(userId, provider, role);
        jwtTokenRepository.saveAccessToken(userId, deviceId, newAccessToken);

        return newAccessToken;
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserId(token);
    }
}
