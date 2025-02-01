package com.wxxzin.portfolio.server.domain.user.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

import java.util.Optional;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUserEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM BaseUserEntity u WHERE u.id = :id")
    Optional<BaseUserEntity> findById(Long id);
    Optional<BaseUserEntity> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    boolean existsByProviderAndEmail(Provider provider, String email);
}
