package com.wxxzin.portfolio.server.domain.team.repository;

import com.wxxzin.portfolio.server.domain.team.entity.TeamEntity;
import com.wxxzin.portfolio.server.domain.team.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.team.entity.TeamTaskEntity;

@Repository
public interface TeamTaskRepository extends JpaRepository<TeamTaskEntity, Long> {
    Page<TeamTaskEntity> findByTeamEntityAndTaskStatus(TeamEntity teamEntity, TaskStatus taskStatus, Pageable pageable);
}
