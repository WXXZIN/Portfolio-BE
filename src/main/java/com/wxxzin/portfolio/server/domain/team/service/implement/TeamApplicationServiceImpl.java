package com.wxxzin.portfolio.server.domain.team.service.implement;

import java.util.Comparator;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxxzin.portfolio.server.common.exception.CreationException;
import com.wxxzin.portfolio.server.common.exception.ModificationException;
import com.wxxzin.portfolio.server.common.exception.RemovalException;
import com.wxxzin.portfolio.server.common.exception.RetrievalException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.project.enums.RecruitmentStatus;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamApplicationResponseDTO;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamResponseDTO;
import com.wxxzin.portfolio.server.domain.team.entity.TeamApplication;
import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;
import com.wxxzin.portfolio.server.domain.team.enums.ApplicationStatus;
import com.wxxzin.portfolio.server.domain.team.repository.TeamApplicationRepository;
import com.wxxzin.portfolio.server.domain.team.service.TeamApplicationService;
import com.wxxzin.portfolio.server.domain.team.service.TeamService;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.service.BaseUserService;

@Service
public class TeamApplicationServiceImpl implements TeamApplicationService {

    private static final String ALL = "ALL";
    private static final String APPROVED = "APPROVED";
    private static final String REJECTED = "REJECTED";

    private final TeamApplicationRepository teamApplicationRepository;
    private final BaseUserService baseUserService;
    private final TeamService teamService;

    public TeamApplicationServiceImpl(
        TeamApplicationRepository teamApplicationRepository, 
        BaseUserService baseUserService,
        @Lazy TeamService teamService
    ) {
        this.teamApplicationRepository = teamApplicationRepository;
        this.baseUserService = baseUserService;
        this.teamService = teamService;
    }

