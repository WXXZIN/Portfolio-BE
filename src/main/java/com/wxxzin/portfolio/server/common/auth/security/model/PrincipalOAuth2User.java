package com.wxxzin.portfolio.server.common.auth.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

public record PrincipalOAuth2User(UserResponseDTO userResponseDTO) implements PrincipalUser, OAuth2User {

    /* PrincipalUser */

    @Override
    public Long getUserId() {
        return userResponseDTO.userId();
    }

    @Override
    public Provider getProvider() {
        return userResponseDTO.provider();
    }

    /* OAuth2User */

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userResponseDTO.role().name()));

        return authorities;
    }

    @Override
    public String getName() {
        return userResponseDTO.nickname();
    }
}
