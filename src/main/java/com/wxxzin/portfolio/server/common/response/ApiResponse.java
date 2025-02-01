package com.wxxzin.portfolio.server.common.response;

import lombok.Getter;

@Getter
public abstract class ApiResponse {

    private final boolean success;

    protected ApiResponse(boolean success) {
        this.success = success;
    }
}
