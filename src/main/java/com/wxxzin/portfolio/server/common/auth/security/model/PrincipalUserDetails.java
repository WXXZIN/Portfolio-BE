package com.wxxzin.portfolio.server.common.auth.security.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

public record PrincipalUserDetails(UserResponseDTO userResponseDTO) implements PrincipalUser, UserDetails {

    /* PrincipalUser */

    @Override
    public Long getUserId() {
        return userResponseDTO.userId();
    }

    @Override
    public Provider getProvider() {
        return userResponseDTO.provider();
    }

    /* UserDetails */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userResponseDTO.role().name()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return userResponseDTO.password();
    }

    @Override
    public String getUsername() {
        return userResponseDTO.username();
    }
}
