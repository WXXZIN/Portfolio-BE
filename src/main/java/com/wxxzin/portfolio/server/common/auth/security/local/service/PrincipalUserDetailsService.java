package com.wxxzin.portfolio.server.common.auth.security.local.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUserDetails;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.entity.LocalUserEntity;
import com.wxxzin.portfolio.server.domain.user.repository.LocalUserRepository;

@Service
public class PrincipalUserDetailsService implements UserDetailsService {

    private final LocalUserRepository localUserRepository;

    public PrincipalUserDetailsService(LocalUserRepository localUserRepository) {
        this.localUserRepository = localUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LocalUserEntity localUserEntity = localUserRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                ErrorMessage.USER_NOT_FOUND.getMessage()
                        )
                );

        return new PrincipalUserDetails(UserResponseDTO.of(localUserEntity));
    }
}
