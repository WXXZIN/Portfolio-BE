package com.wxxzin.portfolio.server.common.auth.security.device.service.implement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.security.device.dto.request.DeviceRegisterRequestDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.dto.response.DeviceResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.entity.DeviceEntity;
import com.wxxzin.portfolio.server.common.auth.security.device.repository.DeviceRepository;
import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceService;
import com.wxxzin.portfolio.server.common.exception.ModificationException;
import com.wxxzin.portfolio.server.common.exception.RetrievalException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(
        DeviceRepository deviceRepository
    ) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    @Transactional
    public void registerDevice(DeviceRegisterRequestDTO deviceRegisterRequestDTO, Long userId) {
        if (isDeviceAlreadyRegistered(userId, deviceRegisterRequestDTO.deviceId())) {
            return;
        }

        DeviceEntity deviceEntity = createDeviceEntity(userId, deviceRegisterRequestDTO);

        deviceRepository.save(deviceEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<DeviceResponseDTO>> getDeviceList(Long userId) {
        return deviceRepository.findByBaseUserEntityId(userId).stream()
            .map(DeviceResponseDTO::of)
            .collect(Collectors.groupingBy(DeviceResponseDTO::os));
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponseDTO getDeviceDetail(String deviceId, Long userId) {
        return DeviceResponseDTO.of(
            deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RetrievalException(ErrorMessage.DEVICE_NOT_FOUND))
        );
    }

    @Override
    @Transactional
    public void updateLastLogin(String deviceId) {
        DeviceEntity deviceEntity = deviceRepository.findByDeviceId(deviceId)
            .orElseThrow(() -> new ModificationException(ErrorMessage.DEVICE_NOT_FOUND));

        deviceEntity.updateLastLogin();
        deviceRepository.save(deviceEntity);
    }

    @Override
    @Transactional
    public void deleteDevice(String deviceId) {
        deviceRepository.findByDeviceId(deviceId).ifPresent(deviceRepository::delete);

    }

    private boolean isDeviceAlreadyRegistered(Long userId, String deviceId) {
        List<DeviceEntity> deviceList = deviceRepository.findByBaseUserEntityId(userId);
        
        return deviceList.stream().anyMatch(device -> device.getDeviceId().equals(deviceId));
    }

    private DeviceEntity createDeviceEntity(Long userId, DeviceRegisterRequestDTO deviceRegisterRequestDTO) {
        return DeviceEntity.builder()
            .userId(userId)
            .deviceId(deviceRegisterRequestDTO.deviceId())
            .os(deviceRegisterRequestDTO.os())
            .deviceName(deviceRegisterRequestDTO.deviceName())
            .build();
    }
}
