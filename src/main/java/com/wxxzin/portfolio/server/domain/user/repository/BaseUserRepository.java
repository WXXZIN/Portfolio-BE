package com.wxxzin.portfolio.server.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

import java.util.Optional;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUserEntity, Long> {
    Optional<BaseUserEntity> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    boolean existsByProviderAndEmail(Provider provider, String email);
}
