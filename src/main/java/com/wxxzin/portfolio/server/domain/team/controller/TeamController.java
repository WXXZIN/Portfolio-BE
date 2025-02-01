package com.wxxzin.portfolio.server.domain.team.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUser;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;
import com.wxxzin.portfolio.server.common.response.success.SuccessMessage;
import com.wxxzin.portfolio.server.domain.team.dto.request.CreateTeamRequestDTO;
import com.wxxzin.portfolio.server.domain.team.dto.response.TeamResponseDTO;
import com.wxxzin.portfolio.server.domain.team.service.TeamService;

@RestController
@Slf4j
@Tag(name = "Team", description = "팀 관련 API")
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @Operation(summary = "팀 생성", description = "팀을 생성합니다.")
    public ResponseEntity<ApiSuccessResponse<TeamResponseDTO>> createTeam(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @RequestBody CreateTeamRequestDTO createTeamRequestDTO
    ) {

        Long userId = principalUser.getUserId();

        TeamResponseDTO teamResponseDTO = teamService.createTeam(userId, createTeamRequestDTO);
        
        return ResponseEntity
            .status(201)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_CREATE_SUCCESS.getMessage(),
                    teamResponseDTO
                )
            );
    }

    @GetMapping
    @Operation(summary = "팀 목록 조회", description = "팀 목록을 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<List<TeamResponseDTO>>> getTeamList(
        @AuthenticationPrincipal PrincipalUser principalUser
    ) {

        Long userId = principalUser.getUserId();

        List<TeamResponseDTO> teamResponseDTOList = teamService.getTeamList(userId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_LIST_RETRIEVE_SUCCESS.getMessage(),
                    teamResponseDTOList
                )
            );
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "팀 정보 조회", description = "팀 정보를 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<TeamResponseDTO>> getTeamInfo(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "teamId") Long teamId
    ) {

        Long userId = principalUser.getUserId();

        TeamResponseDTO teamResponseDTO = teamService.getTeamInfo(userId, teamId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_INFO_RETRIEVE_SUCCESS.getMessage(),
                    teamResponseDTO
                )
            );
    }

    @PutMapping("/{teamId}/change-leader")
    @Operation(summary = "팀장 변경", description = "팀장을 변경합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> changeLeader(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "teamId") Long teamId,
        @RequestBody String newLeaderName
    ) {

        Long userId = principalUser.getUserId();

        teamService.changeLeader(userId, teamId, newLeaderName);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_LEADER_CHANGE_SUCCESS.getMessage()
                )
            );
    }

    @DeleteMapping("/{teamId}")
    @Operation(summary = "팀 나가기", description = "팀에서 나갑니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> leaveTeam(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "teamId") Long teamId
    ) {

        Long userId = principalUser.getUserId();

        teamService.leaveTeam(userId, teamId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.TEAM_LEAVE_SUCCESS.getMessage()
                )
            );
    }
}
