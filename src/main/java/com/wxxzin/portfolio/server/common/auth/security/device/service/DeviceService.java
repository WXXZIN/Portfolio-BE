package com.wxxzin.portfolio.server.common.auth.security.device.service;

import java.util.List;
import java.util.Map;

import com.wxxzin.portfolio.server.common.auth.security.device.dto.request.DeviceRegisterRequestDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.dto.response.DeviceResponseDTO;

public interface DeviceService {
    void registerDevice(DeviceRegisterRequestDTO deviceRegisterRequestDTO, Long userId);
    Map<String, List<DeviceResponseDTO>> getDeviceList(Long userId);
    DeviceResponseDTO getDeviceDetail(String deviceId, Long userId);
    void updateLastLogin(String deviceId);
    void deleteDevice(String deviceId);
}
