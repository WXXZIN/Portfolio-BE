package com.wxxzin.portfolio.server.common.auth.service.implement;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.dto.request.ChangePasswordRequestDTO;
import com.wxxzin.portfolio.server.common.auth.service.LocalUserAuthService;
import com.wxxzin.portfolio.server.common.exception.UserAuthException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.entity.LocalUserEntity;
import com.wxxzin.portfolio.server.domain.user.repository.LocalUserRepository;
import com.wxxzin.portfolio.server.domain.user.service.BaseUserService;

@Service
@Slf4j
public class LocalUserAuthServiceImpl implements LocalUserAuthService {
    
    private final LocalUserRepository localUserRepository;
    private final BaseUserService baseUserService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LocalUserAuthServiceImpl(
            LocalUserRepository localUserRepository,
            BaseUserService baseUserService
    ) {
        this.localUserRepository = localUserRepository;
        this.baseUserService = baseUserService;
    }

    @Override
    @Transactional(readOnly = true)
    public String findUsernameByEmail(String email) {
        return localUserRepository.findByBaseUserEntity_Email(email)
        .map(LocalUserEntity::getUsername)
        .orElseThrow(() -> new UserAuthException(ErrorMessage.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequestDTO requestDTO) {
        BaseUserEntity baseUserEntity = findBaseUserEntityById(userId);
        LocalUserEntity localUserEntity = baseUserEntity.getLocalUserEntity();

        String encodedNewPassword = passwordEncoder.encode(requestDTO.newPassword());

        validateCurrentPassword(localUserEntity.getPassword(), requestDTO.currentPassword());
        validatePassword(localUserEntity.getPassword(), requestDTO.newPassword());

        localUserEntity.updatePassword(encodedNewPassword);
        localUserRepository.save(localUserEntity);
    }

    @Transactional(readOnly = true)
    protected BaseUserEntity findBaseUserEntityById(Long userId) {
        return baseUserService.findBaseUserEntityById(userId);
    }

    private void validateCurrentPassword(String currentPassword, String inputPassword) {
        if (!passwordEncoder.matches(inputPassword, currentPassword)) {
            throw new UserAuthException(ErrorMessage.PASSWORD_NOT_MATCH);
        }
    }

    private void validatePassword(String currentPassword, String newPassword) {
        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new UserAuthException(ErrorMessage.PASSWORD_SAME_AS_BEFORE);
        }
    }
}
