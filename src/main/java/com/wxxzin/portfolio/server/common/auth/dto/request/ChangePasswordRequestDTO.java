package com.wxxzin.portfolio.server.common.auth.dto.request;

public record ChangePasswordRequestDTO(
    String currentPassword,
    String newPassword
) {
}
