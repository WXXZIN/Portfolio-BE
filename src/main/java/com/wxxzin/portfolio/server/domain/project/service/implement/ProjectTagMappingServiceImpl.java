package com.wxxzin.portfolio.server.domain.project.service.implement;

import java.util.List;

import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import org.springframework.stereotype.Service;

import com.wxxzin.portfolio.server.domain.project.entity.ProjectTagMapping;
import com.wxxzin.portfolio.server.domain.project.repository.ProjectTagMappingRepository;
import com.wxxzin.portfolio.server.domain.project.service.ProjectTagMappingService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProjectTagMappingServiceImpl implements ProjectTagMappingService {

    private final ProjectTagMappingRepository projectTagMappingRepository;

    public ProjectTagMappingServiceImpl(ProjectTagMappingRepository projectTagMappingRepository) {
        this.projectTagMappingRepository = projectTagMappingRepository;
    }

    @Transactional
    @Override
    public void saveProjectTagMapping(List<ProjectTagMapping> projectTagMappings) {
        projectTagMappingRepository.saveAll(projectTagMappings);
    }

    @Transactional
    @Override
    public void deleteAllByProjectEntity(ProjectEntity projectEntity) {
        projectTagMappingRepository.deleteAllByProjectEntity(projectEntity);
    }
}
