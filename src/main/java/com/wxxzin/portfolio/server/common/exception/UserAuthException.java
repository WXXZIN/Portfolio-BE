package com.wxxzin.portfolio.server.common.exception;

import org.springframework.security.core.AuthenticationException;

import lombok.Getter;

import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

@Getter
public class UserAuthException extends AuthenticationException {
    
    private final ErrorMessage errorMessage;

    public UserAuthException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
