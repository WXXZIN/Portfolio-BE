package com.wxxzin.portfolio.server.domain.project.entity;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "heart",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "user_id"})
    }
)
public class HeartEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private BaseUserEntity baseUserEntity;

    @Builder
    public HeartEntity(ProjectEntity projectEntity, BaseUserEntity baseUserEntity) {
        this.projectEntity = projectEntity;
        this.baseUserEntity = baseUserEntity;
    }
}
