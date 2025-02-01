package com.wxxzin.portfolio.server.common.auth.service.implement;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.dto.request.SocialUserRegisterRequestDTO;
import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceIdExtractor;
import com.wxxzin.portfolio.server.common.auth.security.device.util.DeviceIdGenerator;
import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.OAuth2UserInfo;
import com.wxxzin.portfolio.server.common.auth.service.AuthenticationService;
import com.wxxzin.portfolio.server.common.auth.service.SocialUserAuthService;
import com.wxxzin.portfolio.server.common.auth.util.NicknameGenerator;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.entity.SocialUserEntity;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.repository.BaseUserRepository;
import com.wxxzin.portfolio.server.domain.user.repository.SocialUserRepository;

@Service
@Slf4j
public class SocialUserAuthServiceImpl implements SocialUserAuthService {
    
    private final AuthenticationService authenticationService;
    private final BaseUserRepository baseUserRepository;
    private final SocialUserRepository socialUserRepository;
    private final DeviceIdExtractor deviceIdExtractor;
    private final DeviceIdGenerator deviceIdGenerator;
    private final NicknameGenerator nicknameGenerator;

    public SocialUserAuthServiceImpl(
        AuthenticationService authenticationService,
        BaseUserRepository baseUserRepository,
        SocialUserRepository socialUserRepository,
        DeviceIdExtractor deviceIdExtractor,
        DeviceIdGenerator deviceIdGenerator,
        NicknameGenerator nicknameGenerator
    ) {
        this.authenticationService = authenticationService;
        this.baseUserRepository = baseUserRepository;
        this.socialUserRepository = socialUserRepository;
        this.deviceIdExtractor = deviceIdExtractor;
        this.deviceIdGenerator = deviceIdGenerator;
        this.nicknameGenerator = nicknameGenerator;
    }

    @Override
    public UserAuthResponseDTO updateOrRegisterUserForApp(
        Provider provider,
        SocialUserRegisterRequestDTO socialUserRegisterRequestDTO,
        HttpServletRequest request
    ) {
        UserResponseDTO userResponseDTO = updateOrRegisterUser(provider, socialUserRegisterRequestDTO);

        String deviceId = deviceIdExtractor.getDeviceId(request)
            .orElse(deviceIdGenerator.createDeviceId(userResponseDTO.userId()));

        UserAuthResponseDTO userAuthResponseDTO = authenticationService.handleAuthenticationSuccess(
            userResponseDTO, deviceId, request
        );

        String accessToken = userAuthResponseDTO.accessToken();
        String refreshTokenCookie = authenticationService.createRefreshTokenCookie(userAuthResponseDTO.refreshToken()).toString();
        String deviceIdCookie = authenticationService.createDeviceIdCookie(userAuthResponseDTO.deviceId()).toString();

        return UserAuthResponseDTO.of(accessToken, refreshTokenCookie, deviceIdCookie, userResponseDTO);
    }

    private UserResponseDTO updateOrRegisterUser(
        Provider provider,
        OAuth2UserInfo oAuth2UserInfo
    ) {
        SocialUserEntity socialUserEntity = findSocialUser(provider, oAuth2UserInfo.getSocialId());

        if (socialUserEntity == null) {
            return registerUser(provider, oAuth2UserInfo);
        } else {
            return updateUser(socialUserEntity, oAuth2UserInfo);
        }
    }

    private SocialUserEntity findSocialUser(Provider provider, String socialId) {
        return socialUserRepository
            .findByBaseUserEntity_ProviderAndSocialId(provider, socialId)
            .orElse(null);
    }

    private UserResponseDTO registerUser(
        Provider provider,
        OAuth2UserInfo oAuth2UserInfo
    ) {
        BaseUserEntity baseUserEntity = createBaseUser(provider, oAuth2UserInfo);
        baseUserRepository.save(baseUserEntity);

        SocialUserEntity socialUserEntity = createSocialUser(baseUserEntity, oAuth2UserInfo.getSocialId());
        socialUserRepository.save(socialUserEntity);

        return UserResponseDTO.of(socialUserEntity);
    }

    private UserResponseDTO updateUser(
        SocialUserEntity socialUserEntity,
        OAuth2UserInfo oAuth2UserInfo
    ) {
        socialUserEntity.getBaseUserEntity().updateEmail(oAuth2UserInfo.getEmail());
        
        return UserResponseDTO.of(socialUserEntity);
    }

    private BaseUserEntity createBaseUser(Provider provider, OAuth2UserInfo oAuth2UserInfo) {
        return BaseUserEntity.builder()
            .provider(provider)
            .nickname(nicknameGenerator.createNickname())
            .email(oAuth2UserInfo.getEmail())
            .build();
    }

    private SocialUserEntity createSocialUser(BaseUserEntity baseUserEntity, String socialId) {
        return SocialUserEntity.builder()
            .baseUserEntity(baseUserEntity)
            .socialId(socialId)
            .build();
    }
}
