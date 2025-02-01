package com.wxxzin.portfolio.server.domain.team.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUser;
import com.wxxzin.portfolio.server.common.response.PageResponse;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;
import com.wxxzin.portfolio.server.common.response.success.SuccessMessage;
import com.wxxzin.portfolio.server.domain.team.dto.request.AddTaskRequestDTO;
import com.wxxzin.portfolio.server.domain.team.dto.request.EditTaskRequestDTO;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamTaskResponseDTO;
import com.wxxzin.portfolio.server.domain.team.service.TeamTaskService;

@RestController
@Slf4j
@Tag(name = "Team - Task", description = "팀 업무 관련 API")
@RequestMapping("/api/v1/team/{teamId}")
public class TeamTaskController {

    private final TeamTaskService teamTaskService;

    public TeamTaskController(TeamTaskService teamTaskService) {
        this.teamTaskService = teamTaskService;
    }

    @PostMapping("/task")
    @Operation(summary = "팀 업무 추가", description = "팀 업무를 추가합니다.")
    public ResponseEntity<ApiSuccessResponse<TeamTaskResponseDTO>> addTeamTask(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "teamId") Long teamId,
        @RequestBody AddTaskRequestDTO addTaskRequestDTO
    ) {

        Long userId = principalUser.getUserId();

        TeamTaskResponseDTO response = teamTaskService.addTeamTask(userId, teamId, addTaskRequestDTO);

        return ResponseEntity
            .status(201)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_TASK_ADD_SUCCESS.getMessage(),
                    response
                )
            );
    }

    @GetMapping("/task")
    @Operation(summary = "팀 업무 목록 조회", description = "팀 업무 목록을 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<PageResponse<TeamTaskResponseDTO>>> getTeamTaskList(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "teamId") Long teamId,
        @RequestParam(value = "taskStatus", required = false, defaultValue = "TODO") String taskStatus,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {

        Long userId = principalUser.getUserId();

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<TeamTaskResponseDTO> response = teamTaskService.getTeamTaskList(userId, teamId, taskStatus, pageable);

        List<TeamTaskResponseDTO> sortedContent = response.getContent().stream()
            .sorted(Comparator.comparingInt(TeamTaskResponseDTO::taskPriority))
            .toList();

        PageResponse<TeamTaskResponseDTO> pageResponse = PageResponse.<TeamTaskResponseDTO>builder()
            .content(sortedContent)
            .pageNumber(response.getNumber())
            .pageSize(response.getSize())
            .totalElements(response.getTotalElements())
            .totalPages(response.getTotalPages())
            .build();

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_TASK_LIST_RETRIEVAL_SUCCESS.getMessage(),
                    pageResponse
                )
            );
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "팀 업무 조회", description = "팀 업무를 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<TeamTaskResponseDTO>> getTeamTaskInfo(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "taskId") Long taskId
    ) {

        Long userId = principalUser.getUserId();

        TeamTaskResponseDTO response = teamTaskService.getTeamTaskInfo(userId, taskId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_TASK_INFO_RETRIEVAL_SUCCESS.getMessage(),
                    response
                )
            );
    }

    @PutMapping("/task/{taskId}")
    @Operation(summary = "팀 업무 수정", description = "팀 업무를 수정합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> editTeamTask(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "taskId") Long taskId,
        @RequestBody EditTaskRequestDTO editTaskRequestDTO
    ) {

        Long userId = principalUser.getUserId();

        teamTaskService.editTeamTask(userId, taskId, editTaskRequestDTO);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_TASK_EDIT_SUCCESS.getMessage()
                )
            );
    }

    @DeleteMapping("/task/{taskId}")
    @Operation(summary = "팀 업무 삭제", description = "팀 업무를 삭제합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteTeamTask(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "taskId") Long taskId
    ) {

        Long userId = principalUser.getUserId();

        teamTaskService.deleteTeamTask(taskId, userId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_TASK_DELETE_SUCCESS.getMessage()
                )
            );
    }
}
