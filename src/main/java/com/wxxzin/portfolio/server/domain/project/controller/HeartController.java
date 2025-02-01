package com.wxxzin.portfolio.server.domain.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUser;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;
import com.wxxzin.portfolio.server.domain.project.service.HeartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "Project - Heart", description = "프로젝트 좋아요 관련 API")
@RequestMapping("/api/v1/project/heart")
public class HeartController {

    private final HeartService heartService;

    public HeartController(HeartService heartService) {
        this.heartService = heartService;
    }

    @PostMapping("/{projectId}")
    @Operation(summary = "프로젝트 좋아요 / 좋아요 취소", description = "프로젝트를 좋아요 또는 좋아요 취소합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> heartProject(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(value = "projectId") Long projectId
    ) {

        Long userId = principalUser.getUserId();
        String message = heartService.heartProject(userId, projectId);

        return ResponseEntity
            .status(201)
            .body(
                ApiSuccessResponse.of(
                    message
                )
            );
    }
}
