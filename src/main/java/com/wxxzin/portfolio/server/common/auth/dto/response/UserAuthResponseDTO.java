package com.wxxzin.portfolio.server.common.auth.dto.response;

import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;

public record UserAuthResponseDTO(
    String accessToken,
    String refreshToken,
    String deviceId,
    UserResponseDTO userResponseDTO
) {
    public static UserAuthResponseDTO of(String accessToken, String refreshToken, String deviceId, UserResponseDTO userResponseDTO) {
        return new UserAuthResponseDTO(accessToken, refreshToken, deviceId, userResponseDTO);
    }
}
