package com.wxxzin.portfolio.server.common.auth.security.local.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceIdExtractor;
import com.wxxzin.portfolio.server.common.auth.security.device.util.DeviceIdGenerator;
import com.wxxzin.portfolio.server.common.auth.security.handler.CommonLoginSuccessHandler;
import com.wxxzin.portfolio.server.common.auth.security.handler.ResponseHandler;
import com.wxxzin.portfolio.server.common.auth.service.AuthenticationService;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;
import com.wxxzin.portfolio.server.common.response.success.SuccessMessage;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;

@Component
@Slf4j
public class JsonLoginSuccessHandler extends CommonLoginSuccessHandler {
    
    public JsonLoginSuccessHandler(
        AuthenticationService authenticationService,
        DeviceIdExtractor deviceIdExtractor,
        DeviceIdGenerator deviceIdGenerator
    ) {
        super(authenticationService, deviceIdExtractor ,deviceIdGenerator);
    }

    @Override
    protected void handleSuccess(
        UserAuthResponseDTO userAuthResponseDTO,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {

        ApiSuccessResponse<UserResponseDTO> apiSuccessResponse = ApiSuccessResponse.of(
            SuccessMessage.AUTH_LOGIN_SUCCESS.getMessage(),
            userAuthResponseDTO.userResponseDTO()
        );

        ResponseHandler.setSuccessResponse(
            userAuthResponseDTO.accessToken(),
            authenticationService.createRefreshTokenCookie(userAuthResponseDTO.refreshToken()),
            authenticationService.createDeviceIdCookie(userAuthResponseDTO.deviceId()),
            apiSuccessResponse, 
            response
        );
    }
}
