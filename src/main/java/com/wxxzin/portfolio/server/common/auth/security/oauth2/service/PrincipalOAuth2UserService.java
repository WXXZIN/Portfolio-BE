package com.wxxzin.portfolio.server.common.auth.security.oauth2.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalOAuth2User;
import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.OAuth2UserInfo;
import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.wxxzin.portfolio.server.common.auth.service.SocialUserAuthService;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialUserAuthService socialUserAuthService;

    public PrincipalOAuth2UserService(SocialUserAuthService socialUserAuthService) {
        this.socialUserAuthService = socialUserAuthService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception e) {
             throw new OAuth2AuthenticationException(e.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Provider provider = Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
        String accessToken = userRequest.getAccessToken().getTokenValue();

        UserResponseDTO userResponseDTO = socialUserAuthService.updateOrRegisterUserForWeb(provider, oAuth2UserInfo, accessToken);

        return new PrincipalOAuth2User(userResponseDTO);
    }
}
