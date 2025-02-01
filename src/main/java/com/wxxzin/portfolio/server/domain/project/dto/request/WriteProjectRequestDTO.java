package com.wxxzin.portfolio.server.domain.project.dto.request;

import java.util.List;

public record WriteProjectRequestDTO(
    String title,
    String content,
    int requireMemberCount,
    String deadline,
    Long teamId,
    List<String> tags
) {
}
