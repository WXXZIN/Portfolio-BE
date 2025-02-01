package com.wxxzin.portfolio.server.domain.user.service;

import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

public interface BaseUserService {
    boolean isNicknameTaken(String nickname);
    UserResponseDTO getUserProfile(Long userId);
    BaseUserEntity findBaseUserEntityById(Long userId);
    BaseUserEntity findBaseUserEntityByNickname(String nickname);
    void changeNickname(Long userId, String newNickname);
}
