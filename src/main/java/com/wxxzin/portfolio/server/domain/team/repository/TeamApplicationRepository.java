package com.wxxzin.portfolio.server.domain.team.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.team.entity.TeamApplication;
import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;
import com.wxxzin.portfolio.server.domain.team.enums.ApplicationStatus;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

@Repository
public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Long> {
    
    Optional<TeamApplication> findByTeamEntityAndBaseUserEntity(TeamEntity teamEntity, BaseUserEntity targetBaseUserEntity);
    Page<TeamApplication> findByTeamEntity(TeamEntity teamEntity, Pageable pageable);
    Page<TeamApplication> findByTeamEntityAndApplicationStatus(TeamEntity teamEntity, ApplicationStatus approved, Pageable pageable);
    boolean existsByTeamEntity_IdAndBaseUserEntity_Id(Long teamId, Long userId);
}
