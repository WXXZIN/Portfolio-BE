package com.wxxzin.portfolio.server.common.auth.security.filter;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wxxzin.portfolio.server.common.auth.dto.request.LocalUserLoginRequestDTO;
import com.wxxzin.portfolio.server.common.auth.security.local.handler.JsonLoginFailureHandler;
import com.wxxzin.portfolio.server.common.auth.security.local.handler.JsonLoginSuccessHandler;
import com.wxxzin.portfolio.server.common.exception.UserAuthException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

public class JsonLoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final String DEFAULT_FILTER_PROCESS_URL = "/api/v1/user/auth/login";
    private static final String POST_METHOD = "POST";

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    
    public JsonLoginFilter(
        AuthenticationManager authenticationManager,
        JsonLoginSuccessHandler jsonLoginSuccessHandler,
        JsonLoginFailureHandler jsonLoginFailureHandler
    ) {
        this.authenticationManager = authenticationManager;
        this.setFilterProcessesUrl(DEFAULT_FILTER_PROCESS_URL);
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler(jsonLoginSuccessHandler);
        this.setAuthenticationFailureHandler(jsonLoginFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws AuthenticationException {

        if (!isJsonLoginRequest(request)) {
            return super.attemptAuthentication(request, response);
        }

        LocalUserLoginRequestDTO localUserLoginRequestDTO;

        try {
            localUserLoginRequestDTO = objectMapper.readValue(request.getInputStream(), LocalUserLoginRequestDTO.class);
        } catch (IOException e) {
            throw new UserAuthException(ErrorMessage.INVALID_REQUEST_BODY);
        }

        String username = localUserLoginRequestDTO.username();
        String password = localUserLoginRequestDTO.password();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    private boolean isJsonLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().equals(DEFAULT_FILTER_PROCESS_URL)
                && request.getMethod().equals(POST_METHOD);
    }
}
