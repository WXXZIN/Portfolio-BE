package com.wxxzin.portfolio.server.common.auth.service.implement;

import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.dto.request.DeviceRegisterRequestDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceService;
import com.wxxzin.portfolio.server.common.auth.security.jwt.repository.JwtTokenRepository;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenProvider;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenService;
import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalOAuth2User;
import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUserDetails;
import com.wxxzin.portfolio.server.common.auth.service.AuthenticationService;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.enums.Role;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenService jwtTokenService;
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final DeviceService deviceService;

    public AuthenticationServiceImpl(
        JwtTokenService jwtTokenService,
        JwtTokenRepository jwtTokenRepository,
        JwtTokenProvider jwtTokenProvider,
        DeviceService deviceService
    ) {
        this.jwtTokenService = jwtTokenService;
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.deviceService = deviceService;
    }

    @Override
    public void setAuthentication(String accessToken) {
        UserResponseDTO userResponseDTO = jwtTokenProvider.getUserFromAccessToken(accessToken);

        Authentication authentication = createAuthentication(userResponseDTO);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication(UserResponseDTO userResponseDTO) {
        if (userResponseDTO.provider() == Provider.LOCAL) {
            PrincipalUserDetails principalUserDetails = new PrincipalUserDetails(userResponseDTO);

            return new UsernamePasswordAuthenticationToken(principalUserDetails, null, principalUserDetails.getAuthorities());
        } else {
            PrincipalOAuth2User principalOAuth2User = new PrincipalOAuth2User(userResponseDTO);

            return new OAuth2AuthenticationToken(principalOAuth2User, principalOAuth2User.getAuthorities(), principalOAuth2User.getProvider().name());
        }
    }

    @Override
    public UserAuthResponseDTO handleAuthenticationSuccess(UserResponseDTO userResponseDTO, String deviceId, HttpServletRequest request) {
        Long userId = userResponseDTO.userId();
        Provider provider = userResponseDTO.provider();
        Role role = userResponseDTO.role();
        
        String accessToken = jwtTokenService.createAccessToken(userId, provider, role);
        String refreshToken = jwtTokenService.createRefreshToken(userId);

        String os;
        String deviceName;

        String clientType = request.getHeader("X-Client-Type");

        if ("FLUTTER-APP".equals(clientType)) {
            os = request.getHeader("os");
            deviceName = request.getHeader("deviceName");
        } else {
            os = "UNKNOWN";
            deviceName = "UNKNOWN";
        }

        DeviceRegisterRequestDTO deviceRegisterRequestDTO = new DeviceRegisterRequestDTO(
            deviceId, 
            os,
            deviceName
        );
        deviceService.registerDevice(deviceRegisterRequestDTO, userId);

        jwtTokenRepository.saveAccessToken(userId, deviceId, accessToken);
        jwtTokenRepository.saveRefreshToken(userId, deviceId, refreshToken);

        return new UserAuthResponseDTO(
                accessToken,
                refreshToken,
                deviceId,
                userResponseDTO
        );
    }

    @Override
    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        Date expireTime = jwtTokenProvider.getExpirationTime(refreshToken);
        long maxAge = (expireTime.getTime() - System.currentTimeMillis()) / 1000;

        return createCookie("refreshToken", refreshToken, maxAge);
    }

    @Override
    public ResponseCookie createDeviceIdCookie(String deviceId) {
        return createCookie("deviceId", deviceId, 60 * 60 * 24 * 365);
    }

    @Override
    public void updateLastLoginAt(String deviceId) {
        deviceService.updateLastLogin(deviceId);
    }

    private ResponseCookie createCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAge)
                .build();
    }
}
