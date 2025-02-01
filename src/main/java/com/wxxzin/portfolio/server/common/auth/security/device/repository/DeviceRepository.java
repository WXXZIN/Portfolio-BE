package com.wxxzin.portfolio.server.common.auth.security.device.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.common.auth.security.device.entity.DeviceEntity;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
    List<DeviceEntity> findByBaseUserEntityId(Long userId);
    Optional<DeviceEntity> findByDeviceId(String deviceId);
    boolean existsByBaseUserEntityIdAndDeviceId(Long userId, String deviceId);
}