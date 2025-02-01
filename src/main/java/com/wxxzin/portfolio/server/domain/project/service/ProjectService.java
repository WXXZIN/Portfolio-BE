package com.wxxzin.portfolio.server.domain.project.service;

import com.wxxzin.portfolio.server.domain.project.dto.request.EditProjectRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wxxzin.portfolio.server.domain.project.dto.request.WriteProjectRequestDTO;
import com.wxxzin.portfolio.server.domain.project.dto.response.ProjectResponseDTO;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;

public interface ProjectService {
    ProjectResponseDTO writeProject(Long userId, WriteProjectRequestDTO writeProjectRequestDTO);
    Page<ProjectResponseDTO> getProjectList(Long userId, Pageable pageable);
    Page<ProjectResponseDTO> getSearchedProjectList(Long userId, String searchType, String searchKeyword, String sortBy, Pageable pageable);
    Page<ProjectResponseDTO> getProjectListIsHearted(Long userId, Pageable pageable);
    ProjectResponseDTO getProjectInfo(Long userId, Long projectId);
    ProjectEntity findProjectEntityById(Long projectId);
    void editProject(Long userId, Long projectId, EditProjectRequestDTO editProjectRequestDTO);
    void deleteProject(Long userId, Long projectId);
}
