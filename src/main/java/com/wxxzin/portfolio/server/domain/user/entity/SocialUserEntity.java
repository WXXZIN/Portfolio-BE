package com.wxxzin.portfolio.server.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.wxxzin.portfolio.server.common.entity.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "social_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "social_Id"})
})
public class SocialUserEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "social_id", nullable = false, length = 50)
    private String socialId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private BaseUserEntity baseUserEntity;

    @Builder
    public SocialUserEntity(BaseUserEntity baseUserEntity, String socialId) {
        this.baseUserEntity = baseUserEntity;
        this.socialId = socialId;
    }
}
