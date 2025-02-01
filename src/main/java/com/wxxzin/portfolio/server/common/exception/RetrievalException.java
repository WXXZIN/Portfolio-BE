package com.wxxzin.portfolio.server.common.exception;

import lombok.Getter;

import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

@Getter
public class RetrievalException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public RetrievalException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
