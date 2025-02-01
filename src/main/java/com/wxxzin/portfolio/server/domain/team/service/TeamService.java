package com.wxxzin.portfolio.server.domain.team.service;

import java.util.List;

import com.wxxzin.portfolio.server.domain.team.dto.request.CreateTeamRequestDTO;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamResponseDTO;
import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;

public interface TeamService {
    TeamResponseDTO createTeam(Long userId, CreateTeamRequestDTO createTeamRequestDTO);
    List<TeamResponseDTO> getTeamList(Long userId);
    TeamResponseDTO getTeamInfo(Long userId, Long teamId);
    TeamEntity findTeamEntityById(Long teamId);
    void addMember(Long teamId, Long newMemberId);
    void changeLeader(Long userId, Long teamId, String newLeaderName);
    void leaveTeam(Long userId, Long teamId);
}
