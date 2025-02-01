package com.wxxzin.portfolio.server.domain.team.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wxxzin.portfolio.server.domain.team.dto.response.TeamApplicationResponseDTO;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamResponseDTO;

public interface TeamApplicationService {
    void applyTeam(Long teamId, Long userId);
    Page<TeamApplicationResponseDTO> getTeamApplicationList(Long userId, Long teamId, String applicationStatus, Pageable pageable);
    TeamResponseDTO approveTeamApplication(Long userId, Long teamApplicationId);
    void rejectTeamApplication(Long userId, Long teamApplicationId);
    void cancelTeamApplication(Long userId, Long teamId);
}
