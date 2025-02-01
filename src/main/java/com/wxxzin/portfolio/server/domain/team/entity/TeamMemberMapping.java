package com.wxxzin.portfolio.server.domain.team.entity;

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

import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "team_member_mapping")
public class TeamMemberMapping {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BaseUserEntity baseUserEntity;

    @Builder
    public TeamMemberMapping(
        TeamEntity teamEntity, 
        BaseUserEntity baseUserEntity
    ) {
        this.teamEntity = teamEntity;
        this.baseUserEntity = baseUserEntity;
    }
}
