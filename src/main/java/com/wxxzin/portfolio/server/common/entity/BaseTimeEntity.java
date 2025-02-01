package com.wxxzin.portfolio.server.common.entity;

import java.time.LocalDateTime;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @Setter
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
