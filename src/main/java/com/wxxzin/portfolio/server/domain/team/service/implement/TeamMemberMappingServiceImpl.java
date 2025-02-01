package com.wxxzin.portfolio.server.domain.team.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wxxzin.portfolio.server.domain.team.entity.TeamMemberMapping;
import com.wxxzin.portfolio.server.domain.team.repository.TeamMemberMappingRepository;
import com.wxxzin.portfolio.server.domain.team.service.TeamMemberMappingService;

@Service
public class TeamMemberMappingServiceImpl implements TeamMemberMappingService {

    private final TeamMemberMappingRepository teamMemberMappingRepository;

    public TeamMemberMappingServiceImpl(TeamMemberMappingRepository teamMemberMappingRepository) {
        this.teamMemberMappingRepository = teamMemberMappingRepository;
    }

    @Override
    public void save(TeamMemberMapping teamMemberMapping) {
        teamMemberMappingRepository.save(teamMemberMapping);
    }

    @Override
    public List<TeamMemberMapping> findByBaseUserEntity_Id(Long userId) {
        return teamMemberMappingRepository.findByBaseUserEntity_Id(userId);
    }

    @Override
    public Optional<TeamMemberMapping> findByTeamEntity_IdAndBaseUserEntity_Id(Long userId, Long teamId) {
        TeamMemberMapping teamMemberMapping = teamMemberMappingRepository.findByTeamEntity_IdAndBaseUserEntity_Id(teamId, userId);

        return Optional.ofNullable(teamMemberMapping);
    }

    @Override
    public boolean isTeamMember(Long teamId, Long userId) {
        return teamMemberMappingRepository.existsByTeamEntity_IdAndBaseUserEntity_Id(userId, teamId);
    }

    @Override
    public void delete(TeamMemberMapping teamMemberMapping) {
        teamMemberMappingRepository.delete(teamMemberMapping);
    }
}
