package com.wxxzin.portfolio.server.domain.project.service.implement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wxxzin.portfolio.server.common.response.success.SuccessMessage;
import com.wxxzin.portfolio.server.domain.project.entity.HeartEntity;
import com.wxxzin.portfolio.server.domain.project.entity.ProjectEntity;
import com.wxxzin.portfolio.server.domain.project.repository.HeartRepository;
import com.wxxzin.portfolio.server.domain.project.service.HeartService;
import com.wxxzin.portfolio.server.domain.project.service.ProjectService;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.service.BaseUserService;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Service
@Slf4j
public class HeartServiceImpl implements HeartService{
    
    private final HeartRepository heartRepository;
    private final BaseUserService baseUserService;
    private final ProjectService projectService;

    public HeartServiceImpl(
        HeartRepository heartRepository,
        BaseUserService baseUserService,
        ProjectService projectService
    ) {
        this.heartRepository = heartRepository;
        this.baseUserService = baseUserService;
        this.projectService = projectService;
    }

    @Override
    @Transactional
    public String heartProject(Long userId, Long projectId) {
        ProjectEntity projectEntity = findProjectEntityById(projectId);
        BaseUserEntity baseUserEntity = findBaseUserEntityById(userId);

        Optional<HeartEntity> existingHeart = heartRepository.findByProjectEntityAndBaseUserEntity(projectEntity, baseUserEntity);

        if (existingHeart.isPresent()) {
            heartRepository.delete(existingHeart.get());
            return SuccessMessage.PROJECT_HEART_CANCEL_SUCCESS.getMessage();
        }

        HeartEntity heartEntity = HeartEntity.builder()
                .projectEntity(projectEntity)
                .baseUserEntity(baseUserEntity)
                .build();

        heartRepository.save(heartEntity);

        return SuccessMessage.PROJECT_HEART_SUCCESS.getMessage();
    }


    private BaseUserEntity findBaseUserEntityById(Long userId) {
        return baseUserService.findBaseUserEntityById(userId);
    }

    private ProjectEntity findProjectEntityById(Long projectId) {
        return projectService.findProjectEntityById(projectId);
    }
}
