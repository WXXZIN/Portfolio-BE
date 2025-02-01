package com.wxxzin.portfolio.server.common.exception;

import lombok.Getter;

import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

@Getter
public class CreationException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public CreationException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
