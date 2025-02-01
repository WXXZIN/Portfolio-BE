package com.wxxzin.portfolio.server.domain.team.dto.response;

import java.util.List;

import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;
import com.wxxzin.portfolio.server.domain.team.entity.TeamMemberMapping;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

public record TeamResponseDTO(
    Long id,
    String name,
    String leaderName,
    List<String> memberNames,
    int memberCount,
    String projectName,
    String projectStatus,
    boolean isTeamLeader
) {
    public static TeamResponseDTO of(TeamEntity teamEntity, Long userId) {

        List<String> memberNames = teamEntity.getTeamMemberMappings() != null ?
            teamEntity.getTeamMemberMappings().stream()
                .map(TeamMemberMapping::getBaseUserEntity)
                .map(BaseUserEntity::getNickname)
                .toList()
            : List.of();

        List<ProjectEntity> projectEntities = teamEntity.getProjectEntities() != null 
            ? teamEntity.getProjectEntities() 
            : List.of();
    
        ProjectEntity lastProject = projectEntities.isEmpty()
            ? null
            : projectEntities.get(projectEntities.size() - 1);

        String projectName = lastProject != null ? lastProject.getTitle() : null;
        String projectStatus = lastProject != null ? lastProject.getRecruitmentStatus().getStatus() : null;

        boolean isTeamLeader = teamEntity.getLeader().getId().equals(userId);

        return new TeamResponseDTO(
            teamEntity.getId(),
            teamEntity.getName(),
            teamEntity.getLeader().getNickname(),
            memberNames,
            memberNames.size(),
            projectName,
            projectStatus,
            isTeamLeader
        );
    }
}
