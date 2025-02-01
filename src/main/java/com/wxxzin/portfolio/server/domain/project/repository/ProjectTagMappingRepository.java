package com.wxxzin.portfolio.server.domain.project.repository;

import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.project.entity.ProjectTagMapping;

@Repository
public interface ProjectTagMappingRepository extends JpaRepository<ProjectTagMapping, Long> {

    void deleteAllByProjectEntity(ProjectEntity projectEntity);
}
