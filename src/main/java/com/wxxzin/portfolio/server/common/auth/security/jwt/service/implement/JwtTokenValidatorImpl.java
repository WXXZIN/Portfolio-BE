package com.wxxzin.portfolio.server.common.auth.security.jwt.service.implement;

import java.util.Optional;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.stereotype.Component;

import com.wxxzin.portfolio.server.common.auth.security.jwt.repository.JwtTokenRepository;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenProvider;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenValidator;
import com.wxxzin.portfolio.server.common.exception.UserAuthException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

@Component
public class JwtTokenValidatorImpl implements JwtTokenValidator {

    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenValidatorImpl(
        JwtTokenRepository jwtTokenRepository, 
        JwtTokenProvider jwtTokenProvider
    ) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void validateAccessToken(String accessToken, String deviceId) {
        Long userId = validateToken(
            accessToken, 
            deviceId, 
            ErrorMessage.ACCESS_TOKEN_NOT_FOUND,
            ErrorMessage.EXPIRED_ACCESS_TOKEN
        );

        Optional<String> storedAccessTokenOpt = jwtTokenRepository.getAccessToken(userId, deviceId);

        if (storedAccessTokenOpt.isEmpty() || !accessToken.equals(storedAccessTokenOpt.get())) {
            throw new UserAuthException(ErrorMessage.INVALID_ACCESS_TOKEN);
        }
    }

    @Override
    public void validateRefreshToken(String refreshToken, String deviceId) {
        Long userId = validateToken(
            refreshToken, 
            deviceId, 
            ErrorMessage.REFRESH_TOKEN_NOT_FOUND,
            ErrorMessage.EXPIRED_REFRESH_TOKEN
        );

        Optional<String> storedRefreshTokenOpt = jwtTokenRepository.getRefreshToken(userId, deviceId);

        if (storedRefreshTokenOpt.isEmpty() || !refreshToken.equals(storedRefreshTokenOpt.get())) {
            throw new UserAuthException(ErrorMessage.INVALID_REFRESH_TOKEN);
        }
    }

    private Long validateToken(
        String token,
        String deviceId,
        ErrorMessage tokenNotFoundMessage,
        ErrorMessage tokenExpiredMessage
    ) {
        if (token == null) {
            throw new UserAuthException(tokenNotFoundMessage);
        }

        if (deviceId == null) {
            throw new UserAuthException(ErrorMessage.DEVICE_NOT_FOUND);
        }

        try {
            jwtTokenProvider.isExpired(token);
        } catch (ExpiredJwtException e) {
            throw new UserAuthException(tokenExpiredMessage);
        } catch (MalformedJwtException e) {
            throw new UserAuthException(ErrorMessage.MALFORMED_TOKEN);
        } catch (SignatureException e) {
            throw new UserAuthException(ErrorMessage.INVALID_SIGNATURE);
        } catch (ClaimJwtException e) {
            throw new UserAuthException(ErrorMessage.INVALID_CLAIMS);
        } catch (Exception e) {
            throw new UserAuthException(ErrorMessage.UNKNOWN_ERROR);
        }
        
        return jwtTokenProvider.getUserId(token);
    }
}
