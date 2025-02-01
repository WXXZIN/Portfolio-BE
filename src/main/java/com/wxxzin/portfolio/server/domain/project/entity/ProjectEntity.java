package com.wxxzin.portfolio.server.domain.project.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

import com.wxxzin.portfolio.server.common.entity.BaseTimeEntity;
import com.wxxzin.portfolio.server.domain.project.enums.RecruitmentStatus;
import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "project")
public class ProjectEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int requireMemberCount = 0;

    @Column(nullable = false)
    private int newRequireMemberCount;

    @Column(nullable = false)
    private int currentMemberCount = 0;

    @Column(nullable = false)
    private LocalDate deadline;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.REMOVE)
    private List<ProjectTagMapping> projectTagMappings;

    @Enumerated(EnumType.STRING)
    private RecruitmentStatus recruitmentStatus = RecruitmentStatus.RECRUITING;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BaseUserEntity baseUserEntity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.REMOVE)
    private List<HeartEntity> heartEntities;

    @Builder
    public ProjectEntity(
        String title, 
        String content,
        int newRequireMemberCount,
        LocalDate deadline,  
        RecruitmentStatus recruitmentStatus, 
        BaseUserEntity baseUserEntity,
        TeamEntity teamEntity
    ) {
        this.title = title;
        this.content = content;
        this.newRequireMemberCount = newRequireMemberCount;
        this.deadline = deadline;
        this.recruitmentStatus = recruitmentStatus != null ? recruitmentStatus : RecruitmentStatus.RECRUITING;
        this.baseUserEntity = baseUserEntity;
        this.teamEntity = teamEntity;
    }

    public void updateRequireMemberCount(int count) {
        this.requireMemberCount += count;
    }

    public void updateCurrentMemberCount(int count) {
        this.currentMemberCount += count;
    }
}
