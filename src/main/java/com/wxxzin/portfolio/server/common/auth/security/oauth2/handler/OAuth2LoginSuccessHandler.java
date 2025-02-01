package com.wxxzin.portfolio.server.common.auth.security.oauth2.handler;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceIdExtractor;
import com.wxxzin.portfolio.server.common.auth.security.device.util.DeviceIdGenerator;
import com.wxxzin.portfolio.server.common.auth.security.handler.CommonLoginSuccessHandler;
import com.wxxzin.portfolio.server.common.auth.service.AuthenticationService;

@Component
@Slf4j
public class OAuth2LoginSuccessHandler extends CommonLoginSuccessHandler {

    private static final String WEB_REDIRECT_URL = "http://localhost:3000/auth/oauth2/success";

    public OAuth2LoginSuccessHandler(
        AuthenticationService authenticationService,
        DeviceIdExtractor deviceIdExtractor,
        DeviceIdGenerator deviceIdGenerator
    ) {
        super(authenticationService, deviceIdExtractor, deviceIdGenerator);
    }

    @Override
    protected void handleSuccess(
        UserAuthResponseDTO userAuthResponseDTO,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        response.addHeader("Set-Cookie", authenticationService.createRefreshTokenCookie(userAuthResponseDTO.refreshToken()).toString());
        response.addHeader("Set-Cookie", authenticationService.createDeviceIdCookie(userAuthResponseDTO.deviceId()).toString());
        response.sendRedirect(WEB_REDIRECT_URL);
    }
}
