package com.wxxzin.portfolio.server.domain.project.controller;

import com.wxxzin.portfolio.server.domain.project.dto.request.EditProjectRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.wxxzin.portfolio.server.domain.project.dto.request.WriteProjectRequestDTO;
import com.wxxzin.portfolio.server.domain.project.dto.response.ProjectResponseDTO;
import com.wxxzin.portfolio.server.domain.project.service.ProjectService;

@RestController
@Slf4j
@Tag(name = "Project", description = "프로젝트 관련 API")
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "프로젝트 생성", description = "프로젝트를 생성합니다.")
    public ResponseEntity<ApiSuccessResponse<ProjectResponseDTO>> writeProject(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @RequestBody WriteProjectRequestDTO writeProjectRequestDTO
    ) {

        Long userId = principalUser.getUserId();
        ProjectResponseDTO response = projectService.writeProject(userId, writeProjectRequestDTO);

        return ResponseEntity
            .status(201)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.PROJECT_WRITE_SUCCESS.getMessage(),
                    response
                )
            );
    }

    @GetMapping
    @Operation(summary = "프로젝트 목록 조회", description = "프로젝트 목록을 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<PageResponse<ProjectResponseDTO>>> getProjectList(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        
        Long userId = null;

        if (principalUser != null) {
            userId = principalUser.getUserId();
        }

        Pageable pageable = PageRequest.of(page - 1, 5, Sort.Direction.DESC, "createdAt");
        Page<ProjectResponseDTO> response = projectService.getProjectList(userId, pageable);

        PageResponse<ProjectResponseDTO> pageResponse = PageResponse.<ProjectResponseDTO>builder()
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
                    SuccessMessage.PROJECT_LIST_RETRIEVE_SUCCESS.getMessage(),
                    pageResponse
                )
            );
    }

    @GetMapping("/search")
    @Operation(summary = "프로젝트 검색", description = "프로젝트를 검색합니다.")
    public ResponseEntity<ApiSuccessResponse<PageResponse<ProjectResponseDTO>>> getSearchedProjectList(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType,
        @RequestParam(value = "searchKeyword", required = false, defaultValue = "") String searchKeyword,
        @RequestParam(value = "sortBy", required = false, defaultValue = "latest") String sortBy,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        Long userId = null;

        if (principalUser != null) {
            userId = principalUser.getUserId();
        }

        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<ProjectResponseDTO> response = projectService.getSearchedProjectList(userId, searchType, searchKeyword, sortBy, pageable);

        PageResponse<ProjectResponseDTO> pageResponse = PageResponse.<ProjectResponseDTO>builder()
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
                    SuccessMessage.PROJECT_LIST_RETRIEVE_SUCCESS.getMessage(),
                    pageResponse
                )
            );
    }

    @GetMapping("/hearted")
    @Operation(summary = "좋아요한 프로젝트 목록 조회", description = "좋아요한 프로젝트 목록을 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<PageResponse<ProjectResponseDTO>>> getProjectListIsHearted(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        Long userId = principalUser.getUserId();

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "createdAt");
        Page<ProjectResponseDTO> response = projectService.getProjectListIsHearted(userId, pageable);

        PageResponse<ProjectResponseDTO> pageResponse = PageResponse.<ProjectResponseDTO>builder()
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
                    SuccessMessage.PROJECT_LIST_RETRIEVE_SUCCESS.getMessage(),
                    pageResponse
                )
            );
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "프로젝트 정보 조회", description = "프로젝트 정보를 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<ProjectResponseDTO>> getProjectInfo(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(value = "projectId") Long projectId
    ) {
        Long userId = null;

        if (principalUser != null) {
            userId = principalUser.getUserId();
        }

        ProjectResponseDTO response = projectService.getProjectInfo(userId, projectId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.PROJECT_INFO_RETRIEVE_SUCCESS.getMessage(),
                    response
                )
            );
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "프로젝트 수정", description = "프로젝트를 수정합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> editProject(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(value = "projectId") Long projectId,
        @RequestBody EditProjectRequestDTO editProjectRequestDTO
    ) {
        Long userId = principalUser.getUserId();

        projectService.editProject(userId, projectId, editProjectRequestDTO);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.PROJECT_EDIT_SUCCESS.getMessage()
                )
            );
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteProject(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(value = "projectId") Long projectId
    ) {
        Long userId = principalUser.getUserId();

        projectService.deleteProject(userId, projectId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.PROJECT_DELETE_SUCCESS.getMessage()
                )
            );
    }
}
