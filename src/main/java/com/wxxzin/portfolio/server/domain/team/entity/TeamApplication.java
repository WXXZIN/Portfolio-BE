package com.wxxzin.portfolio.server.domain.team.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import com.wxxzin.portfolio.server.common.entity.BaseTimeEntity;
import com.wxxzin.portfolio.server.domain.team.enums.ApplicationStatus;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "team_application")
public class TeamApplication extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BaseUserEntity baseUserEntity;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus = ApplicationStatus.PENDING;

    @Builder
    public TeamApplication(
        BaseUserEntity baseUserEntity, 
        TeamEntity teamEntity, 
        ApplicationStatus applicationStatus
    ) {
        this.baseUserEntity = baseUserEntity;
        this.teamEntity = teamEntity;
        this.applicationStatus = applicationStatus != null ? applicationStatus : ApplicationStatus.PENDING;
    }

    public void updateStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
