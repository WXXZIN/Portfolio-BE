package com.wxxzin.portfolio.server.common.auth.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.domain.user.repository.BaseUserRepository;

@Component
@Slf4j
public class NicknameGenerator {

    private final BaseUserRepository baseUserRepository;
    private final SecureRandom random = new SecureRandom();

    public NicknameGenerator(BaseUserRepository baseUserRepository) {
        this.baseUserRepository = baseUserRepository;
    }

    public String createNickname() {
        String nickname;

        do {
            nickname = "user_" + random.nextInt(100000000);
        } while (baseUserRepository.existsByNickname(nickname));

        return nickname;
    }
}
