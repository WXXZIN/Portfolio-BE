package com.wxxzin.portfolio.server.common.auth.security.device.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.wxxzin.portfolio.server.common.entity.BaseTimeEntity;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "device")
public class DeviceEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 36)
    private String deviceId;

    @Column(nullable = false)
    private String os;

    @Column(nullable = false)
    private String deviceName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BaseUserEntity baseUserEntity;

    @Builder
    public DeviceEntity(
        Long userId,
        String deviceId, 
        String os, 
        String deviceName
    ) {
        this.baseUserEntity = BaseUserEntity.builder().build();
        this.baseUserEntity.setId(userId);
        this.deviceId = deviceId;
        this.os = os;
        this.deviceName = deviceName;
    }

    public void updateLastLogin() {
        setUpdatedAt(LocalDateTime.now());
    }
}
