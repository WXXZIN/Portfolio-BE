package com.wxxzin.portfolio.server.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.user.entity.LocalUserEntity;

@Repository
public interface LocalUserRepository extends JpaRepository<LocalUserEntity, Long> {
    boolean existsByUsername(String username);
    Optional<LocalUserEntity> findByUsername(String username);
    Optional<LocalUserEntity> findByBaseUserEntity_Email(String email);
}
