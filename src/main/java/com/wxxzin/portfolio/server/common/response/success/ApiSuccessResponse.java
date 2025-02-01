package com.wxxzin.portfolio.server.common.response.success;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

import com.wxxzin.portfolio.server.common.response.ApiResponse;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiSuccessResponse <T> extends ApiResponse {

    private final String message;
    private T data;

    protected ApiSuccessResponse(String message) {
        super(true);
        this.message = message;
    }

    protected ApiSuccessResponse(String message, T data) {
        super(true);
        this.message = message;
        this.data = data;
    }

    public static <T> ApiSuccessResponse<T> of(String message) {
        return new ApiSuccessResponse<>(message);
    }

    public static <T> ApiSuccessResponse<T> of(String message, T data) {
        return new ApiSuccessResponse<>(message, data);
    }
}
