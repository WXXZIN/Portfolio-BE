package com.wxxzin.portfolio.server.domain.team.service.implement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxxzin.portfolio.server.common.exception.RetrievalException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.team.dto.request.AddTaskRequestDTO;
import com.wxxzin.portfolio.server.domain.team.dto.request.EditTaskRequestDTO;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamTaskResponseDTO;
import com.wxxzin.portfolio.server.domain.team.entity.TeamTaskEntity;
import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;
import com.wxxzin.portfolio.server.domain.team.enums.TaskPriority;
import com.wxxzin.portfolio.server.domain.team.enums.TaskStatus;
import com.wxxzin.portfolio.server.domain.team.repository.TeamTaskRepository;
import com.wxxzin.portfolio.server.domain.team.service.TeamTaskService;
import com.wxxzin.portfolio.server.domain.team.service.TeamService;

import java.time.LocalDate;

@Service
public class TeamTaskServiceImpl implements TeamTaskService {

    private final TeamTaskRepository teamTaskRepository;
    private final TeamService teamService;

    public TeamTaskServiceImpl(
            TeamTaskRepository teamTaskRepository,
            TeamService teamService
    ) {
        this.teamTaskRepository = teamTaskRepository;
        this.teamService = teamService;
    }

    @Override
    @Transactional
    public TeamTaskResponseDTO addTeamTask(Long userId, Long teamId, AddTaskRequestDTO addTaskRequestDTO) {

        TeamEntity teamEntity = findTeamEntityById(teamId);
        checkUserIsTeamMember(teamEntity, userId);

        TeamTaskEntity teamTaskEntity = TeamTaskEntity.builder()
                .title(addTaskRequestDTO.title())
                .description(addTaskRequestDTO.description())
                .deadline(LocalDate.parse(addTaskRequestDTO.deadline()))
                .taskPriority(TaskPriority.of(addTaskRequestDTO.taskPriority()))
                .assigneeMemberName(addTaskRequestDTO.assigneeMemberName())
                .teamEntity(teamEntity)
                .build();

        teamTaskRepository.save(teamTaskEntity);

        return TeamTaskResponseDTO.of(teamTaskEntity, teamTaskEntity.getAssigneeMemberName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamTaskResponseDTO> getTeamTaskList(Long userId, Long teamId, String taskStatus, Pageable pageable) {

        TeamEntity teamEntity = findTeamEntityById(teamId);
        checkUserIsTeamMember(teamEntity, userId);

        Page<TeamTaskEntity> taskEntities = teamTaskRepository.findByTeamEntityAndTaskStatus(teamEntity, TaskStatus.valueOf(taskStatus), pageable);

        return taskEntities.map(teamTaskEntity -> TeamTaskResponseDTO.of(teamTaskEntity, teamTaskEntity.getAssigneeMemberName()));
    }

    @Override
    @Transactional(readOnly = true)
    public TeamTaskResponseDTO getTeamTaskInfo(Long userId, Long taskId) {

        TeamTaskEntity teamTaskEntity = findTaskEntityById(taskId);

        return TeamTaskResponseDTO.of(teamTaskEntity, teamTaskEntity.getAssigneeMemberName());
    }

    @Override
    @Transactional
    public void editTeamTask(Long userId, Long taskId, EditTaskRequestDTO editTaskRequestDTO) {

        TeamTaskEntity teamTaskEntity = findTaskEntityById(taskId);

        if (teamTaskEntity.getTaskStatus() == TaskStatus.DONE) {
            throw new RetrievalException(ErrorMessage.NOT_FOUND_TASK);
        }

        checkUserIsTeamMember(teamTaskEntity.getTeamEntity(), userId);

        teamTaskEntity.setTitle(editTaskRequestDTO.title());
        teamTaskEntity.setDescription(editTaskRequestDTO.description());
        teamTaskEntity.setDeadline(LocalDate.parse(editTaskRequestDTO.deadline()));
        teamTaskEntity.setTaskStatus(TaskStatus.of(editTaskRequestDTO.taskStatus()));
        teamTaskEntity.setTaskPriority(TaskPriority.of(editTaskRequestDTO.taskPriority()));
        teamTaskEntity.setAssigneeMemberName(editTaskRequestDTO.assigneeMemberName());

        teamTaskRepository.save(teamTaskEntity);
    }

    @Override
    @Transactional
    public void deleteTeamTask(Long taskId, Long userId) {

        TeamTaskEntity teamTaskEntity = findTaskEntityById(taskId);

        if (teamTaskEntity.getTaskStatus() == TaskStatus.DONE) {
            throw new RetrievalException(ErrorMessage.NOT_FOUND_TASK);
        }

        checkUserIsTeamMember(teamTaskEntity.getTeamEntity(), userId);

        teamTaskRepository.delete(teamTaskEntity);
    }

    private void checkUserIsTeamMember(TeamEntity teamEntity, Long userId) {
        if (teamEntity.getTeamMemberMappings().stream()
                .noneMatch(teamMemberMapping -> teamMemberMapping.getBaseUserEntity().getId().equals(userId))) {
            throw new RetrievalException(ErrorMessage.NOT_TEAM_MEMBER);
        }
    }

    public TeamTaskEntity findTaskEntityById(Long taskId) {
        return teamTaskRepository.findById(taskId)
            .orElseThrow(() -> new RetrievalException(ErrorMessage.NOT_FOUND_TASK));
    }

    protected TeamEntity findTeamEntityById(Long teamId) {
        return teamService.findTeamEntityById(teamId);
    }
}
