package com.wxxzin.portfolio.server.domain.team.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.team.entity.TeamMemberMapping;

@Repository
public interface TeamMemberMappingRepository extends JpaRepository<TeamMemberMapping, Long> {

    List<TeamMemberMapping> findByBaseUserEntity_Id(Long userId);

    boolean existsByTeamEntity_IdAndBaseUserEntity_Id(Long teamId, Long userId);

    TeamMemberMapping findByTeamEntity_IdAndBaseUserEntity_Id(Long teamId, Long userId);

}
