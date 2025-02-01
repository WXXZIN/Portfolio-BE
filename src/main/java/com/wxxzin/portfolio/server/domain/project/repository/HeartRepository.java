package com.wxxzin.portfolio.server.domain.project.repository;

import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.project.entity.HeartEntity;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

@Repository
public interface HeartRepository extends JpaRepository<HeartEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM HeartEntity h WHERE h.projectEntity = :projectEntity AND h.baseUserEntity = :baseUserEntity")
    Optional<HeartEntity> findByProjectEntityAndBaseUserEntity(ProjectEntity projectEntity, BaseUserEntity baseUserEntity);

}
