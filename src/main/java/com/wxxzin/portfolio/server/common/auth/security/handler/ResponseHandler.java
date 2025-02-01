package com.wxxzin.portfolio.server.common.auth.security.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseCookie;

import com.wxxzin.portfolio.server.common.response.error.ApiErrorResponse;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;

public class ResponseHandler {

    public static void setSuccessResponse(
        String accessToken,
        ResponseCookie refreshTokenCookie,
        ResponseCookie deviceIdCookie,
        ApiSuccessResponse<?> apiSuccessResponse,
        HttpServletResponse response
    ) throws IOException {
        setCommonResponse(response, HttpServletResponse.SC_OK);
        
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        response.addHeader("Set-Cookie", deviceIdCookie.toString());
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiSuccessResponse));
        response.getWriter().flush();
    }

    public static void setErrorResponse(
        ApiErrorResponse apiErrorResponse,
        HttpServletResponse response
    ) throws IOException {
        setCommonResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiErrorResponse));
        response.getWriter().flush();
    }

    private static void setCommonResponse(
        HttpServletResponse response,
        int status
    ) {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}
