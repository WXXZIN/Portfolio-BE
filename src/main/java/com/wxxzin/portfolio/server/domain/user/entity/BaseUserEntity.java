package com.wxxzin.portfolio.server.domain.user.entity;

import java.util.List;

import com.wxxzin.portfolio.server.domain.project.entity.HeartEntity;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.team.entity.TeamApplication;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.*;

import com.wxxzin.portfolio.server.common.auth.security.device.entity.DeviceEntity;
import com.wxxzin.portfolio.server.common.entity.BaseTimeEntity;
import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;
import com.wxxzin.portfolio.server.domain.team.entity.TeamMemberMapping;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.enums.Role;

@SequenceGenerator(
    name = "USER_SEQ_GENERATOR",
    sequenceName = "USER_SEQ",
    allocationSize = 1
)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"provider", "email"})
})
public class BaseUserEntity extends BaseTimeEntity {

    @Setter
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(nullable = false, length = 50)
    private String email;

    @OneToOne(mappedBy = "baseUserEntity", cascade = CascadeType.ALL)
    private LocalUserEntity localUserEntity;

    @OneToOne(mappedBy = "baseUserEntity", cascade = CascadeType.ALL)
    private SocialUserEntity socialUserEntity;

    @OneToMany(mappedBy = "baseUserEntity", cascade = CascadeType.ALL)
    private List<DeviceEntity> deviceEntities;

    @OneToMany(mappedBy = "leader")
    private List<TeamEntity> leaderTeams;

    @OneToMany(mappedBy = "baseUserEntity", cascade = CascadeType.REMOVE)
    private List<TeamApplication> teamApplications;

    @OneToMany(mappedBy = "baseUserEntity")
    private List<TeamMemberMapping> teamMemberMappings;

    @OneToMany(mappedBy = "baseUserEntity", cascade = CascadeType.REMOVE)
    private List<ProjectEntity> projectEntities;

    @OneToMany(mappedBy = "baseUserEntity", cascade = CascadeType.REMOVE)
    private List<HeartEntity> heartEntities;

    @Builder
    public BaseUserEntity (Provider provider, Role role, String nickname, String email) {
        this.provider = provider;
        this.role = role != null ? role : Role.ROLE_USER;
        this.email = email;
        this.nickname = nickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
}
