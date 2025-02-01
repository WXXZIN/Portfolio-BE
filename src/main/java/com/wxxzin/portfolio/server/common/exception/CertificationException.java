package com.wxxzin.portfolio.server.common.exception;

import lombok.Getter;


import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

@Getter
public class CertificationException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public CertificationException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
