package com.wxxzin.portfolio.server.domain.team.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUser;
import com.wxxzin.portfolio.server.common.response.PageResponse;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;
import com.wxxzin.portfolio.server.common.response.success.SuccessMessage;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamApplicationResponseDTO;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamResponseDTO;
import com.wxxzin.portfolio.server.domain.team.service.TeamApplicationService;

@RestController
@Slf4j
@Tag(name = "Team - Application", description = "팀 신청 관련 API")
@RequestMapping("/api/v1/team/{teamId}/application")
public class TeamApplicationController {

    private final TeamApplicationService teamApplicationService;

    public TeamApplicationController(TeamApplicationService teamApplicationService) {
        this.teamApplicationService = teamApplicationService;
    }

    @PostMapping
    @Operation(summary = "팀 신청", description = "팀 참가를 신청합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> applyTeam(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "teamId") Long teamId
    ) {
        Long userId = principalUser.getUserId();

        teamApplicationService.applyTeam(teamId, userId);

        return ResponseEntity
            .status(201)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_APPLICATION_APPLY_SUCCESS.getMessage()
                )
            );
    }

    @GetMapping
    @Operation(summary = "팀 신청 목록 조회", description = "팀 신청 목록을 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<PageResponse<TeamApplicationResponseDTO>>> getTeamApplicationList(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "teamId") Long teamId,
        @RequestParam(name = "applicationStatus", required = false) String applicationStatus,
        @RequestParam(name = "page", required = false, defaultValue = "1") int page
    ) {

        Long userId = principalUser.getUserId();

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "createdAt");
        Page<TeamApplicationResponseDTO> response = teamApplicationService.getTeamApplicationList(userId, teamId, applicationStatus, pageable);

        PageResponse<TeamApplicationResponseDTO> pageResponse = PageResponse.<TeamApplicationResponseDTO>builder()
            .content(response.getContent())
            .pageNumber(response.getNumber())
            .pageSize(response.getSize())
            .totalElements(response.getTotalElements())
            .totalPages(response.getTotalPages())
            .build();

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_APPLICATION_LIST_RETRIEVE_SUCCESS.getMessage(),
                    pageResponse
                )
            );
    }

    @PutMapping("/{teamApplicationId}")
    @Operation(summary = "팀 신청 처리", description = "팀 신청을 승인 또는 거절합니다.")
    public ResponseEntity<ApiSuccessResponse<?>> processTeamApplication(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "teamApplicationId") Long teamApplicationId,
        @RequestParam(name = "action") String action
    ) {

        Long userId = principalUser.getUserId();

        if ("approve".equals(action)) {
            TeamResponseDTO response = teamApplicationService.approveTeamApplication(userId, teamApplicationId);

            return ResponseEntity
                .status(200)
                .body(
                    ApiSuccessResponse.of(
                        SuccessMessage.TEAM_APPLICATION_APPROVE_SUCCESS.getMessage(),
                        response
                    )
                );
        } else if ("reject".equals(action)) {
            teamApplicationService.rejectTeamApplication(userId, teamApplicationId);

            return ResponseEntity
                .status(200)
                .body(
                    ApiSuccessResponse.of(
                        SuccessMessage.TEAM_APPLICATION_REJECT_SUCCESS.getMessage()
                    )
                );
        }

        return null;
    }

    @DeleteMapping
    @Operation(summary = "팀 신청 삭제", description = "팀 신청을 취소합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteTeamApplication(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "teamId") Long teamId
    ) {
        
        Long userId = principalUser.getUserId();

        teamApplicationService.cancelTeamApplication(userId, teamId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_APPLICATION_DELETE_SUCCESS.getMessage()
                )
            );
    }
}
