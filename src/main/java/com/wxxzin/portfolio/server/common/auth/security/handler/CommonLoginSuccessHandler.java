package com.wxxzin.portfolio.server.common.auth.security.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceIdExtractor;
import com.wxxzin.portfolio.server.common.auth.security.device.util.DeviceIdGenerator;
import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalOAuth2User;
import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUserDetails;
import com.wxxzin.portfolio.server.common.auth.service.AuthenticationService;
import com.wxxzin.portfolio.server.common.exception.UserAuthException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;

public abstract class CommonLoginSuccessHandler implements AuthenticationSuccessHandler {

    protected final AuthenticationService authenticationService;
    protected final DeviceIdExtractor deviceIdExtractor;
    protected final DeviceIdGenerator deviceIdGenerator;

    protected CommonLoginSuccessHandler(
        AuthenticationService authenticationService,
        DeviceIdExtractor deviceIdExtractor,
        DeviceIdGenerator deviceIdGenerator
    ) {
        this.authenticationService = authenticationService;
        this.deviceIdExtractor = deviceIdExtractor;
        this.deviceIdGenerator = deviceIdGenerator;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, 
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {

        Object principal = authentication.getPrincipal();
        UserResponseDTO userResponseDTO = null;

        if (principal instanceof PrincipalUserDetails) {
            PrincipalUserDetails principalUserDetails = (PrincipalUserDetails) authentication.getPrincipal();
            userResponseDTO = principalUserDetails.userResponseDTO();
        } else if (principal instanceof PrincipalOAuth2User) {
            PrincipalOAuth2User principalOAuth2User = (PrincipalOAuth2User) authentication.getPrincipal();
            userResponseDTO = principalOAuth2User.userResponseDTO();
        }

        if (userResponseDTO == null) {
            throw new UserAuthException(ErrorMessage.USER_NOT_FOUND);
        }

        String deviceId = deviceIdExtractor.getDeviceId(request)
            .orElse(deviceIdGenerator.createDeviceId(userResponseDTO.userId()));

        UserAuthResponseDTO userAuthResponseDTO = authenticationService.handleAuthenticationSuccess(
            userResponseDTO, deviceId, request
        );

        handleSuccess(userAuthResponseDTO, request, response);
    }

    protected abstract void handleSuccess(
        UserAuthResponseDTO userAuthResponseDTO,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException;
}
