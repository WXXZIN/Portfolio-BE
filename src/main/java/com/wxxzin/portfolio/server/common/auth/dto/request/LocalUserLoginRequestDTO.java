package com.wxxzin.portfolio.server.common.auth.dto.request;

public record LocalUserLoginRequestDTO(
    String username,
    String password
) {
}
