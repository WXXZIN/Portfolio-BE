package com.wxxzin.portfolio.server.common.email.dto.request;

public record TemporaryPasswordRequestDTO(
    String username,
    String email
) {
}
