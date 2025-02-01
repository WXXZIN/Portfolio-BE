package com.wxxzin.portfolio.server.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.wxxzin.portfolio.server.common.entity.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "local_user")
public class LocalUserEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @OneToOne
    @JoinColumn(name = "user_id")
    private BaseUserEntity baseUserEntity;

    @Builder
    public LocalUserEntity(BaseUserEntity baseUserEntity, String username, String password) {
        this.baseUserEntity = baseUserEntity;
        this.username = username;
        this.password = password;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
