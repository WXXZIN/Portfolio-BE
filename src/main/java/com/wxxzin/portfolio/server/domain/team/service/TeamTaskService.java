package com.wxxzin.portfolio.server.domain.team.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wxxzin.portfolio.server.domain.team.dto.request.AddTaskRequestDTO;
import com.wxxzin.portfolio.server.domain.team.dto.request.EditTaskRequestDTO;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamTaskResponseDTO;

public interface TeamTaskService {
    TeamTaskResponseDTO addTeamTask(Long userId, Long teamId, AddTaskRequestDTO addTaskRequestDTO);
    Page<TeamTaskResponseDTO> getTeamTaskList(Long userId, Long teamId, String taskStatus, Pageable pageable);
    TeamTaskResponseDTO getTeamTaskInfo(Long userId, Long teamId);
    void editTeamTask(Long userId, Long taskId, EditTaskRequestDTO editTaskRequestDTO);
    void deleteTeamTask(Long userId, Long taskId);
}
