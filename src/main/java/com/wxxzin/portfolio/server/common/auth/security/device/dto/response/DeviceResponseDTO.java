package com.wxxzin.portfolio.server.common.auth.security.device.dto.response;

import java.time.LocalDateTime;

import com.wxxzin.portfolio.server.common.auth.security.device.entity.DeviceEntity;

public record DeviceResponseDTO(
    Long userId,
    String deviceId,
    String os,
    String deviceName,
    LocalDateTime firstLoginAt,
    LocalDateTime lastLoginAt
) {
    public static DeviceResponseDTO of(
        DeviceEntity deviceEntity
    ) {
        return new DeviceResponseDTO(
            deviceEntity.getBaseUserEntity().getId(),
            deviceEntity.getDeviceId(),
            deviceEntity.getOs(),
            deviceEntity.getDeviceName(),
            deviceEntity.getCreatedAt(),
            deviceEntity.getUpdatedAt()
        );
    }
}
