package com.wxxzin.portfolio.server.domain.project.dto.request;

import java.util.List;

public record EditProjectRequestDTO(
    String title,
    String content,
    int requireMemberCount,
    String deadline,
    List<String> tags
) {
}
