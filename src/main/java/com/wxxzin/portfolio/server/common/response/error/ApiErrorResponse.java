package com.wxxzin.portfolio.server.common.response.error;

import lombok.Getter;

import com.wxxzin.portfolio.server.common.response.ApiResponse;

@Getter
public class ApiErrorResponse extends ApiResponse {

    private final ErrorDetails errorDetails;

    protected ApiErrorResponse(ErrorDetails errorDetails) {
        super(false);
        this.errorDetails = errorDetails;
    }

    public static ApiErrorResponse of(ErrorMessage errorType, ErrorMessage errorDetails) {
        return new ApiErrorResponse(ErrorDetails.of(errorType, errorDetails));
    }
}
