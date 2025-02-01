package com.wxxzin.portfolio.server.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.entity.LocalUserEntity;
import com.wxxzin.portfolio.server.domain.user.entity.SocialUserEntity;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.enums.Role;

public record UserResponseDTO(
    @JsonIgnore Long userId,
    Provider provider,
    @JsonIgnore Role role,
    String nickname,
    String email,

    /* LOCAL */
    @JsonIgnore String username,
    @JsonIgnore String password
) {
    public static UserResponseDTO of(Long userId, Provider provider, Role role) {
        return new UserResponseDTO(userId, provider, role, null, null, null, null);
    }

    public static UserResponseDTO of(BaseUserEntity baseUserEntity) {
        return new UserResponseDTO(
            baseUserEntity.getId(),
            baseUserEntity.getProvider(),
            baseUserEntity.getRole(),
            baseUserEntity.getNickname(),
            baseUserEntity.getEmail(),
            null,
            null
        );
    }

    public static UserResponseDTO of(LocalUserEntity localUserEntity) {
        return new UserResponseDTO(
            localUserEntity.getBaseUserEntity().getId(),
            localUserEntity.getBaseUserEntity().getProvider(),
            localUserEntity.getBaseUserEntity().getRole(),
            localUserEntity.getBaseUserEntity().getNickname(),
            localUserEntity.getBaseUserEntity().getEmail(),
            localUserEntity.getUsername(),
            localUserEntity.getPassword()
        );
    }

    public static UserResponseDTO of(SocialUserEntity socialUserEntity) {
        return new UserResponseDTO(
            socialUserEntity.getBaseUserEntity().getId(),
            socialUserEntity.getBaseUserEntity().getProvider(),
            socialUserEntity.getBaseUserEntity().getRole(),
            socialUserEntity.getBaseUserEntity().getNickname(),
            socialUserEntity.getBaseUserEntity().getEmail(),
            null,
            null
        );
    }
}
