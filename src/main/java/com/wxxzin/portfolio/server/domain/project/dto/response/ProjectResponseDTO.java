package com.wxxzin.portfolio.server.domain.project.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;

public record ProjectResponseDTO(
    Long id,
    Long teamId,
    LocalDate createdAt,
    String title,
    String content,
    String writerName,
    int viewCount,
    int requireMemberCount,
    int currentMemberCount,
    LocalDate deadline,
    String recruitmentStatus,
    List<String> tags,
    int heartCount,
    boolean isHearted,
    boolean isApplied,
    boolean isTeamMember
) {
    public static ProjectResponseDTO of(
        ProjectEntity projectEntity, boolean isHearted, boolean isApplied, boolean isTeamMember
    ) {
        List<String> tags = projectEntity.getProjectTagMappings() != null ?
                projectEntity.getProjectTagMappings().stream()
                        .map(projectTagMapping -> projectTagMapping.getTagEntity().getTagName())
                        .toList()
                : List.of();

        return new ProjectResponseDTO(
            projectEntity.getId(),
            projectEntity.getTeamEntity().getId(),
            projectEntity.getCreatedAt().toLocalDate(),
            projectEntity.getTitle(),
            projectEntity.getContent(),
            projectEntity.getBaseUserEntity().getNickname(),
            projectEntity.getViewCount(),
            projectEntity.getNewRequireMemberCount(),
            projectEntity.getCurrentMemberCount(),
            projectEntity.getDeadline(),
            projectEntity.getRecruitmentStatus().getStatus(),
            tags,
            projectEntity.getHeartEntities() != null ? projectEntity.getHeartEntities().size() : 0,
            isHearted,
            isApplied,
            isTeamMember
        );
    }
}
