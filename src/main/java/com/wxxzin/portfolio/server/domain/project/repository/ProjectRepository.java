package com.wxxzin.portfolio.server.domain.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Page<ProjectEntity> findByHeartEntities_BaseUserEntity_Id(Long userId, Pageable pageable);

    @Query("SELECT p FROM ProjectEntity p WHERE " +
        "(:searchType = 'title' AND p.title LIKE %:searchKeyword%) OR " +
        "(:searchType = 'content' AND p.content LIKE %:searchKeyword%) OR " +
        "(:searchType = 'nickname' AND p.baseUserEntity.nickname = :searchKeyword) OR " +
        "(:searchType = 'tag' AND EXISTS (" +
        "   SELECT 1 FROM p.projectTagMappings ptm WHERE ptm.tagEntity.tagName = :searchKeyword" +
        ")) " +
        "ORDER BY " +
        "   CASE WHEN :sortBy = 'latest' THEN p.createdAt ELSE NULL END DESC, " +
        "   CASE WHEN :sortBy = 'popularity' THEN SIZE(p.heartEntities) ELSE NULL END DESC, " +
        "   CASE WHEN :sortBy = 'views' THEN p.viewCount ELSE NULL END DESC")
    Page<ProjectEntity> searchProjects(@Param("searchType") String searchType, 
                                @Param("searchKeyword") String searchKeyword, 
                                @Param("sortBy") String sortBy, 
                                Pageable pageable);

    @Modifying
    @Query("UPDATE ProjectEntity p SET p.viewCount = p.viewCount + 1 WHERE p.id = :projectId")
    void increaseViewCount(@Param("projectId") Long projectId);
}
