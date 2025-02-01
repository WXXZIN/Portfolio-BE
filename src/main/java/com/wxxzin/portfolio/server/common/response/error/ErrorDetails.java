package com.wxxzin.portfolio.server.common.response.error;

public record ErrorDetails(
    ErrorMessage errorType, 
    String errorTypeMessage, 
    ErrorMessage details,
    String detailMessage) {

    public static ErrorDetails of(ErrorMessage errorType, ErrorMessage details) {
        return new ErrorDetails(errorType, errorType.getMessage(), details, details.getMessage());
    }
}
