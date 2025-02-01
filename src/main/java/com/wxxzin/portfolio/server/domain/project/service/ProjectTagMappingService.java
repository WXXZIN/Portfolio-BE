package com.wxxzin.portfolio.server.domain.project.service;

import java.util.List;

import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectTagMapping;

public interface ProjectTagMappingService {
    void saveProjectTagMapping(List<ProjectTagMapping> projectTagMappings);
    void deleteAllByProjectEntity(ProjectEntity projectEntity);
}
