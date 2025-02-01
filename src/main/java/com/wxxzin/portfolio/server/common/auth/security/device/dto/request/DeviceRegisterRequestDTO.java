package com.wxxzin.portfolio.server.common.auth.security.device.dto.request;

public record DeviceRegisterRequestDTO(
    String deviceId,
    String os,
    String deviceName
) {
}