    @Transactional
    @Override
    public void applyTeam(Long teamId, Long userId) {
        TeamEntity teamEntity = findTeamEntityById(teamId);
        BaseUserEntity leaderBaseUserEntity = teamEntity.getLeader();
        BaseUserEntity targetBaseUserEntity = findBaseUserEntityById(userId);

        if (leaderBaseUserEntity.equals(targetBaseUserEntity)) {
            throw new CreationException(ErrorMessage.TEAM_LEADER_CANNOT_APPLY);
        }

        ProjectEntity latestProject = teamEntity.getProjectEntities()
            .stream()
            .max(Comparator.comparing(ProjectEntity::getCreatedAt))
            .orElseThrow(() -> new CreationException(ErrorMessage.TEAM_NOT_FOUND));

        RecruitmentStatus recruitmentStatus = latestProject.getRecruitmentStatus();

        if (recruitmentStatus != RecruitmentStatus.RECRUITING) {
            throw new ModificationException(ErrorMessage.TEAM_NOT_RECRUITING);
        }

        if (teamApplicationRepository.existsByTeamEntity_IdAndBaseUserEntity_Id(teamId, userId)) {
            throw new CreationException(ErrorMessage.TEAM_ALREADY_APPLIED);
        }

        teamApplicationRepository.save(
            TeamApplication.builder()
                .teamEntity(teamEntity)
                .baseUserEntity(targetBaseUserEntity)
                .build()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TeamApplicationResponseDTO> getTeamApplicationList(
        Long userId,
        Long teamId,
        String applicationStatus,   
        Pageable pageable
    ) {
        TeamEntity teamEntity = findTeamEntityById(teamId);
        BaseUserEntity leaderBaseUserEntity = teamEntity.getLeader();
        BaseUserEntity targetBaseUserEntity = findBaseUserEntityById(userId);

        if (!leaderBaseUserEntity.equals(targetBaseUserEntity)) {
            throw new RetrievalException(ErrorMessage.NOT_TEAM_LEADER);
        }

        Page<TeamApplication> teamApplicationDTOPage;

        switch (applicationStatus) {
            case ALL -> teamApplicationDTOPage = teamApplicationRepository.findByTeamEntity(teamEntity, pageable);
            case APPROVED -> teamApplicationDTOPage = teamApplicationRepository.findByTeamEntityAndApplicationStatus(teamEntity, ApplicationStatus.APPROVED, pageable);
            case REJECTED -> teamApplicationDTOPage = teamApplicationRepository.findByTeamEntityAndApplicationStatus(teamEntity, ApplicationStatus.REJECTED, pageable);
            default -> teamApplicationDTOPage = teamApplicationRepository.findByTeamEntityAndApplicationStatus(teamEntity, ApplicationStatus.PENDING, pageable);
        }

        return teamApplicationDTOPage.map(TeamApplicationResponseDTO::of);
    }

    @Transactional
    @Override
    public TeamResponseDTO approveTeamApplication(Long userId, Long teamApplicationId) {
        TeamApplication teamApplication = findTeamApplicationById(teamApplicationId);
        BaseUserEntity targetBaseUserEntity = findBaseUserEntityById(userId);

        changeTeamApplicationStatus(teamApplication, targetBaseUserEntity, ApplicationStatus.APPROVED);

        TeamEntity teamEntity = teamApplication.getTeamEntity();
        teamService.addMember(teamEntity.getId(), teamApplication.getBaseUserEntity().getId());

        ProjectEntity latestProject = teamEntity.getProjectEntities()
            .stream()
            .max(Comparator.comparing(ProjectEntity::getCreatedAt))
            .orElseThrow(() -> new ModificationException(ErrorMessage.PROJECT_NOT_FOUND));

        latestProject.updateCurrentMemberCount(1);

        int currentMemberCount = latestProject.getCurrentMemberCount();
        int requiredMemberCount = latestProject.getRequireMemberCount();

        if (currentMemberCount >= requiredMemberCount) {
            latestProject.setRecruitmentStatus(RecruitmentStatus.COMPLETED);
        }

        return TeamResponseDTO.of(teamEntity, userId);
    }

    @Transactional
    @Override
    public void rejectTeamApplication(Long userId, Long teamApplicationId) {
        TeamApplication teamApplication = findTeamApplicationById(teamApplicationId);
        BaseUserEntity targetBaseUserEntity = findBaseUserEntityById(userId);

        changeTeamApplicationStatus(teamApplication, targetBaseUserEntity, ApplicationStatus.REJECTED);
    }

    @Transactional
    @Override
    public void cancelTeamApplication(Long userId, Long teamId) {
        TeamEntity teamEntity = findTeamEntityById(teamId);
        BaseUserEntity targetBaseUserEntity = findBaseUserEntityById(userId);

        TeamApplication teamApplication = teamApplicationRepository.findByTeamEntityAndBaseUserEntity(teamEntity, targetBaseUserEntity)
            .orElseThrow(() -> new RetrievalException(ErrorMessage.TEAM_APPLICATION_NOT_FOUND));

        if (!teamApplication.getBaseUserEntity().equals(targetBaseUserEntity)) {
            throw new RemovalException(ErrorMessage.NOT_TEAM_APPLICATION_OWNER);
        }

        teamApplicationRepository.delete(teamApplication);
    }

    public TeamApplication findTeamApplicationById(Long teamApplicationId) {
        return teamApplicationRepository.findById(teamApplicationId)
            .orElseThrow(() -> new RetrievalException(ErrorMessage.TEAM_APPLICATION_NOT_FOUND));
    }

    protected BaseUserEntity findBaseUserEntityById(Long userId) {
        return baseUserService.findBaseUserEntityById(userId);
    }

    protected TeamEntity findTeamEntityById(Long teamId) {
        return teamService.findTeamEntityById(teamId);
    }

    protected void changeTeamApplicationStatus(
        TeamApplication teamApplication, 
        BaseUserEntity targetBaseUserEntity, 
        ApplicationStatus applicationStatus
    ) {
        TeamEntity teamEntity = teamApplication.getTeamEntity();
        ProjectEntity latestProject = teamEntity.getProjectEntities()
            .stream()
            .max(Comparator.comparing(ProjectEntity::getCreatedAt))
            .orElseThrow(() -> new ModificationException(ErrorMessage.PROJECT_NOT_FOUND));

        RecruitmentStatus recruitmentStatus = latestProject.getRecruitmentStatus();

        if (recruitmentStatus != RecruitmentStatus.RECRUITING) {
            throw new ModificationException(ErrorMessage.TEAM_NOT_RECRUITING);
        }

        BaseUserEntity leaderBaseUserEntity = teamEntity.getLeader();

        if (!leaderBaseUserEntity.equals(targetBaseUserEntity)) {
            throw new ModificationException(ErrorMessage.NOT_TEAM_LEADER);
        }

        teamApplication.updateStatus(applicationStatus);
        teamApplicationRepository.save(teamApplication);
    }
}
