package com.wxxzin.portfolio.server.domain.user.service.implement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.exception.ModificationException;
import com.wxxzin.portfolio.server.common.exception.RetrievalException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.repository.BaseUserRepository;
import com.wxxzin.portfolio.server.domain.user.service.BaseUserService;

@Service
@Slf4j
public class BaseUserServiceImpl implements BaseUserService {

    private final BaseUserRepository baseUserRepository;

    public BaseUserServiceImpl(BaseUserRepository baseUserRepository) {
        this.baseUserRepository = baseUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNicknameTaken(String nickname) {
        return baseUserRepository.existsByNickname(nickname);
    }

    @Override
    @Transactional
    public UserResponseDTO getUserProfile(Long userId) {
        BaseUserEntity baseUserEntity = findBaseUserEntityById(userId);
        
        return UserResponseDTO.of(baseUserEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseUserEntity findBaseUserEntityById(Long userId) {
        return baseUserRepository.findById(userId)
                .orElseThrow(() -> new RetrievalException(ErrorMessage.USER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public BaseUserEntity findBaseUserEntityByNickname(String nickname) {
        return baseUserRepository.findByNickname(nickname)
                .orElseThrow(() -> new RetrievalException(ErrorMessage.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void changeNickname(Long userId, String newNickname) {
        BaseUserEntity baseUserEntity = findBaseUserEntityById(userId);

        validateNickname(newNickname);

        baseUserEntity.updateNickname(newNickname);
        baseUserRepository.save(baseUserEntity);
    }

    @Transactional(readOnly = true)
    protected void validateNickname(String newNickname) {
        if (baseUserRepository.existsByNickname(newNickname)) {
            throw new ModificationException(ErrorMessage.NICKNAME_ALREADY_EXISTS);
        }
    }
}
