package com.wxxzin.portfolio.server.common.auth.service.implement;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceService;
import com.wxxzin.portfolio.server.common.auth.security.jwt.repository.JwtTokenRepository;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenService;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenValidator;
import com.wxxzin.portfolio.server.common.auth.service.BaseUserAuthService;
import com.wxxzin.portfolio.server.common.exception.RemovalException;
import com.wxxzin.portfolio.server.common.exception.UserAuthException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.repository.BaseUserRepository;

import io.jsonwebtoken.ExpiredJwtException;

@Service
@Slf4j
public class BaseUserAuthServiceImpl implements BaseUserAuthService{

    private final BaseUserRepository baseUserRepository;
    private final JwtTokenService jwtTokenService;
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenValidator jwtTokenValidator;
    private final DeviceService deviceService;
    
    public BaseUserAuthServiceImpl(
        BaseUserRepository baseUserRepository,
        JwtTokenService jwtTokenService, 
        JwtTokenRepository jwtTokenRepository,
        JwtTokenValidator jwtTokenValidator,
        DeviceService deviceService
    ) {
        this.baseUserRepository = baseUserRepository;
        this.jwtTokenService = jwtTokenService;
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtTokenValidator = jwtTokenValidator;
        this.deviceService = deviceService;
    }

    // Aceess Token이 만료된 경우도 있어, 필터에서 제외하여 처리
    @Override
    @Transactional
    public void logoutCurrentDevice(
        String refreshToken,
        String deviceId
    ) { 
        Long userId = jwtTokenService.getUserIdFromToken(refreshToken);

        logout(userId, refreshToken, deviceId);
    }

    @Override
    @Transactional
    public void logoutTargetDevice(
        Long userId,
        String targetDeviceId
    ) {
        String targetRefreshToken = jwtTokenRepository.getRefreshToken(userId, targetDeviceId)
                .orElseThrow(() -> new UserAuthException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND));
        
        logout(userId, targetRefreshToken, targetDeviceId);
    }

    @Transactional(noRollbackFor = { RemovalException.class })
    protected void logout(Long userId, String refreshToken, String deviceId) {
        try {
            jwtTokenValidator.validateRefreshToken(refreshToken, deviceId);
        } catch (UserAuthException e) {
            log.error("Authentication error during logout: {}", e.getMessage());
        } catch (NoSuchElementException e) {
            log.error("No refresh token found for the device");
        } catch (ExpiredJwtException e) {
            log.warn("Refresh token expired: {}", e.getMessage());
        }
    
        try {
            jwtTokenRepository.deleteTokens(userId, deviceId);
            deviceService.deleteDevice(deviceId);
        } catch (RemovalException e) {
            log.error("Device not found: {}", e.getMessage());
        }
    }

    @Override
    public void deleteUser(Long userId, String clientType) {
        BaseUserEntity baseUserEntity = baseUserRepository.findById(userId)
            .orElseThrow(
                () -> new UserAuthException(ErrorMessage.USER_NOT_FOUND)
            );
        try {
            baseUserRepository.delete(baseUserEntity);
            jwtTokenRepository.deleteAllTokens(userId);
        } catch (DataIntegrityViolationException e) {
            SQLException sqlException = (SQLException) e.getMostSpecificCause();
            String errorMessage = sqlException.getMessage();

            if (errorMessage != null && errorMessage.contains("foreign key constraint fails")) {
                String[] parts = errorMessage.split("`");

                if (parts.length > 2) {
                    String referencedTable = parts[3];
                    String referencingColumn = parts[7];

                    if (referencedTable.equals("team") && referencingColumn.equals("leader_id") ||
                        referencedTable.equals("team_member_mapping") && referencingColumn.equals("user_id")) {
                        throw new RemovalException(ErrorMessage.USER_IS_TEAM_LEADER_OR_MEMBER);
                    }

                    throw new RemovalException(ErrorMessage.USER_DELETE_FAILED);
                }
            }
        } catch (Exception e) {
            throw new RemovalException(ErrorMessage.USER_DELETE_FAILED);
        }
    }

    // Aceess Token이 만료된 경우 재발급을 위한 로직이라 필터에서 제외하여 처리
    @Override
    @Transactional
    public String reissueAccessToken(
        String refreshToken,
        String deviceId
    ) {
        jwtTokenValidator.validateRefreshToken(refreshToken, deviceId);

        return jwtTokenService.reissueAccessToken(refreshToken, deviceId);
    }

    @Override
    @Transactional
    public UserAuthResponseDTO reissueAccessTokenForSocialLogin(
        String refreshToken,
        String deviceId
    ) {
        jwtTokenValidator.validateRefreshToken(refreshToken, deviceId);

        Long userId = jwtTokenService.getUserIdFromToken(refreshToken);
        BaseUserEntity baseUserEntity = baseUserRepository.findById(userId)
            .orElseThrow(() -> new UserAuthException(ErrorMessage.USER_NOT_FOUND));

        return UserAuthResponseDTO.of(
            jwtTokenService.reissueAccessToken(refreshToken, deviceId),
            null,
            null,
            UserResponseDTO.of(baseUserEntity)
        );
    }
}
