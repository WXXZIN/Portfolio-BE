package com.wxxzin.portfolio.server.domain.team.service;

import java.util.List;
import java.util.Optional;

import com.wxxzin.portfolio.server.domain.team.entity.TeamMemberMapping;

public interface TeamMemberMappingService {
    void save(TeamMemberMapping teamMemberMapping);
    List<TeamMemberMapping> findByBaseUserEntity_Id(Long userId);
    Optional<TeamMemberMapping> findByTeamEntity_IdAndBaseUserEntity_Id(Long userId, Long teamId);
    boolean isTeamMember(Long userId, Long teamId);
    void delete(TeamMemberMapping teamMemberMapping);
}
