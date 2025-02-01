package com.wxxzin.portfolio.server.domain.team.entity;

import java.util.List;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.wxxzin.portfolio.server.common.entity.BaseTimeEntity;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "team")
public class TeamEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "teamEntity", cascade = CascadeType.REMOVE)
    private List<ProjectEntity> projectEntities;

    @OneToMany(mappedBy = "teamEntity", cascade = CascadeType.REMOVE)
    private List<TeamApplication> teamApplications;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private BaseUserEntity leader;
    
    @OneToMany(mappedBy = "teamEntity", cascade = CascadeType.REMOVE)
    private List<TeamMemberMapping> teamMemberMappings;

    @OneToMany(mappedBy = "teamEntity", cascade = CascadeType.REMOVE)
    private List<TeamTaskEntity> taskEntities;

    @Builder
    public TeamEntity(String name, BaseUserEntity leader) {
        this.name = name;
        this.leader = leader;
    }

    public void updateLeader(BaseUserEntity newLeader) {
        this.leader = newLeader;
    }
}
