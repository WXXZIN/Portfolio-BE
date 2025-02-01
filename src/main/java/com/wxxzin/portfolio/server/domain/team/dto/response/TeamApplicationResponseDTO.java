package com.wxxzin.portfolio.server.domain.team.dto.response;

import java.time.LocalDate;

import com.wxxzin.portfolio.server.domain.team.entity.TeamApplication;

public record TeamApplicationResponseDTO(
    Long id,
    String nickname,
    String applicationStatus,
    LocalDate applicationDate
) {
    public static TeamApplicationResponseDTO of(
        TeamApplication teamApplication
    ) {
        return new TeamApplicationResponseDTO(
            teamApplication.getId(),
            teamApplication.getBaseUserEntity() != null ? teamApplication.getBaseUserEntity().getNickname() : "익명",
            teamApplication.getApplicationStatus().getStatus(),
            teamApplication.getCreatedAt().toLocalDate()
        );
    }
}
