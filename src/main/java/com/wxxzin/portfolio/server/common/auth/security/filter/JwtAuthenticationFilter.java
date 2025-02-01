package com.wxxzin.portfolio.server.common.auth.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceIdExtractor;
import com.wxxzin.portfolio.server.common.auth.security.handler.ResponseHandler;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenExtractor;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenValidator;
import com.wxxzin.portfolio.server.common.auth.service.AuthenticationService;
import com.wxxzin.portfolio.server.common.exception.UserAuthException;
import com.wxxzin.portfolio.server.common.response.error.ApiErrorResponse;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDE_PATHS = List.of(
        "/", "/error", "/favicon.ico",
        "/swagger-ui/**", "/v3/api-docs/**",
        "/api/v1/user/auth/login",
        "/api/v1/user/auth/oauth2/{provider}",
        "/api/v1/user/auth/sdk/oauth2/{provider}",
        "/api/v1/user/auth/logout",
        "/api/v1/user/auth/reissue",
        "/api/v1/user/auth/reissue/social",
        "/api/v1/user/auth/find-username",
        "/api/v1/email/**",
        "/api/v1/user/register",
        "/api/v1/user/is-username-taken",
        "/api/v1/user/is-nickname-taken"
    );

    private final AuthenticationService authenticationService;
    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final DeviceIdExtractor deviceIdExtractor;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(
        AuthenticationService authenticationService, 
        JwtTokenValidator jwtTokenValidator,
        JwtTokenExtractor jwtTokenExtractor, 
        DeviceIdExtractor deviceIdExtractor
    ) {
        this.authenticationService = authenticationService;
        this.jwtTokenValidator = jwtTokenValidator;
        this.jwtTokenExtractor = jwtTokenExtractor;
        this.deviceIdExtractor = deviceIdExtractor;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        
        if (path.equals("/api/v1/project/write")) {
            return false;
        }

        boolean shouldNotFilter = EXCLUDE_PATHS.stream()
                .anyMatch(pattern -> {
                    boolean match = antPathMatcher.match(pattern, path);
                    if (match) {
                        System.out.println("Matched pattern: " + pattern + " for path: " + path);
                    }
                    return match;
                });
    
        System.out.println("Request path: " + path + ", shouldNotFilter: " + shouldNotFilter);
        return shouldNotFilter;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> accessTokenOpt = jwtTokenExtractor.getAccessToken(request);
        Optional<String> deviceIdOpt = deviceIdExtractor.getDeviceId(request);

        if (accessTokenOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (deviceIdOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = accessTokenOpt.get();
        String deviceId = deviceIdOpt.get();

        try {
            jwtTokenValidator.validateAccessToken(accessToken, deviceId);
            authenticationService.setAuthentication(accessToken);
            authenticationService.updateLastLoginAt(deviceId);
        } catch (UserAuthException e) {
            ResponseHandler.setErrorResponse(
                ApiErrorResponse.of(
                    ErrorMessage.USER_AUTH_FAILED,
                    e.getErrorMessage()
                ),
                response
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}
