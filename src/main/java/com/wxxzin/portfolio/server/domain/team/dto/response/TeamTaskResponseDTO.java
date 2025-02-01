package com.wxxzin.portfolio.server.domain.team.dto.response;

import com.wxxzin.portfolio.server.domain.team.entity.TeamTaskEntity;

import java.time.LocalDate;

public record TeamTaskResponseDTO(
    Long id,
    String title,
    String description,
    LocalDate deadline,
    String taskStatus,
    int taskPriority,
    String assigneeMemberName
) {
    public static TeamTaskResponseDTO of(
            TeamTaskEntity teamTaskEntity, String assigneeMemberName
    ) {
        return new TeamTaskResponseDTO(
            teamTaskEntity.getId(),
            teamTaskEntity.getTitle(),
            teamTaskEntity.getDescription(),
            teamTaskEntity.getDeadline(),
            teamTaskEntity.getTaskStatus().getStatus(),
            teamTaskEntity.getTaskPriority().getPriority(),
            assigneeMemberName
        );
    }
}
