package com.wxxzin.portfolio.server.domain.project.service.implement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.exception.CreationException;
import com.wxxzin.portfolio.server.common.exception.RemovalException;
import com.wxxzin.portfolio.server.common.exception.RetrievalException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.project.dto.request.EditProjectRequestDTO;
import com.wxxzin.portfolio.server.domain.project.dto.request.WriteProjectRequestDTO;
import com.wxxzin.portfolio.server.domain.project.dto.response.ProjectResponseDTO;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectTagMapping;
import com.wxxzin.portfolio.server.domain.project.entity.TagEntity;
import com.wxxzin.portfolio.server.domain.project.enums.RecruitmentStatus;
import com.wxxzin.portfolio.server.domain.project.repository.ProjectRepository;
import com.wxxzin.portfolio.server.domain.project.service.ProjectService;
import com.wxxzin.portfolio.server.domain.project.service.ProjectTagMappingService;
import com.wxxzin.portfolio.server.domain.project.service.TagService;
import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;
import com.wxxzin.portfolio.server.domain.team.service.TeamService;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.service.BaseUserService;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final BaseUserService baseUserService;
    private final TeamService teamService;
    private final TagService tagService;
    private final ProjectTagMappingService projectTagMappingService;

    public ProjectServiceImpl(
        ProjectRepository projectRepository,
        BaseUserService baseUserService,
        TeamService teamService,
        TagService tagService,
        ProjectTagMappingService projectTagMappingService
    ) {
        this.projectRepository = projectRepository;
        this.baseUserService = baseUserService;
        this.teamService = teamService;
        this.tagService = tagService;
        this.projectTagMappingService = projectTagMappingService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateRecruitmentStatuses() {

        projectRepository.findAll().forEach(projectEntity -> {
            if (projectEntity.getDeadline().isBefore(LocalDate.now())
                && projectEntity.getRecruitmentStatus() == RecruitmentStatus.RECRUITING
                && projectEntity.getCurrentMemberCount() < projectEntity.getRequireMemberCount()) {
                projectEntity.setRecruitmentStatus(RecruitmentStatus.CANCELED);
            }
        });
    }

    @Transactional
    @Override
    public ProjectResponseDTO writeProject(Long userId, WriteProjectRequestDTO writeProjectRequestDTO) {

        String title = writeProjectRequestDTO.title();
        String content = writeProjectRequestDTO.content();
        int requireMemberCount = writeProjectRequestDTO.requireMemberCount();
        String deadline = writeProjectRequestDTO.deadline();
        Long teamId = writeProjectRequestDTO.teamId();
        List<String> tags = writeProjectRequestDTO.tags();

        BaseUserEntity baseUserEntity = findBaseUserEntityById(userId);
        TeamEntity teamEntity = findTeamEntityById(teamId);

        ProjectEntity latestProject = teamEntity.getProjectEntities()
            .stream()
            .max(Comparator.comparing(ProjectEntity::getCreatedAt))
            .orElse(null);

        if (latestProject != null && latestProject.getRecruitmentStatus() == RecruitmentStatus.RECRUITING) {
            throw new CreationException(ErrorMessage.PROJECT_ALREADY_RECRUITING);
        }

        ProjectEntity projectEntity = ProjectEntity.builder()
            .title(title)
            .content(content)
            .newRequireMemberCount(requireMemberCount)
            .deadline(LocalDate.parse(deadline))
            .baseUserEntity(baseUserEntity)
            .teamEntity(teamEntity)
            .build();

        if (latestProject != null) {
            projectEntity.updateRequireMemberCount(teamEntity.getTeamMemberMappings().size() + requireMemberCount);
        } else {
            projectEntity.setRequireMemberCount(teamEntity.getTeamMemberMappings().size() + requireMemberCount);
        }

        projectEntity.updateCurrentMemberCount(teamEntity.getTeamMemberMappings().size());
        projectRepository.save(projectEntity);

        List<ProjectTagMapping> projectTagMappings = processTags(projectEntity, tags);
        projectTagMappingService.saveProjectTagMapping(projectTagMappings);

        return ProjectResponseDTO.of(projectEntity, false, false, false);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProjectResponseDTO> getProjectList(
        Long userId,
        Pageable pageable
    ) {
        Page<ProjectEntity> projectEntityDTOPage = projectRepository.findAll(pageable);

        return projectEntityDTOPage.map(projectEntity -> mapProjectEntityToResponseDTO(projectEntity, userId));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProjectResponseDTO> getSearchedProjectList(
        Long userId,
        String searchType,
        String searchKeyword,
        String sortBy,
        Pageable pageable
    ) {
        Page<ProjectEntity> projectPageResponseDTO;

        projectPageResponseDTO = projectRepository.searchProjects(searchType, searchKeyword, sortBy, pageable);

        return projectPageResponseDTO.map(projectEntity -> mapProjectEntityToResponseDTO(projectEntity, userId));
    }

    private ProjectResponseDTO mapProjectEntityToResponseDTO(ProjectEntity projectEntity, Long userId) {
        boolean isHearted = false;

        if (userId != null) {
            isHearted = projectEntity.getHeartEntities().stream()
                    .anyMatch(heartEntity -> heartEntity.getBaseUserEntity().getId().equals(userId));
        }

        return ProjectResponseDTO.of(projectEntity, isHearted, false, false);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProjectResponseDTO> getProjectListIsHearted(Long userId, Pageable pageable) {
        
        Page<ProjectEntity> projectEntityDTOPage = projectRepository.findByHeartEntities_BaseUserEntity_Id(userId, pageable);

        return projectEntityDTOPage.map(projectEntity -> {
            boolean isHearted = true;
            return ProjectResponseDTO.of(projectEntity, isHearted, false, false);
        });
    }

    @Transactional
    @Override
    public ProjectResponseDTO getProjectInfo(Long userId, Long projectId) {

        ProjectEntity projectEntity = findProjectEntityById(projectId);

        boolean isHearted = false;
        boolean isApplied = false;
        boolean isTeamMember = false;

        if (userId != null) {
            isHearted = projectEntity.getHeartEntities().stream()
                .anyMatch(heartEntity -> heartEntity.getBaseUserEntity().getId().equals(userId));

            isApplied = projectEntity.getTeamEntity().getTeamApplications().stream()
                .anyMatch(teamApplication -> teamApplication.getBaseUserEntity().getId().equals(userId));

            isTeamMember = projectEntity.getTeamEntity().getTeamMemberMappings().stream()
                .anyMatch(teamMemberMapping -> teamMemberMapping.getBaseUserEntity().getId().equals(userId));
        }

        projectRepository.increaseViewCount(projectId);

        return ProjectResponseDTO.of(projectEntity, isHearted, isApplied, isTeamMember);
    }

    @Transactional(readOnly = true)
    @Override
    public ProjectEntity findProjectEntityById(Long projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new RetrievalException(ErrorMessage.PROJECT_NOT_FOUND));
    }

    @Transactional
    @Override
    public void editProject(Long userId, Long projectId, EditProjectRequestDTO editProjectRequestDTO) {

        ProjectEntity projectEntity = findProjectEntityById(projectId);

        if (!projectEntity.getBaseUserEntity().getId().equals(userId)) {
            throw new RetrievalException(ErrorMessage.NOT_PROJECT_OWNER);
        }

        TeamEntity teamEntity = projectEntity.getTeamEntity();

        String title = editProjectRequestDTO.title();
        String content = editProjectRequestDTO.content();
        int requireMemberCount = editProjectRequestDTO.requireMemberCount();
        String deadline = editProjectRequestDTO.deadline();
        List<String> tags = editProjectRequestDTO.tags();

        projectEntity.setTitle(title);
        projectEntity.setContent(content);
        projectEntity.setNewRequireMemberCount(requireMemberCount);
        projectEntity.setRequireMemberCount(teamEntity.getTeamMemberMappings().size() + requireMemberCount);
        projectEntity.setDeadline(LocalDate.parse(deadline));

        projectRepository.save(projectEntity);

        projectTagMappingService.deleteAllByProjectEntity(projectEntity);

        List<ProjectTagMapping> projectTagMappings = processTags(projectEntity, tags);
        projectTagMappingService.saveProjectTagMapping(projectTagMappings);
    }

    @Transactional
    @Override
    public void deleteProject(Long userId, Long projectId) {

        ProjectEntity projectEntity = findProjectEntityById(projectId);

        if (!projectEntity.getBaseUserEntity().getId().equals(userId)) {
            throw new RemovalException(ErrorMessage.NOT_PROJECT_OWNER);
        }

        projectRepository.delete(projectEntity);
    }

    protected List<ProjectTagMapping> processTags(ProjectEntity projectEntity, List<String> tags) {

        List<ProjectTagMapping> projectTagMappings = new ArrayList<>();

        if (tags != null && !tags.isEmpty()) {
            Set<String> uniqueTags = new HashSet<>(tags);

            uniqueTags.forEach(tag -> {
                TagEntity tagEntity = tagService.findTagEntityByTagName(tag);

                ProjectTagMapping projectTagMapping = ProjectTagMapping.builder()
                        .projectEntity(projectEntity)
                        .tagEntity(tagEntity)
                        .build();

                projectTagMappings.add(projectTagMapping);
            });
        }

        return projectTagMappings;
    }

    protected BaseUserEntity findBaseUserEntityById(Long userId) {
        return baseUserService.findBaseUserEntityById(userId);
    }

    protected TeamEntity findTeamEntityById(Long teamId) {
        return teamService.findTeamEntityById(teamId);
    }
}
