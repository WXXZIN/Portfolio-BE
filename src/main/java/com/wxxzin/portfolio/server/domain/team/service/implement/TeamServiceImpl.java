package com.wxxzin.portfolio.server.domain.team.service.implement;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxxzin.portfolio.server.common.exception.ModificationException;
import com.wxxzin.portfolio.server.common.exception.RemovalException;
import com.wxxzin.portfolio.server.common.exception.RetrievalException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.team.dto.request.CreateTeamRequestDTO;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamResponseDTO;
import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;
import com.wxxzin.portfolio.server.domain.team.entity.TeamMemberMapping;
import com.wxxzin.portfolio.server.domain.team.repository.TeamRepository;
import com.wxxzin.portfolio.server.domain.team.service.TeamMemberMappingService;
import com.wxxzin.portfolio.server.domain.team.service.TeamService;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.service.BaseUserService;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final BaseUserService baseUserService;
    private final TeamMemberMappingService teamMemberMappingService;

    public TeamServiceImpl(
        TeamRepository teamRepository,
        BaseUserService baseUserService,
        TeamMemberMappingService teamMemberMappingService
    ) {
        this.teamRepository = teamRepository;
        this.baseUserService = baseUserService;
        this.teamMemberMappingService = teamMemberMappingService;
    }

    @Override
    @Transactional
    public TeamResponseDTO createTeam(Long userId, CreateTeamRequestDTO createTeamRequestDTO) {
        
        BaseUserEntity leader = findBaseUserEntityById(userId);

        TeamEntity teamEntity = TeamEntity.builder()
            .name(createTeamRequestDTO.name())
            .leader(leader)
            .build();

        teamRepository.save(teamEntity);

        TeamMemberMapping teamMemberMapping = TeamMemberMapping.builder()
            .teamEntity(teamEntity)
            .baseUserEntity(leader)
            .build();

            teamMemberMappingService.save(teamMemberMapping);

        return TeamResponseDTO.of(teamEntity, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponseDTO> getTeamList(Long userId) {
        
        List<TeamMemberMapping> teamMemberMappings = teamMemberMappingService.findByBaseUserEntity_Id(userId);
        List<TeamEntity> teamEntities = teamMemberMappings.stream()
            .map(TeamMemberMapping::getTeamEntity)
            .distinct()
            .toList();

        return teamEntities.stream()
            .map(teamEntity -> TeamResponseDTO.of(teamEntity, userId))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponseDTO getTeamInfo(Long userId, Long teamId) {
        
        TeamEntity teamEntity = findTeamEntityById(teamId);

        if (teamEntity.getTeamMemberMappings().stream()
            .noneMatch(teamMemberMapping -> teamMemberMapping.getBaseUserEntity().getId().equals(userId))) {
            throw new RetrievalException(ErrorMessage.NOT_TEAM_MEMBER);
        }

        return TeamResponseDTO.of(teamEntity, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamEntity findTeamEntityById(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new RetrievalException(ErrorMessage.TEAM_NOT_FOUND));
    }

    @Override
    @Transactional
    public void addMember(Long teamId, Long newMemberId) {

        TeamEntity teamEntity = findTeamEntityById(teamId);

        BaseUserEntity newMember = findBaseUserEntityById(newMemberId);

        TeamMemberMapping teamMemberMapping = TeamMemberMapping.builder()
            .teamEntity(teamEntity)
            .baseUserEntity(newMember)
            .build();

        teamMemberMappingService.save(teamMemberMapping);
    }

    @Override
    @Transactional
    public void changeLeader(Long userId, Long teamId, String newLeaderName) {

        TeamEntity teamEntity = findTeamEntityById(teamId);
        Long leaderId = teamEntity.getLeader().getId();

        if (!leaderId.equals(userId)) {
            throw new ModificationException(ErrorMessage.NOT_TEAM_LEADER);
        }

        BaseUserEntity newLeader = findBaseUserEntityByNickname(newLeaderName);

        teamEntity.updateLeader(newLeader);
        teamRepository.save(teamEntity);
    }

    @Override
    @Transactional
    public void leaveTeam(Long userId, Long teamId) {

        TeamEntity teamEntity = findTeamEntityById(teamId);
        ProjectEntity latestProject = teamEntity.getProjectEntities()
            .stream()
            .max(Comparator.comparing(ProjectEntity::getCreatedAt))
            .orElse(null);
        Long leaderId = teamEntity.getLeader().getId();

        if (latestProject != null && teamEntity.getTeamMemberMappings().size() > 1) {
            if (leaderId.equals(userId)) {
                throw new RemovalException(ErrorMessage.LEADER_CANNOT_LEAVE_TEAM);
            }

            if (!teamMemberMappingService.isTeamMember(userId, teamId)) {
                throw new RemovalException(ErrorMessage.NOT_TEAM_MEMBER);
            }
        }

        if (latestProject != null) {
            latestProject.updateCurrentMemberCount(latestProject.getCurrentMemberCount() - 1);
        }

        TeamMemberMapping teamMemberMapping = teamMemberMappingService.findByTeamEntity_IdAndBaseUserEntity_Id(userId, teamId)
                .orElseThrow(() -> new RemovalException(ErrorMessage.NOT_TEAM_MEMBER));

        teamMemberMappingService.delete(teamMemberMapping);

        if (leaderId.equals(userId)) {
            teamRepository.delete(teamEntity);
        }
    }

    protected BaseUserEntity findBaseUserEntityById(Long userId) {
        return baseUserService.findBaseUserEntityById(userId);
    }

    protected BaseUserEntity findBaseUserEntityByNickname(String nickname) {
        return baseUserService.findBaseUserEntityByNickname(nickname);
    }
}
