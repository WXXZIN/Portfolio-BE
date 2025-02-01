package com.wxxzin.portfolio.server.domain.team.dto.request;

public record EditTaskRequestDTO(
    String title,
    String description,
    String deadline,
    String taskStatus,
    int taskPriority,
    String assigneeMemberName
) {
}
