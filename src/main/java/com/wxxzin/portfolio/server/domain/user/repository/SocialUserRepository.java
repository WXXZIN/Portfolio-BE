package com.wxxzin.portfolio.server.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wxxzin.portfolio.server.domain.user.entity.SocialUserEntity;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUserEntity, Long> {
    Optional<SocialUserEntity> findByBaseUserEntity_ProviderAndSocialId(Provider provider, String socialId);
}
