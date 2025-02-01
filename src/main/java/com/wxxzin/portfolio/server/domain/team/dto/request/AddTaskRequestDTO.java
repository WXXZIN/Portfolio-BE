package com.wxxzin.portfolio.server.domain.team.dto.request;

public record AddTaskRequestDTO(
    String title,
    String description,
    String deadline,
    int taskPriority,
    String assigneeMemberName
) {
}
