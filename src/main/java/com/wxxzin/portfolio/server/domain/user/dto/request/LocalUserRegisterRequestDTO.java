package com.wxxzin.portfolio.server.domain.user.dto.request;

public record LocalUserRegisterRequestDTO(
    String username,
    String password,
    String nickname,
    String email
) {
}
