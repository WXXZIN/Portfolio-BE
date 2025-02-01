package com.wxxzin.portfolio.server.domain.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.project.entity.HeartEntity;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

@Repository
public interface HeartRepository extends JpaRepository<HeartEntity, Long> {

    boolean existsByProjectEntityAndBaseUserEntity(ProjectEntity projectEntity, BaseUserEntity baseUserEntity);

    Optional<HeartEntity> findByProjectEntityAndBaseUserEntity(ProjectEntity projectEntity, BaseUserEntity baseUserEntity);

}
