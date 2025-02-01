package com.wxxzin.portfolio.server.domain.user.service.implement;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.exception.CreationException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.dto.request.LocalUserRegisterRequestDTO;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.entity.LocalUserEntity;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.repository.BaseUserRepository;
import com.wxxzin.portfolio.server.domain.user.repository.LocalUserRepository;
import com.wxxzin.portfolio.server.domain.user.service.LocalUserService;

@Service
@Slf4j
public class LocalUserServiceImpl implements LocalUserService {

    private final BaseUserRepository baseUserRepository;
    private final LocalUserRepository localUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public LocalUserServiceImpl(
        BaseUserRepository baseUserRepository, 
        LocalUserRepository localUserRepository,
        BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.baseUserRepository = baseUserRepository;
        this.localUserRepository = localUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameTaken(String username) {
        return localUserRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public void registerUser(LocalUserRegisterRequestDTO localUserRegisterRequestDTO) {
        checkForDuplicate(localUserRegisterRequestDTO);

        BaseUserEntity baseUserEntity = createBaseUserEntity(localUserRegisterRequestDTO);
        baseUserRepository.save(baseUserEntity);

        LocalUserEntity localUserEntity = createLocalUserEntity(baseUserEntity, localUserRegisterRequestDTO);
        localUserRepository.save(localUserEntity);
    }

    @Transactional(readOnly = true)
    protected void checkForDuplicate(LocalUserRegisterRequestDTO userRequestDTO) {
        String username = userRequestDTO.username();
        String nickname = userRequestDTO.nickname();

        if (localUserRepository.existsByUsername(username)) {
            throw new CreationException(ErrorMessage.USERNAME_ALREADY_EXISTS);
        }

        if (baseUserRepository.existsByNickname(nickname)) {
            throw new CreationException(ErrorMessage.NICKNAME_ALREADY_EXISTS);
        }
    }

    private BaseUserEntity createBaseUserEntity(LocalUserRegisterRequestDTO localUserRegisterRequestDTO) {
        return BaseUserEntity.builder()
                .provider(Provider.LOCAL)
                .nickname(localUserRegisterRequestDTO.nickname())
                .email(localUserRegisterRequestDTO.email())
                .build();
    }

    private LocalUserEntity createLocalUserEntity(BaseUserEntity baseUserEntity, LocalUserRegisterRequestDTO localUserRegisterRequestDTO) {
        return LocalUserEntity.builder()
                .baseUserEntity(baseUserEntity)
                .username(localUserRegisterRequestDTO.username())
                .password(bCryptPasswordEncoder.encode(localUserRegisterRequestDTO.password()))
                .build();
    }
}
