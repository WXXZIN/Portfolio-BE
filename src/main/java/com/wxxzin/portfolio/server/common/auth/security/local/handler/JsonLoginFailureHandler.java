package com.wxxzin.portfolio.server.common.auth.security.local.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.security.handler.CommonLoginFailureHandler;
import com.wxxzin.portfolio.server.common.auth.security.handler.ResponseHandler;
import com.wxxzin.portfolio.server.common.exception.UserAuthException;
import com.wxxzin.portfolio.server.common.response.error.ApiErrorResponse;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

@Component
@Slf4j
public class JsonLoginFailureHandler extends CommonLoginFailureHandler {
    
    @Override
    protected void handleFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        ApiErrorResponse apiErrorResponse;

        if (exception instanceof UserAuthException authException) {

            apiErrorResponse = ApiErrorResponse.of(
                    ErrorMessage.USER_LOGIN_FAILED,
                    authException.getErrorMessage()
            );
        } else if (exception instanceof BadCredentialsException) {
            apiErrorResponse = ApiErrorResponse.of(
                    ErrorMessage.USER_LOGIN_FAILED,
                    ErrorMessage.INVALID_CREDENTIALS
            );
        } else {
            apiErrorResponse = ApiErrorResponse.of(
                    ErrorMessage.USER_LOGIN_FAILED,
                    ErrorMessage.USER_NOT_FOUND
            );
        }

        ResponseHandler.setErrorResponse(
            apiErrorResponse,
            response
        );
    }
}
